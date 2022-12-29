package com.my1stle.customer.portal.serviceImpl.cases;

import com.my1stle.customer.portal.service.InstallationService;
import com.my1stle.customer.portal.service.cases.CaseService;
import com.my1stle.customer.portal.service.cases.CaseSubmitResult;
import com.my1stle.customer.portal.service.model.Installation;
import com.my1stle.customer.portal.service.model.ServiceCase;
import com.my1stle.customer.portal.service.odoo.DefaultHelpdeskService;
import com.my1stle.customer.portal.service.odoo.HelpdeskServiceOdoo;
import com.my1stle.customer.portal.service.odoo.InstallationServiceOdoo;
import com.my1stle.customer.portal.service.odoo.OdooCaseDTO;
import com.my1stle.customer.portal.service.serviceapi.ExistingAttachmentDto;
import com.my1stle.customer.portal.service.serviceapi.ExistingServiceCaseDto;
import com.my1stle.customer.portal.service.serviceapi.ServiceApiCategory;
import com.my1stle.customer.portal.service.serviceapi.ServiceApiException;
import com.my1stle.customer.portal.service.serviceapi.ServiceApiGroup;
import com.my1stle.customer.portal.service.serviceapi.ServiceCaseDto;
import com.my1stle.customer.portal.service.serviceapi.ServiceCaseStatus;
import com.my1stle.customer.portal.service.serviceapi.ServiceCasesApi;
import com.my1stle.customer.portal.util.Visitor;
import com.my1stle.customer.portal.web.dto.cases.CaseDto;
import com.my1stle.customer.portal.web.exception.ResourceNotFoundException;
import org.apache.commons.lang3.StringUtils;
import org.apache.tika.io.IOUtils;
import org.baeldung.persistence.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Primary
public class ServiceApiCaseServiceDecorator implements CaseService {

