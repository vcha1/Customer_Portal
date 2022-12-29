package com.my1stle.customer.portal.service.model;

import com.dev1stle.scheduling.system.v1.model.salesforce.CustomerInformation;
import com.dev1stle.scheduling.system.v1.service.truck.roll.api.model.EventDetail;
import com.my1stle.customer.portal.service.appointment.AppointmentStatus;

public interface Appointment {

    AppointmentStatus getStatus();

    EventDetail getEventDetail();

    CustomerInformation getCustomerInformation();

}