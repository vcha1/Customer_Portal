package com.my1stle.customer.portal.service.stripe;

import org.baeldung.persistence.model.User;


public interface PaymentService {
    public String createCustomer(User user);
    public void chargeCreditCard(Order order);
}
