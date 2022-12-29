package com.my1stle.customer.portal.service.model;

import java.math.BigDecimal;
import java.util.Optional;

public interface OrderItemDiscount {

    String getName();

    String getDescription();

    BigDecimal getAmount();

    Optional<ProductDiscount> getProductDiscount();

}