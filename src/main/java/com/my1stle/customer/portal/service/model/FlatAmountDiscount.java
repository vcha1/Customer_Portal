package com.my1stle.customer.portal.service.model;

import java.math.BigDecimal;

public interface FlatAmountDiscount extends ProductDiscount {

    BigDecimal getFlatDiscountAmount();

}