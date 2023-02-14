package com.my1stle.customer.portal.service.cases;

import com.my1stle.customer.portal.service.model.NewServiceCaseAttachment;
import com.my1stle.customer.portal.service.model.ServiceCase;
import com.my1stle.customer.portal.service.serviceapi.ServiceApiException;

import java.io.IOException;

public interface CaseAttachmentService {

    ServiceCase addAttachment(String caseId, NewServiceCaseAttachment attachment) throws CaseServiceException, ServiceApiException, IOException;

}
