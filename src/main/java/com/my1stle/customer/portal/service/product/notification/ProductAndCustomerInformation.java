package com.my1stle.customer.portal.service.product.notification;

import java.math.BigDecimal;

public interface ProductAndCustomerInformation {
    String getInstallationName();
    String getProductName();
    int getProductQuantity();
    BigDecimal getTotalPaid();
}
