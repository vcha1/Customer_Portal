package com.my1stle.customer.portal.service.payment;

import com.my1stle.customer.portal.service.model.PaymentMethod;

import java.util.List;
import java.util.Optional;

public interface PaymentMethodService {

    List<PaymentMethod> getAll();

    Optional<PaymentMethod> getById(Long id);

    Optional<PaymentMethod> getByName(String name);

}