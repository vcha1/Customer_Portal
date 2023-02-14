package com.my1stle.customer.portal.serviceImpl.cases;

import com.amazonaws.services.s3.AmazonS3;
import com.my1stle.customer.portal.service.InstallationService;
import com.my1stle.customer.portal.service.cases.CaseAttachmentService;
import com.my1stle.customer.portal.service.cases.CaseServiceException;
import com.my1stle.customer.portal.service.model.NewServiceCaseAttachment;
import com.my1stle.customer.portal.service.model.ServiceCase;
import com.my1stle.customer.portal.service.model.ServiceCaseAttachment;
import com.my1stle.customer.portal.service.odoo.OdooHelpdeskMessage;
import com.my1stle.customer.portal.service.odoo.OdooObjectConnection;
import com.my1stle.customer.portal.service.serviceapi.*;
import com.my1stle.customer.portal.web.exception.ResourceNotFoundException;
import org.apache.commons.lang3.StringUtils;
import org.apache.tika.io.IOUtils;
import org.baeldung.persistence.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
public class ServiceApiCaseAttachmentService implements CaseAttachmentService {

    private final ServiceCasesApi serviceCasesApi;
    private final InstallationService installationService;

    private final ServiceUsersApi serviceUsersApi;
    private final OdooHelpdeskMessage odooHelpdeskMessage;
    private final OdooObjectConnection odooObjectConnection;

    private final AmazonS3 amazonS3;


    List<ByteArrayResource> resources = new ArrayList<>();
    List<String> odooAttachmentNames = new ArrayList<>();




    @Autowired
    public ServiceApiCaseAttachmentService(ServiceCasesApi serviceCasesApi, InstallationService installationService,
                                           ServiceUsersApi serviceUsersApi, OdooHelpdeskMessage odooHelpdeskMessage,
                                           OdooObjectConnection odooObjectConnection, AmazonS3 amazonS3) {
        this.serviceCasesApi = serviceCasesApi;
        this.installationService = installationService;
        this.serviceUsersApi = serviceUsersApi;
        this.odooHelpdeskMessage = odooHelpdeskMessage;
        this.odooObjectConnection = odooObjectConnection;
        this.amazonS3 = amazonS3;
    }

    @Override
    public ServiceCase addAttachment(String caseId, NewServiceCaseAttachment attachment) throws CaseServiceException, ServiceApiException, IOException {

        if (!StringUtils.isNumeric(caseId)) {
            throw new CaseServiceException("Invalid id");
        }

        ExistingServiceCaseDto existingServiceCase = getExistingServiceCase(Long.parseLong(caseId));
        //validate(existingServiceCase);

        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        //putting attachment straight to Odoo from customer portal  before putting in php/AWS
        createAttachmentInOdoo(currentUser.getEmail(), existingServiceCase.getId(),attachment.getFile(), existingServiceCase.getOdooId());

        addAttachment(existingServiceCase.getId(), currentUser.getServiceApiUserId(), attachment.getFile());
        return ServiceCaseProxy.from(existingServiceCase);

    }

    private ExistingServiceCaseDto getExistingServiceCase(long id) throws CaseServiceException {
        try {
            return serviceCasesApi.get(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Case Not Found!"));
        } catch (ServiceApiException e) {
            throw new CaseServiceException(e.getMessage(), e);
        }
    }

    private void validate(ExistingServiceCaseDto existingServiceCase) {
        Predicate<ExistingServiceCaseDto> currentUserOwnsExistingServiceCase = new CurrentUserOwnsExistingServiceCase(this.installationService);
        if (!currentUserOwnsExistingServiceCase.test(existingServiceCase)) {
            throw new ResourceNotFoundException("Case Not Found!");
        }
    }

    private void addAttachment(long caseId, long serviceApiUserId, MultipartFile file) throws CaseServiceException {
        try {
            ExistingAttachmentDto existingAttachmentDto = this.serviceCasesApi.addAttachment(
                    caseId,
                    serviceApiUserId,
                    file.getOriginalFilename(),
                    new ByteArrayResource(IOUtils.toByteArray(file.getInputStream()))
            );
        } catch (ServiceApiException e) {
            throw new CaseServiceException(e.getMessage(), e);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }


    //putting attachment straight to Odoo from customer portal  before putting in php/AWS
    public void createAttachmentInOdoo(String currentUserEmail, Long existingServiceCaseId,
                                       MultipartFile attachmentFile, String existingServiceCaseOdooId) throws IOException, ServiceApiException {
        String objectType = "mail.message";
        String objectTypeAttachment = "ir.attachment";

        //Try to retrieve attachments from Odoo
        odooAttachmentRetriever(Integer.valueOf(existingServiceCaseOdooId), objectTypeAttachment);

        String attachmentName =attachmentFile.getName();
        String encoded = setEncodeAndAttachmentNames(attachmentFile);

        addAWSPhpAttachmentToOdoo(Integer.valueOf(existingServiceCaseOdooId), objectTypeAttachment, objectType, attachmentName, encoded);


        odooAttachmentNames.clear();
        resources.clear();;

    }

    private void odooAttachmentRetriever(Integer odooId,String objectTypeAttachment){
        List<Map<String, ?>> attachmentResults = odooHelpdeskMessage.getTicketAttachment(odooId, odooObjectConnection, objectTypeAttachment);
        for (Map<String, ?> attachmentResult : attachmentResults) {
            Object datas = attachmentResult.get("datas");
            byte[] decoded = Base64.getDecoder().decode(datas.toString());
            //only to add from odoo to php will resources below be required
            resources.add(new ByteArrayResource(decoded));
            odooAttachmentNames.add(attachmentResult.get("name").toString());
        }
    }

    private String setEncodeAndAttachmentNames(MultipartFile  attachmentFile) throws IOException {
        InputStream is = attachmentFile.getResource().getInputStream();
        byte[] targetArray = new byte[is.available()];
        is.read(targetArray);
        return Base64.getEncoder().encodeToString(targetArray);
    }

    private void addAWSPhpAttachmentToOdoo(Integer odooId, String objectTypeAttachment, String objectType,
                                           String attachmentName, String encoded){
        if (!odooAttachmentNames.contains(attachmentName)) {
            //Adds the atttachment in the ir.attachment
            List<Integer> num = odooHelpdeskMessage.createTicketAttachment(odooId, odooObjectConnection, objectTypeAttachment, encoded, attachmentName);
            //This works at creating a message with the created Attachment in Odoo
            odooHelpdeskMessage.createTicketAttachmentMessage(odooId, odooObjectConnection, objectType, num.get(0));
        }

    }


}
