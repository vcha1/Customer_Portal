package com.my1stle.customer.portal.service.pricing;

import com.my1stle.customer.portal.service.model.PaymentMethod;
import com.my1stle.customer.portal.service.model.ServiceRequest;

import java.math.BigDecimal;

public interface ServiceRequestPricingService {

    BigDecimal getTotalPrice(ServiceRequest serviceRequest, PaymentMethod paymentMethod);

    BigDecimal getSubTotalAmount(ServiceRequest serviceRequest);

    BigDecimal getRequiredDepositAmount(ServiceRequest serviceRequest);

    BigDecimal getDiscountAmount(ServiceRequest serviceRequest);

    BigDecimal getConvenienceFee(ServiceRequest serviceRequest, PaymentMethod paymentMethod);

}