package com.my1stle.customer.portal.service.attachment;

import com.dev1stle.repository.salesforce.SalesforceObjectRepository;
import com.dev1stle.repository.salesforce.client.SalesforceClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AttachmentInfoRepository extends SalesforceObjectRepository<AttachmentInfoSalesforceObject> {
	@Autowired
	public AttachmentInfoRepository(SalesforceClient client) {
		super(client);
	}
}
