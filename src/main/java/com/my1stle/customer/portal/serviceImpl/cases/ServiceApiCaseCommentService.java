package com.my1stle.customer.portal.serviceImpl.cases;

import com.my1stle.customer.portal.service.InstallationService;
import com.my1stle.customer.portal.service.cases.CaseCommentService;
import com.my1stle.customer.portal.service.cases.CaseServiceException;
import com.my1stle.customer.portal.service.model.NewServiceCaseComment;
import com.my1stle.customer.portal.service.model.ServiceCase;
import com.my1stle.customer.portal.service.serviceapi.CommentDto;
import com.my1stle.customer.portal.service.serviceapi.ExistingServiceCaseDto;
import com.my1stle.customer.portal.service.serviceapi.ServiceApiException;
import com.my1stle.customer.portal.service.serviceapi.ServiceCasesApi;
import com.my1stle.customer.portal.web.exception.ResourceNotFoundException;
import org.apache.commons.lang3.StringUtils;
import org.baeldung.persistence.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.function.Predicate;

@Service
public class ServiceApiCaseCommentService implements CaseCommentService {

    private final ServiceCasesApi serviceCasesApi;
    private final InstallationService installationService;

    @Autowired
    public ServiceApiCaseCommentService(ServiceCasesApi serviceCasesApi, InstallationService installationService) {
        this.serviceCasesApi = serviceCasesApi;
        this.installationService = installationService;
    }

    @Override
    public ServiceCase addComment(String caseId, NewServiceCaseComment comment) throws CaseServiceException {

        if (!StringUtils.isNumeric(caseId)) {
            throw new CaseServiceException("Invalid id");
        }

        long id = Long.parseLong(caseId);

        ExistingServiceCaseDto existingServiceCase = getExistingServiceCase(id);

        validate(existingServiceCase);

        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long serviceApiUserId = currentUser.getServiceApiUserId();
        String body = comment.getBody();

        CommentDto commentDto = new CommentDto(serviceApiUserId, body, false);
        addComment(existingServiceCase.getId(), commentDto);

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

    private void addComment(long id, CommentDto commentDto) throws CaseServiceException {
        try {
            this.serviceCasesApi.addComment(id, commentDto);
        } catch (ServiceApiException e) {
            throw new CaseServiceException(e.getMessage(), e);
        }
    }

}
