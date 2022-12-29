package com.my1stle.customer.portal.service.model;

import com.dev1stle.scheduling.system.v1.model.Skill;
import com.my1stle.customer.portal.service.pricing.PaymentSchedule;
import com.my1stle.customer.portal.service.pricing.PricingType;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.Set;

public interface Product {

    Long getId();

    Boolean isActive();

    String getName();

    String getDescription();

    Integer getDuration();

    boolean getIsSchedulable();

    boolean getIsSubscriptionBased();

    BigDecimal getPricePerUnit();

    BigDecimal getRequiredDepositPercentage();

    Set<ProductDiscount> getDiscounts();

    Set<Skill> getSkills();

    String getProductImageUrl();

    PricingType getPricingType();

    PaymentSchedule getPaymentSchedule();

    Optional<ProductAgreementDocument> getProductAgreementDocument();

}