    private final CaseService decoratedCaseService;
    private final ServiceCasesApi serviceCasesApi;
    private final InstallationService installationService;
    private final Visitor<ServiceCaseProxy> serviceCaseProxyVisitor;
    private final InstallationServiceOdoo installationServiceOdoo;
    private final HelpdeskServiceOdoo helpdeskServiceOdoo;

    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceApiCaseServiceDecorator.class);

    @Autowired
    public ServiceApiCaseServiceDecorator(
            @Qualifier("defaultCaseService") CaseService decoratedCaseService,
            ServiceCasesApi serviceCasesApi,
            InstallationService installationService,
            Visitor<ServiceCaseProxy> serviceCaseProxyVisitor,
            InstallationServiceOdoo installationServiceOdoo,
            HelpdeskServiceOdoo helpdeskServiceOdoo) {

        this.decoratedCaseService = decoratedCaseService;
        this.serviceCasesApi = serviceCasesApi;
        this.installationService = installationService;
        this.serviceCaseProxyVisitor = serviceCaseProxyVisitor;
        this.installationServiceOdoo = installationServiceOdoo;
        this.helpdeskServiceOdoo = helpdeskServiceOdoo;
    }

    @Override
    public List<ServiceCase> getCases() {

        List<ServiceCase> cases = this.decoratedCaseService.getCases();


        List<ServiceCaseProxy> proxies = this.installationService.getInstallations()
                .parallelStream()
                .map(Installation::getId)
                .map(externalId -> {
                    try {
                        return serviceCasesApi.getByExternalId(externalId);
                    } catch (ServiceApiException e) {
                        throw new RuntimeException(e.getMessage(), e);
                    }
                })
                .flatMap(List::stream)
                .map(ServiceCaseProxy::from)
                .filter(ServiceApiCaseServiceDecorator::isNotInternalTicket)
                .collect(Collectors.toList());

        //System.out.println(proxies);

        /*
        List<ServiceCaseProxy> proxies = this.installationService.getInstallations()
                .parallelStream()
                .map(Installation::getId)
                .map(externalId -> {
                    try {
                        return serviceCasesApi.getByExternalId("a064u00001nqPxOAAU");
                    } catch (ServiceApiException e) {
                        throw new RuntimeException(e.getMessage(), e);
                    }
                })
                .flatMap(List::stream)
                .map(ServiceCaseProxy::from)
                .filter(ServiceApiCaseServiceDecorator::isNotInternalTicket)
                .collect(Collectors.toList());
        */

        for (ServiceCaseProxy proxy : proxies) {
            proxy.accept(serviceCaseProxyVisitor);
        }

        List<ServiceCase> temp = new ArrayList<>();
        temp.addAll(cases);
        temp.addAll(proxies);

        return temp;

    }

    @Override
    public Optional<ServiceCase> get(String id) {
        if (StringUtils.isNumeric(id)) {
            //System.out.println("getServiceCase(id)" + id);
            //System.out.println(getServiceCase(id));
            return getServiceCase(id);
        }
        //System.out.println("this.decoratedCaseService.get(id)");
        //System.out.println(this.decoratedCaseService.get(id));
        return this.decoratedCaseService.get(id);
    }


    @Override
    public List<ExistingServiceCaseDto> getByOdooIdTest(String id) {
        if (StringUtils.isNumeric(id)) {
            return getServiceCaseProxyOdooTest(id);
        }
        return this.decoratedCaseService.getByOdooIdTest(id);
    }

    @Override
    public CaseSubmitResult submit(CaseDto dto) {

        Boolean isInstallOperational = dto.getIsInstallOperational();
        String chosenInstallationId = isInstallOperational ? dto.getInstallationId() : dto.getAddressChoiceId();
        String category = dto.getCategory();
        String preInstallIssue = dto.getPreInstallIssue();
        String preInstallDescription = dto.getPreInstallDescription();
        String description = getDescription(dto);
        Boolean interiorDamage = dto.getInteriorDamage() == null ? false : dto.getInteriorDamage();
        List<MultipartFile> attachments = dto.getAttachments();
        //System.out.println(dto.getAddressChoiceId());
        //test
        chosenInstallationId = "a064u00001nqPxOAAU";
        //test

        Installation installation = this.installationService.getInstallationById(chosenInstallationId);

        if(installation == null) {
            throw new ResourceNotFoundException("Installation Not Found!");
        }

        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long serviceApiUserId = currentUser.getServiceApiUserId();
        ServiceApiCategory serviceApiCategory = isInstallOperational ? ServiceApiCategory.valueOf(Long.valueOf(category)) : ServiceApiCategory.valueOf(Long.valueOf(preInstallIssue));
        String serviceDescription = isInstallOperational ? description : preInstallDescription;
        long issueTypeId = serviceApiCategory.getParentId() != null ? serviceApiCategory.getParentId() : serviceApiCategory.getId();
        Long subIssueTypeId = serviceApiCategory.getParentId() != null ? serviceApiCategory.getId() : null;
        String summary = serviceApiCategory.getLabel();
        Long groupId = determineGroupId(installation, issueTypeId);


        ServiceCaseDto serviceCaseDto = new ServiceCaseDto.Builder(serviceApiUserId, issueTypeId, summary, serviceDescription, ServiceCaseStatus.NEW)
                .subIssueType(subIssueTypeId)
                .externalId(chosenInstallationId)
                .groupId(groupId)
                .build();

        OdooCaseDTO odooCaseDTO = new OdooCaseDTO(serviceCaseDto, dto.getAddressChoiceId());

        //System.out.println(odooCaseDTO.getId());
        String odooId = odooCaseDTO.getId().toString();

        //System.out.println(odooId);

        ServiceCaseDto serviceCaseDto2 = new ServiceCaseDto.Builder(serviceApiUserId, issueTypeId, summary, serviceDescription, ServiceCaseStatus.NEW)
                .subIssueType(subIssueTypeId)
                .odooId(odooId)
                .externalId(chosenInstallationId)
                .groupId(groupId)
                .build();

        return submit(serviceApiUserId, serviceCaseDto2, interiorDamage ,attachments);

    }


    private Optional<ServiceCase> getServiceCase(String id) {

        /*
        Set<String> installationIds = this.installationService.getInstallations()
                .stream()
                .map(Installation::getId)
                .collect(Collectors.toSet());
        */

        Optional<ServiceCaseProxy> serviceCase = getServiceCaseProxy(id);

        //Optional<ServiceCaseProxy> serviceCase = getServiceCaseProxyOdoo(id);
        //System.out.println("getServiceCase " + id);
        //Try to add Odoo ID to list
        List<String> installationOdooNames = this.installationServiceOdoo.getInstallationByEmail().getName();

        if (serviceCase.isPresent()) {

            ServiceCaseProxy proxy = serviceCase.get();
            //System.out.println("proxy " + proxy);
            DefaultHelpdeskService helpdeskTicketInstallation = this.helpdeskServiceOdoo.getHelpdeskByTicketId(proxy.getOdooId());
            //Installation need to be switched to odoo ID, make sure Odoo contains the Odoo External ID
            //For this to work
            /*
            if (installationIds.contains(proxy.getExternalId()) && isNotInternalTicket(proxy)) {
                proxy.accept(this.serviceCaseProxyVisitor);
                return Optional.of(proxy);
            */
            if (installationOdooNames.contains(helpdeskTicketInstallation.getInstallationName()) && isNotInternalTicket(proxy)) {
                proxy.accept(this.serviceCaseProxyVisitor);
                return Optional.of(proxy);
            } else {
                return Optional.empty();
            }
        }
        return Optional.empty();
    }



    private Optional<ServiceCaseProxy> getServiceCaseProxy(String id) {
        try {
            return serviceCasesApi.get(Long.parseLong(id))
                    .map(ServiceCaseProxy::from);
        } catch (ServiceApiException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    // Using Odoo ID
    private List<ExistingServiceCaseDto> getServiceCaseProxyOdooTest(String id) {
        try {
            return serviceCasesApi.getByOdooIdTest(id);
        } catch (ServiceApiException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    private String getDescription(CaseDto dto){
        String description = dto.getDescription();
        if(dto.getInteriorDamage() != null && dto.getInteriorDamage()){
            description += "\n__________________________" + "\n Interior Damage";
        }
        if(dto.getInverterDisplayScreen() != null) {
            if (dto.getInverterDisplayScreen()) {
                description += "\n__________________________" + "\n Inverter Screen Present";
            } else if (!dto.getInverterDisplayScreen()) {
                description += "\n__________________________" + "\n Inverter Screen Not Present";
            }
        }
        return description;
    }

    private CaseSubmitResult submit(Long serviceApiUserId, ServiceCaseDto serviceCaseDto, Boolean interiorDamage, List<MultipartFile> attachments) {

        List<MultipartFile> multipartFiles = attachments
                .stream()
                .filter(attachment -> !StringUtils.isBlank(attachment.getOriginalFilename()))
                .collect(Collectors.toList());

        if(multipartFiles.isEmpty()) {
            Long issueType = serviceCaseDto.getIssueType();
            if(issueType == ServiceApiCategory.HIGH_UTILITY_BILL.getId()) {
                return new DefaultCaseSubmitResult(false, "At least one picture of a utility bill is required!");
            } else if(issueType == ServiceApiCategory.METER_ISSUE.getId()){
                return new DefaultCaseSubmitResult(false, "At least one picture of your meter is required!");
            } else if (issueType == ServiceApiCategory.ROOF_LEAK.getId() && interiorDamage) {
                return new DefaultCaseSubmitResult(false, "At least one picture of interior damage is required!");
            }
        }

        try {
            ExistingServiceCaseDto existingServiceCaseDto = this.serviceCasesApi.create(serviceCaseDto);
            List<String> attachmentNamesWithErrors = addAttachments(serviceApiUserId, existingServiceCaseDto, attachments)
                    .stream()
                    .map(MultipartFile::getOriginalFilename)
                    .collect(Collectors.toList());
            String additionalInfo = attachmentNamesWithErrors.isEmpty() ? "" : String.format("However we were unable to add the following attachment(s):\n %s. Please try again by viewing your case details", String.join(", ", attachmentNamesWithErrors));
            String message = String.format("Your case has been submitted! Your case number is %s. %s", existingServiceCaseDto.getId(), additionalInfo);
            return new DefaultCaseSubmitResult(true, message);
        } catch (ServiceApiException e) {
            LOGGER.error(e.getMessage(), e);
            return new DefaultCaseSubmitResult(false, "We're sorry, something went wrong. Please try once more.");
        }
    }

    /**
     * @param serviceApiUserId
     * @param existingServiceCaseDto
     * @param attachments
     * @return a list of attachment names in which there was an error uploading.
     */
    private List<MultipartFile> addAttachments(long serviceApiUserId, ExistingServiceCaseDto existingServiceCaseDto, List<MultipartFile> attachments) {
        long id = existingServiceCaseDto.getId();
        List<MultipartFile> filterAttachments = attachments
                .stream()
                .filter(attachment -> !StringUtils.isBlank(attachment.getOriginalFilename()))
                .collect(Collectors.toList());
        List<MultipartFile> errors = new ArrayList<>();
        for (MultipartFile attachment : filterAttachments) {
            try {
                ExistingAttachmentDto existingAttachmentDto = this.serviceCasesApi.addAttachment(id, serviceApiUserId, attachment.getOriginalFilename(), new ByteArrayResource(IOUtils.toByteArray(attachment.getInputStream())));
            } catch (ServiceApiException | IOException e) {
                LOGGER.error(e.getMessage(), e);
                errors.add(attachment);
            }
        }
        return errors;
    }

    private Long determineGroupId(Installation installation, long issueTypeId) {
        if(ServiceCaseDefaults.SERVICE_GROUP_ISSUE_TYPE.contains(issueTypeId)) {
            return ServiceApiGroup.SERVICE.getId();
        }
        /*if(ServiceCaseDefaults.DIVISION_GROUP_ISSUE_TYPE.contains(issueTypeId)) {
            return determineDivisionGroupId(installation);
        }*/
        return null;
    }

  /*  private Long determineDivisionGroupId(Installation installation) {
        return ServiceCaseDefaults.getGroupIdByRootstockDivisionMasterId(installation.getRootstockProjectMasterDivisionMasterId());
    }*/

    private static boolean isNotInternalTicket(ServiceCaseProxy proxy) {
        return proxy.getIssueTypeId() != ServiceApiCategory.INTERNAL.getId();
    }

}