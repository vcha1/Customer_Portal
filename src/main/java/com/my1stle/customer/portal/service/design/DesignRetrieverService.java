package com.my1stle.customer.portal.service.design;

import com.my1stle.customer.portal.service.attachment.AttachmentData;
import com.my1stle.customer.portal.service.attachment.AttachmentDataRepository;
import com.my1stle.customer.portal.service.attachment.AttachmentInfo;
import com.my1stle.customer.portal.service.attachment.AttachmentInfoRepository;
import com.dev1stle.repository.specification.salesforce.WhereClause;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.List;

@Service
public class DesignRetrieverService {
	private final AttachmentDataRepository dataRepository;
	private final AttachmentInfoRepository infoRepository;

	private static final String PREDESIGN_FILE_NAME_PATTERN = "PREDESIGN%";
	private static final String ALTERNATE_PREDESIGN_FILE_NAME_PATTERN = "UPLOADED_" + PREDESIGN_FILE_NAME_PATTERN;

	@Autowired
	public DesignRetrieverService(
			AttachmentDataRepository dataRepository,
			AttachmentInfoRepository infoRepository
	) {
		this.dataRepository = dataRepository;
		this.infoRepository = infoRepository;
	}

	public AttachmentData getMainDesignForPredesign(String predesignId) {
		List<? extends AttachmentInfo> attachmentInfoList = infoRepository.query(new WhereClause(
				String.format(
						"ParentId = '%s' AND (Name LIKE '%s' OR Name LIKE '%s')",
						predesignId, PREDESIGN_FILE_NAME_PATTERN, ALTERNATE_PREDESIGN_FILE_NAME_PATTERN
				)
		));

		AttachmentInfo mostRecent = null;
		for(AttachmentInfo current : attachmentInfoList) {
			mostRecent = getMostRecent(mostRecent, current);
		}

		if(mostRecent == null)
			return null;

		return dataRepository.findById(mostRecent.getId()).orElse(null);
	}

	private AttachmentInfo getMostRecent(AttachmentInfo first, AttachmentInfo second) {
		if(first == null)
			return second;
		if(second == null)
			return first;

		ZonedDateTime firstCreated = first.getCreatedDate();
		ZonedDateTime secondCreated = second.getCreatedDate();

		if(firstCreated == null)
			return second;
		if(secondCreated == null)
			return first;

		return firstCreated.isAfter(secondCreated) ? first : second;
	}
}
