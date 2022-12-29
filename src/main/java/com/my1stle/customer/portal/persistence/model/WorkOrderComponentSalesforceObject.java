package com.my1stle.customer.portal.persistence.model;

import com.dev1stle.repository.salesforce.model.SObjectApiName;
import com.dev1stle.repository.salesforce.model.SObjectField;
import com.dev1stle.repository.salesforce.model.SalesforceObject;

@SObjectApiName("rstk__woorddmd__c")
public class WorkOrderComponentSalesforceObject extends SalesforceObject {
	@SObjectField(api_name = "Name")
	private String name;

	@SObjectField(api_name = "rstk__woorddmd_desc__c", read_only = true)
	private String description;

	@SObjectField(api_name = "rstk__woorddmd_qtyper__c")
	private Double totalQuantity;

	@SObjectField(api_name = "rstk__woorddmd_proj__c")
	private String projectId;

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public Double getTotalQuantity() {
		return totalQuantity;
	}

	public String getProjectId() {
		return projectId;
	}
}
