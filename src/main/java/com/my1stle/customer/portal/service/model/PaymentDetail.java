package com.my1stle.customer.portal.service.model;

import org.baeldung.persistence.model.User;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

public interface PaymentDetail {

    Long getId();

    User getOwner();

    BigDecimal getAmount();

    BigDecimal getConvenienceFee();

    BigDecimal getTotal();

    PaymentMethod getPaymentMethod();

    ZonedDateTime getCreatedDate();

    String getExternalId();

}