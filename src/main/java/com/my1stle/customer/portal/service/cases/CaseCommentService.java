package com.my1stle.customer.portal.service.cases;

import com.my1stle.customer.portal.service.model.NewServiceCaseComment;
import com.my1stle.customer.portal.service.model.ServiceCase;
import com.my1stle.customer.portal.service.serviceapi.ServiceApiException;

public interface CaseCommentService {

    ServiceCase addComment(String caseId, NewServiceCaseComment comment) throws CaseServiceException, ServiceApiException;

}