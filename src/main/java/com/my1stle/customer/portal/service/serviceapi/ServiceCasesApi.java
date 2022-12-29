package com.my1stle.customer.portal.service.serviceapi;

import org.springframework.core.io.Resource;

import java.util.List;
import java.util.Optional;

public interface ServiceCasesApi {

    Optional<ExistingServiceCaseDto> get(long caseId) throws ServiceApiException;

    Optional<ExistingServiceCaseDto> getByOdooId(long caseId) throws ServiceApiException;

    List<ExistingServiceCaseDto> getByExternalId(String externalId) throws ServiceApiException;

    List<ExistingServiceCaseDto> getByStatus(ServiceCaseStatus status) throws ServiceApiException;

    ExistingServiceCaseDto create(ServiceCaseDto serviceCaseDto) throws ServiceApiException;

    ExistingServiceCaseDto update(long caseId, ServiceCaseDto caseDto) throws ServiceApiException;

    ExistingCommentDto updateComment(long caseId, long commentId, CommentDto commentDto) throws ServiceApiException;

    ExistingCommentDto addComment(long caseId, CommentDto commentDto) throws ServiceApiException;

    ExistingAttachmentDto addAttachment(long caseId, long ownerId, String name, Resource resource) throws ServiceApiException;

    List<ExistingCommentDto> getComments(long caseId) throws ServiceApiException;

    Optional<ExistingCommentDto> getCommentBy(long caseId, long commentId) throws ServiceApiException;

    List<ExistingAttachmentDto> getAttachments(long caseId) throws ServiceApiException;

    Optional<ExistingAttachmentDto> getAttachmentBy(long caseId, long attachmentId) throws ServiceApiException;

}