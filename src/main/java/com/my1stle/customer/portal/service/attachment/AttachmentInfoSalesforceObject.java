package com.my1stle.customer.portal.service.attachment;

import com.dev1stle.repository.salesforce.model.SObjectApiName;
import com.dev1stle.repository.salesforce.model.SObjectField;
import com.dev1stle.repository.salesforce.model.SalesforceObject;

@SObjectApiName("Attachment")
public class AttachmentInfoSalesforceObject extends SalesforceObject implements AttachmentInfo {
	@SObjectField(api_name = "ParentId")
	private String parentId;

	@SObjectField(api_name = "Name")
	private String name;

	@SObjectField(api_name = "Description")
	private String description;

	@SObjectField(api_name = "ContentType")
	private String contentType;

	@SObjectField(api_name = "BodyLength")
	private Integer bodyLength;


	@Override
	public String getParentId() {
		return parentId;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public String getContentType() {
		return contentType;
	}

	@Override
	public Integer getBodyLength() {
		return bodyLength;
	}
}
