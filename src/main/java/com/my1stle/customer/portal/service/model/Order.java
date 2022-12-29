package com.my1stle.customer.portal.service.model;

import org.baeldung.persistence.model.User;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;

public interface Order {

    Long getId();

    User getUser();

    ZonedDateTime getOrderDate();

    BigDecimal getAmountDue();

    BigDecimal getAmountPaid();

    List<OrderItem> getOrderItems();

    List<PaymentDetail> getPaymentDetails();

}