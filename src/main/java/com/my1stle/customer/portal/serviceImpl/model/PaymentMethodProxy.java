package com.my1stle.customer.portal.serviceImpl.model;

import com.my1stle.customer.portal.persistence.model.PaymentMethodEntity;
import com.my1stle.customer.portal.service.model.PaymentMethod;
import com.my1stle.customer.portal.serviceImpl.payment.DefaultPaymentMethodService;

import java.math.BigDecimal;

public class PaymentMethodProxy implements PaymentMethod {

    private Long id;
    private String name;
    private String description;
    private BigDecimal fixedFee;
    private BigDecimal percentageFee;

    private PaymentMethodProxy() {

    }

    public static PaymentMethodProxy from(PaymentMethodEntity entity) {

        PaymentMethodProxy proxy = new PaymentMethodProxy();
        proxy.id = entity.getId();
        proxy.name = entity.getName();
        proxy.description = entity.getDescription();
        proxy.fixedFee = entity.getConvenienceCostFixed();
        proxy.percentageFee = entity.getConvenienceCostPercentage();

        return proxy;

    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public BigDecimal getConvenienceFeeFixed() {
        return fixedFee;
    }

    @Override
    public BigDecimal getConvenienceFeePercentage() {
        return percentageFee;
    }
}
