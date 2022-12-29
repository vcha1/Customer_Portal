package com.my1stle.customer.portal.service.model;

public interface PerAdditionalQuantityFlatAmountDiscount extends FlatAmountDiscount {

    int getMinApplicableQuantity();

    int getMaxApplicableQuantity();

}