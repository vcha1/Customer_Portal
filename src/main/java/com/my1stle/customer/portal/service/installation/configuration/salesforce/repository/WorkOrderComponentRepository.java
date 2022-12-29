package com.my1stle.customer.portal.service.installation.configuration.salesforce.repository;

import com.my1stle.customer.portal.persistence.model.WorkOrderComponentSalesforceObject;
import com.dev1stle.repository.salesforce.SalesforceObjectRepository;
import com.dev1stle.repository.salesforce.client.SalesforceClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WorkOrderComponentRepository extends SalesforceObjectRepository<WorkOrderComponentSalesforceObject> {
	@Autowired
	public WorkOrderComponentRepository(SalesforceClient client) {
		super(client);
	}
}
