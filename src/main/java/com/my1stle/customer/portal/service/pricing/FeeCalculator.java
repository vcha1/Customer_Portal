package com.my1stle.customer.portal.service.pricing;

import com.my1stle.customer.portal.service.model.Installation;
import com.my1stle.customer.portal.service.model.PaymentMethod;
import com.my1stle.customer.portal.service.model.Product;
import com.my1stle.customer.portal.service.model.ServiceRequest;
import com.my1stle.customer.portal.service.model.Subscription;
import com.my1stle.customer.portal.service.odoo.OdooInstallationData;

import java.math.BigDecimal;

public interface FeeCalculator {

    //BigDecimal calculate(Product product, Installation installation, PaymentMethod paymentMethod);

    BigDecimal calculate(Product product, OdooInstallationData odooInstallationData, PaymentMethod paymentMethod);

    BigDecimal calculate(ServiceRequest serviceRequest, PaymentMethod paymentMethod);

    BigDecimal calculate(Subscription subscription, PaymentMethod paymentMethod);

}