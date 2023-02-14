package com.my1stle.customer.portal.service.model;

import com.my1stle.customer.portal.service.odoo.OdooInstallationData;
import org.baeldung.persistence.model.User;

import java.time.ZonedDateTime;

public interface ServiceRequest {

    Long getId();

    Product getProduct();

    int getQuantity();

    User getUser();

    CalendarResource getResource();

    ZonedDateTime getStartTime();

    OdooInstallationData getOdooInstallationData();

    PaymentDetail getPaymentDetail();

    String getCustomerNotes();


}
