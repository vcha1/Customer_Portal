package com.my1stle.customer.portal.service.model;

import java.math.BigDecimal;
import java.util.Optional;

public interface PaymentMethod {

    /**
     * @return the id of the payment
     */
    Long getId();

    /**
     * The name of the payment method
     *
     * @return name
     */
    String getName();

    /**
     * A brief description of the payment method
     *
     * @return description of the payment method
     */
    String getDescription();

    /**
     * fix convenience cost for said payment method
     *
     * @return big decimal representation of the convenience cost (e.g 12.00)
     */
    BigDecimal getConvenienceFeeFixed();

    /**
     * percentage convenience cost for said payment method
     *
     * @return big decimal representation of a percentage (e.g 0.0399 = %3.99)
     * @implSpec a BigDecimal between 0 and 1
     */
    BigDecimal getConvenienceFeePercentage();

}