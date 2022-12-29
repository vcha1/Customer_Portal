package com.my1stle.customer.portal.service.installation.configuration.salesforce.deserialization;

import com.dev1stle.repository.salesforce.deserializer.Deserializer;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class LocalDateDeserializer extends Deserializer<LocalDate> {
	private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

	@Override
	public LocalDate deserialize(String s) {
		return s == null ? null : LocalDate.parse(s, formatter);
	}
}
