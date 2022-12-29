package com.my1stle.customer.portal.service.installation.detail.model;

import java.util.List;
import java.util.Set;

public class Timeline {
	private List<TimelineStep> steps;

	public List<TimelineStep> getSteps() {
		return this.steps;
	}

	/**
	 * The step that the status is contained in, -1 when not found.
	 * @param status an installation status
	 * @return -1 when not found, an integer 0 or above indicating the timeline step that contains
	 *      the status;
	 */
	public int getStepForStatus(String status) {
		for(int idx = 0; idx < steps.size(); idx++) {
			TimelineStep step = steps.get(idx);
			Set<String> statuses = step.getStatuses();
			if(statuses != null && statuses.contains(status)) {
				return idx;
			}
		}

		return -1;
	}
}
