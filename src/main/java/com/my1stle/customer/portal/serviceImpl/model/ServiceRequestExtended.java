package com.my1stle.customer.portal.serviceImpl.model;

import com.my1stle.customer.portal.persistence.model.ServiceRequestEntity;
import com.my1stle.customer.portal.service.model.CalendarResource;
import com.my1stle.customer.portal.service.model.Installation;
import com.my1stle.customer.portal.service.model.PaymentDetail;
import com.my1stle.customer.portal.service.model.Product;
import com.my1stle.customer.portal.service.model.ServiceRequest;
import com.my1stle.customer.portal.service.odoo.OdooInstallationData;
import org.baeldung.persistence.model.User;

import java.time.ZonedDateTime;

public class ServiceRequestExtended implements ServiceRequest {

    private ServiceRequestEntity serviceRequestEntity;
    private CalendarResource calendarResource;
    //private Installation installation;

    private OdooInstallationData odooInstallationData;

    /*
    public ServiceRequestExtended(ServiceRequestEntity serviceRequestEntity, CalendarResource calendarResource, Installation installation) {
        this.serviceRequestEntity = serviceRequestEntity;
        this.calendarResource = calendarResource;
        this.installation = installation;
    }
    */

    public ServiceRequestExtended(ServiceRequestEntity serviceRequestEntity, CalendarResource calendarResource, OdooInstallationData odooInstallationData) {
        this.serviceRequestEntity = serviceRequestEntity;
        this.calendarResource = calendarResource;
        this.odooInstallationData = odooInstallationData;
    }

    @Override
    public Long getId() {
        return this.serviceRequestEntity.getId();
    }

    @Override
    public Product getProduct() {
        return this.serviceRequestEntity.getProduct();
    }

    @Override
    public int getQuantity() {
        return this.serviceRequestEntity.getQuantity();
    }

    @Override
    public User getUser() {
        return this.serviceRequestEntity.getUser();
    }

    @Override
    public CalendarResource getResource() {
        return calendarResource;
    }

    @Override
    public ZonedDateTime getStartTime() {
        return this.serviceRequestEntity.getStartTime();
    }

    /*
    @Override
    public Installation getInstallation() {
        return this.installation;
    }
    */

    @Override
    public OdooInstallationData getOdooInstallationData() {
        return this.odooInstallationData;
    }


    @Override
    public PaymentDetail getPaymentDetail() {
        return this.serviceRequestEntity.getPaymentDetail();
    }

    @Override
    public String getCustomerNotes() {
        return this.serviceRequestEntity.getCustomerNotes();
    }

}
