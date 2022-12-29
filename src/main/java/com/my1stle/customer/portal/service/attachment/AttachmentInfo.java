package com.my1stle.customer.portal.service.attachment;

import java.time.ZonedDateTime;

public interface AttachmentInfo {
	String getId();
	String getParentId();
	ZonedDateTime getCreatedDate();
	String getName();
	String getDescription();
	String getContentType();
	Integer getBodyLength();
}
