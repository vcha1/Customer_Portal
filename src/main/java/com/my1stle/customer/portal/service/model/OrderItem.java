package com.my1stle.customer.portal.service.model;

import java.math.BigDecimal;
import java.util.Set;

public interface OrderItem {

    Long getId();

    String getDescription();

    BigDecimal getPricePerUnit();

    Integer getQuantity();

    BigDecimal getTotalPrice();

    Order getOrder();

    Product getProduct();

    Set<OrderItemDiscount> getOrderItemDiscounts();

}