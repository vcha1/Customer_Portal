package com.my1stle.customer.portal.service.datetimeselection;

import com.my1stle.customer.portal.service.model.ServiceRequest;
import com.my1stle.customer.portal.web.dto.scheduling.DateTimeSelectionDto;
import com.my1stle.customer.portal.web.dto.suggestion.Appointment;
import com.dev1stle.scheduling.system.v1.model.Calendar;

import java.util.List;
import java.util.Set;

public interface DateTimeSelectionService {

    Appointment createAppointment(String installationId, Long productId);

    Appointment createAppointment(String installationId, Integer durationInMinutes);

    List<Calendar> getAvailableCalendars(String installationId, Long productId);

    List<Calendar> getAvailableCalendars(String installationId, Set<Long> skillIds);

}