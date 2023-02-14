package com.my1stle.customer.portal.serviceImpl.cases;

import com.my1stle.customer.portal.service.InstallationService;
import com.my1stle.customer.portal.service.cases.CaseService;
import com.my1stle.customer.portal.service.cases.CaseSubmitResult;
import com.my1stle.customer.portal.service.model.Installation;
import com.my1stle.customer.portal.service.model.ServiceCase;
import com.my1stle.customer.portal.service.odoo.*;
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
import java.util.*;
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
    private final OdooHelpdeskMessageApi odooHelpdeskMessageApi;

    private final OdooObjectConnection odooObjectConnection;

    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceApiCaseServiceDecorator.class);

    @Autowired
    public ServiceApiCaseServiceDecorator(
            @Qualifier("defaultCaseService") CaseService decoratedCaseService,
            ServiceCasesApi serviceCasesApi,
            InstallationService installationService,
            Visitor<ServiceCaseProxy> serviceCaseProxyVisitor,
            InstallationServiceOdoo installationServiceOdoo,
            HelpdeskServiceOdoo helpdeskServiceOdoo,
            OdooHelpdeskMessageApi odooHelpdeskMessageApi,
            OdooObjectConnection odooObjectConnection) {

        this.decoratedCaseService = decoratedCaseService;
        this.serviceCasesApi = serviceCasesApi;
        this.installationService = installationService;
        this.serviceCaseProxyVisitor = serviceCaseProxyVisitor;
        this.installationServiceOdoo = installationServiceOdoo;
        this.helpdeskServiceOdoo = helpdeskServiceOdoo;
        this.odooHelpdeskMessageApi = odooHelpdeskMessageApi;
        this.odooObjectConnection = odooObjectConnection;
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
            return getServiceCase(id);
        }
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
        String category = dto.getCategory();
        String preInstallIssue = dto.getPreInstallIssue();
        String preInstallDescription = dto.getPreInstallDescription();
        String description = getDescription(dto);
        Boolean interiorDamage = dto.getInteriorDamage() == null ? false : dto.getInteriorDamage();
        List<MultipartFile> attachments = dto.getAttachments();

        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long serviceApiUserId = currentUser.getServiceApiUserId();

        ServiceApiCategory serviceApiCategory = isInstallOperational ? ServiceApiCategory.valueOf(Long.valueOf(category)) : ServiceApiCategory.valueOf(Long.valueOf(preInstallIssue));
        String serviceDescription = isInstallOperational ? description : preInstallDescription;
        long issueTypeId = serviceApiCategory.getParentId() != null ? serviceApiCategory.getParentId() : serviceApiCategory.getId();

        Long subIssueTypeId = serviceApiCategory.getParentId() != null ? serviceApiCategory.getId() : null;
        String summary = serviceApiCategory.getLabel();
        Long groupId = determineGroupId(issueTypeId);

        ServiceCaseDto serviceCaseDto = new ServiceCaseDto.Builder(serviceApiUserId, issueTypeId, summary, serviceDescription, ServiceCaseStatus.NEW)
                .subIssueType(subIssueTypeId)
                //.externalId(chosenInstallationId)
                .groupId(groupId)
                .build();

        if (dto.getAddressChoiceId() != null) {
            OdooCaseDTO odooCaseDTO = new OdooCaseDTO(serviceCaseDto, dto.getAddressChoiceId());

            String odooId = odooCaseDTO.getId().toString();

            ServiceCaseDto serviceCaseDto2 = new ServiceCaseDto.Builder(serviceApiUserId, issueTypeId, summary, serviceDescription, ServiceCaseStatus.NEW)
                    .subIssueType(subIssueTypeId)
                    .odooId(odooId)
                    //.externalId(chosenInstallationId)
                    .groupId(groupId)
                    .build();

            //add user as follower of the odoo ticket
            String userEmail = currentUser.getEmail();
            String fullName = currentUser.getFirstName() + " " + currentUser.getLastName();
            //Adds the customer as a follower of the ticket in helpdesk
            createMainFollowerAndFollower(userEmail, fullName, odooId);


            return submit(serviceApiUserId, serviceCaseDto2, interiorDamage ,attachments);
        }else if (dto.getAddressChoiceId() == null) {
            OdooCaseDTO odooCaseDTO = new OdooCaseDTO(serviceCaseDto, dto.getInstallationId(), "installationId");

            String odooId = odooCaseDTO.getId().toString();

            ServiceCaseDto serviceCaseDto2 = new ServiceCaseDto.Builder(serviceApiUserId, issueTypeId, summary, serviceDescription, ServiceCaseStatus.NEW)
                    .subIssueType(subIssueTypeId)
                    .odooId(odooId)
                    //.externalId(chosenInstallationId)
                    .groupId(groupId)
                    .build();

            //add user as follower of the odoo ticket
            String userEmail = currentUser.getEmail();
            String fullName = currentUser.getFirstName() + " " + currentUser.getLastName();
            //Adds the customer as a follower of the ticket in helpdesk
            createMainFollowerAndFollower(userEmail, fullName, odooId);

            return submit(serviceApiUserId, serviceCaseDto2, interiorDamage ,attachments);
        }

        return null;

    }


    private Optional<ServiceCase> getServiceCase(String id) {

        Optional<ServiceCaseProxy> serviceCase = getServiceCaseProxy(id);

        //Try to add Odoo ID to list
        List<String> installationOdooNames = this.installationServiceOdoo.getInstallationByEmail().getName();

        if (serviceCase.isPresent()) {

            ServiceCaseProxy proxy = serviceCase.get();
            DefaultHelpdeskService helpdeskTicketInstallation = this.helpdeskServiceOdoo.getHelpdeskByTicketId(proxy.getOdooId());

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

    //private Long determineGroupId(Installation installation, long issueTypeId) {
    private Long determineGroupId(long issueTypeId) {
        if(ServiceCaseDefaults.SERVICE_GROUP_ISSUE_TYPE.contains(issueTypeId)) {
            return ServiceApiGroup.SERVICE.getId();
        }
        return null;
    }


    private static boolean isNotInternalTicket(ServiceCaseProxy proxy) {
        return proxy.getIssueTypeId() != ServiceApiCategory.INTERNAL.getId();
    }



    //This function create all the necessary main follower and sub follower for the provided poster emails.
    private void createMainFollowerAndFollower(String userEmail, String name, String odooId){

        //Check to see if the Main Follower of the sub follower exists in Odoo
        List<Map<String, ?>> mainFollower = this.odooHelpdeskMessageApi.getMainEmailFollower(this.odooObjectConnection, userEmail.toLowerCase());

        //If they do not exist, create a main follower account, then after that, we can get the main follower ID
        String mainFollowerId;
        if (mainFollower.isEmpty()){
            List<Integer> newMainFollowerId = this.odooHelpdeskMessageApi.createMainFollower(this.odooObjectConnection, name, userEmail.toLowerCase());
            mainFollowerId = newMainFollowerId.get(0).toString();
        }else {
            mainFollowerId = mainFollower.get(0).get("id").toString();
        }


        Integer odooIdInt = Integer.valueOf(odooId);
        //Check if the one that post the comment is a follower of the ticket
        List<Map<String, ?>> followersFound = this.odooHelpdeskMessageApi.getFollower(this.odooObjectConnection, odooIdInt, mainFollowerId);

        //If the person that post the comment cannot be found as a follower, create them as a follower in the ticket
        if (followersFound.isEmpty()){
            String resModel = "helpdesk.ticket";
            String subtype = "1";
            List<Integer> newFollowerId = this.odooHelpdeskMessageApi.createFollower(this.odooObjectConnection, resModel, odooIdInt, subtype, mainFollowerId);
            followersFound = this.odooHelpdeskMessageApi.getFollower(this.odooObjectConnection, odooIdInt, mainFollowerId);
        }

    }

}