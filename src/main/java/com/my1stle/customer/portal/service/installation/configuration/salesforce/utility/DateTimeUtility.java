package com.my1stle.customer.portal.service.installation.configuration.salesforce.utility;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class DateTimeUtility {
	private static final ZoneId PACIFIC_ZONE = ZoneId.of("America/Los_Angeles");

	/**
	 * Converts the date time stored in Salesforce to the normalized, time zone agnostic form that
	 * it's intended to be in.
	 *
	 * Due to the nature of Salesforce date times, they are required to have a timezone, but some
	 * date times (e.g. PSA scheduled dates) are intended to be stored as the date and time as it
	 * would appear to the customer, but are stored as that date time in the Pacific time zone.
	 *
	 * @param storedTime the DateTime as defined in the Salesforce field
	 * @return the DateTime interpreted as a "universal" date-time without a time zone
	 */
	public static LocalDateTime interpretAsLocalDateTime(ZonedDateTime storedTime) {
		return storedTime.withZoneSameInstant(PACIFIC_ZONE).toLocalDateTime();
	}
}
