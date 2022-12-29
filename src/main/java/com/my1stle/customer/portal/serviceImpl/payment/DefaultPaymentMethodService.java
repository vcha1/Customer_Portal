package com.my1stle.customer.portal.serviceImpl.payment;

import com.my1stle.customer.portal.persistence.repository.PaymentMethodRepository;
import com.my1stle.customer.portal.service.model.PaymentMethod;
import com.my1stle.customer.portal.service.payment.PaymentMethodService;
import com.my1stle.customer.portal.serviceImpl.model.PaymentMethodProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DefaultPaymentMethodService implements PaymentMethodService {

    private PaymentMethodRepository paymentMethodRepository;

    @Autowired
    public DefaultPaymentMethodService(PaymentMethodRepository paymentMethodRepository) {
        this.paymentMethodRepository = paymentMethodRepository;
    }

    @Override
    public List<PaymentMethod> getAll() {
        return this.paymentMethodRepository.findAll().stream().map(PaymentMethodProxy::from).collect(Collectors.toList());
    }

    @Override
    public Optional<PaymentMethod> getById(Long id) {
        return this.paymentMethodRepository.findById(id).map(PaymentMethodProxy::from);
    }

    @Override
    public Optional<PaymentMethod> getByName(String name) {
        return this.paymentMethodRepository.findByName(name).map(PaymentMethodProxy::from);
    }

}