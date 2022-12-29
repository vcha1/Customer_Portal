package com.my1stle.customer.portal.serviceImpl.paypal;

import com.my1stle.customer.portal.service.model.ServiceRequest;
import com.paypal.orders.Order;

interface ValidationResult {
    Order getOrder();

    ServiceRequest getServiceRequest();

    boolean isValid();

    String getErrorMessage();
}
