package com.my1stle.customer.portal.service.model;

import com.my1stle.customer.portal.service.odoo.OdooInstallationData;
import com.my1stle.customer.portal.service.pricing.PaymentSchedule;
import org.baeldung.persistence.model.User;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;

public interface Subscription {

    Long getId();

    Boolean isActivated();

    User getOwner();

    BigDecimal getPrice();

    PaymentSchedule getPaymentFrequency();

    Product getProduct();

    //Installation getInstallation();

    OdooInstallationData getOdooInstallationData();

    ZonedDateTime getStartingDate();

    ZonedDateTime getExpirationDate();

    SubscriptionTermsOfService getSubscriptionTermsOfService();

    List<Order> getOrders();

}