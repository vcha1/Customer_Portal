package com.my1stle.customer.portal.service.paypal;

import com.my1stle.customer.portal.service.model.ServiceRequest;

import java.io.IOException;

public interface ServiceRequestPaymentProcessor {

    ServiceRequest confirmOrder(String orderId) throws IOException;

    ServiceRequest processServiceRequestPayment(String orderId) throws IOException;

}
