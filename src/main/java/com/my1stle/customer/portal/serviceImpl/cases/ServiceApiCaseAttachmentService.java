package com.my1stle.customer.portal.serviceImpl.cases;

import com.my1stle.customer.portal.service.InstallationService;
import com.my1stle.customer.portal.service.cases.CaseAttachmentService;
import com.my1stle.customer.portal.service.cases.CaseServiceException;
import com.my1stle.customer.portal.service.model.NewServiceCaseAttachment;
import com.my1stle.customer.portal.service.model.ServiceCase;
import com.my1stle.customer.portal.service.serviceapi.ExistingAttachmentDto;
import com.my1stle.customer.portal.service.serviceapi.ExistingServiceCaseDto;
import com.my1stle.customer.portal.service.serviceapi.ServiceApiException;
import com.my1stle.customer.portal.service.serviceapi.ServiceCasesApi;
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
import java.util.function.Predicate;

@Service
public class ServiceApiCaseAttachmentService implements CaseAttachmentService {

    private final ServiceCasesApi serviceCasesApi;
    private final InstallationService installationService;

    @Autowired
    public ServiceApiCaseAttachmentService(ServiceCasesApi serviceCasesApi, InstallationService installationService) {
        this.serviceCasesApi = serviceCasesApi;
        this.installationService = installationService;
    }

    @Override
    public ServiceCase addAttachment(String caseId, NewServiceCaseAttachment attachment) throws CaseServiceException {

        if (!StringUtils.isNumeric(caseId)) {
            throw new CaseServiceException("Invalid id");
        }

        ExistingServiceCaseDto existingServiceCase = getExistingServiceCase(Long.parseLong(caseId));
        validate(existingServiceCase);

        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
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


}
