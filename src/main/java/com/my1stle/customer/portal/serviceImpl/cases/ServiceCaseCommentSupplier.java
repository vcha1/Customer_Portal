package com.my1stle.customer.portal.serviceImpl.cases;

import com.my1stle.customer.portal.service.model.ServiceCaseComment;
import com.my1stle.customer.portal.service.serviceapi.ExistingCommentDto;
import com.my1stle.customer.portal.service.serviceapi.ExistingServiceUserDto;
import com.my1stle.customer.portal.service.serviceapi.ServiceApiException;
import com.my1stle.customer.portal.service.serviceapi.ServiceCasesApi;
import com.my1stle.customer.portal.service.serviceapi.ServiceUsersApi;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

class ServiceCaseCommentSupplier implements Supplier<List<ServiceCaseComment>> {

    private long caseId;
    private ServiceCasesApi serviceCasesApi;
    private ServiceUsersApi serviceUsersApi;

    ServiceCaseCommentSupplier(long caseId, ServiceCasesApi serviceCasesApi, ServiceUsersApi serviceUsersApi) {
        this.caseId = caseId;
        this.serviceCasesApi = serviceCasesApi;
        this.serviceUsersApi = serviceUsersApi;
    }

    /**
     * Gets a result.
     *
     * @return a result
     */
    @Override
    public List<ServiceCaseComment> get() {

        try {

            List<ExistingCommentDto> comments = this.serviceCasesApi.getComments(caseId)
                    .stream()
                    .filter(comment -> !comment.isInternal())
                    .collect(Collectors.toList());

            List<ServiceCaseComment> serviceCaseComments = new ArrayList<>();

            for (ExistingCommentDto commentDto : comments) {

                long id = commentDto.getId();
                long posterId = commentDto.getPosterId();
                String body = commentDto.getBody();
                ZonedDateTime createdDateTime = commentDto.getCreatedDateTime();
                ZonedDateTime lastModifiedDateTime = commentDto.getLastModifiedDateTime();

                ExistingServiceUserDto poster = this.serviceUsersApi.findById(posterId)
                        .orElseThrow(() -> new RuntimeException("Comment poster not found!"));
                ServiceCaseComment serviceCaseComment = new ServiceCaseComment(String.format("%s %s", poster.getFirstName(), poster.getLastName()), body, createdDateTime, lastModifiedDateTime);
                serviceCaseComments.add(serviceCaseComment);
            }

            return serviceCaseComments;


        } catch (ServiceApiException e) {
            throw new RuntimeException(e.getMessage(), e);
        }

    }
}
