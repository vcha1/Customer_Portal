package com.my1stle.customer.portal.service.paypal;

import com.braintreepayments.http.HttpResponse;
import com.paypal.orders.Order;

import java.io.IOException;

public interface PayPalClient {
    Order getOrder(String orderId) throws IOException;

    HttpResponse<Order> captureOrder(String orderId, boolean debug) throws IOException;
}
