package com.my1stle.customer.portal.service.appointment;

import com.my1stle.customer.portal.service.model.Appointment;

public interface AppointmentService {

    Appointment get(Long eventId);

    Appointment confirm(Long eventId, String token);

    Appointment reject(Long eventId, String token);

}