package com.my1stle.customer.portal.persistence.repository.installation.detail;

import com.my1stle.customer.portal.service.installation.configuration.salesforce.repository.WorkOrderComponentRepository;
import com.my1stle.customer.portal.persistence.model.WorkOrderComponentSalesforceObject;
import com.dev1stle.repository.specification.salesforce.WhereClause;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class WorkOrderComponentRetriever {
	private final WorkOrderComponentRepository workOrderComponentRepository;

	@Autowired
	WorkOrderComponentRetriever(WorkOrderComponentRepository workOrderComponentRepository) {
		this.workOrderComponentRepository = workOrderComponentRepository;
	}

	public List<WorkOrderComponentSalesforceObject> findByProjectId(String projectId) {
		if(projectId == null || projectId.isEmpty())
			return Collections.emptyList();

		return workOrderComponentRepository.query(new WhereClause(
				String.format("rstk__woorddmd_proj__c = '%s'", projectId)
		));
	}
}
