package com.my1stle.customer.portal.serviceImpl.paypal;

import com.my1stle.customer.portal.service.model.ServiceRequest;
import com.paypal.orders.Order;

class DefaultValidationResult implements ValidationResult {
    private Order order;
    private ServiceRequest serviceRequest;
    private boolean isValid;
    private String errorMessage;

    DefaultValidationResult(Order order, ServiceRequest serviceRequest) {
        this.order = order;
        this.serviceRequest = serviceRequest;
        this.isValid = true;
    }

    DefaultValidationResult(boolean isValid, String errorMessage) {
        if (isValid)
            throw new IllegalArgumentException("isValid value not supported!");

        this.isValid = false;
        this.errorMessage = errorMessage;
    }


    @Override
    public Order getOrder() {
        return order;
    }

    @Override
    public ServiceRequest getServiceRequest() {
        return serviceRequest;
    }

    @Override
    public boolean isValid() {
        return isValid;
    }

    @Override
    public String getErrorMessage() {
        return errorMessage;
    }

}
