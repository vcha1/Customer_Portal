package com.my1stle.customer.portal.service.installation.detail.model;

import java.util.Set;

public class TimelineStep {
	private String label;
	private Set<String> statuses;

	public String getLabel() {
		return label;
	}

	public Set<String> getStatuses() {
		return statuses;
	}
}
