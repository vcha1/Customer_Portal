package com.my1stle.customer.portal.service.attachment;

import com.dev1stle.repository.salesforce.SalesforceObjectRepository;
import com.dev1stle.repository.salesforce.client.SalesforceClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AttachmentDataRepository extends SalesforceObjectRepository<AttachmentDataSalesforceObject> {
	@Autowired
	public AttachmentDataRepository(SalesforceClient client) {
		super(client);
	}
}
