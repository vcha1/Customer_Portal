package com.my1stle.customer.portal.service.pricing;

import com.my1stle.customer.portal.service.model.Installation;
import com.my1stle.customer.portal.service.model.PaymentMethod;
import com.my1stle.customer.portal.service.model.Product;
import com.my1stle.customer.portal.service.model.ServiceRequest;
import com.my1stle.customer.portal.service.model.Subscription;
import com.my1stle.customer.portal.service.odoo.OdooInstallationData;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

/*
@Service
public class ConvenienceFeeCalculator implements FeeCalculator {

    @Override
    public BigDecimal calculate(Product product, Installation installation, PaymentMethod paymentMethod) {

        BigDecimal totalInitialPrice = product.getPricingType().calculation().apply(product, installation);

        return paymentMethod.getConvenienceFeeFixed()
                .add(totalInitialPrice.multiply(paymentMethod.getConvenienceFeePercentage()))
                .setScale(2, RoundingMode.DOWN);

    }

    @Override
    public BigDecimal calculate(ServiceRequest serviceRequest, PaymentMethod paymentMethod) {

        Product product = serviceRequest.getProduct();
        Installation installation = serviceRequest.getInstallation();

        BigDecimal totalInitialPrice = product.getPricingType()
                .calculation()
                .apply(product, installation)
                .multiply(BigDecimal.valueOf(serviceRequest.getQuantity()));


        return paymentMethod.getConvenienceFeeFixed()
                .add(totalInitialPrice.multiply(paymentMethod.getConvenienceFeePercentage()))
                .setScale(2, RoundingMode.DOWN);

    }

    @Override
    public BigDecimal calculate(Subscription subscription, PaymentMethod paymentMethod) {
        return this.calculate(subscription.getProduct(), subscription.getInstallation(), paymentMethod);
    }

}
*/

@Service
public class ConvenienceFeeCalculator implements FeeCalculator {

    @Override
    public BigDecimal calculate(Product product, OdooInstallationData odooInstallationData, PaymentMethod paymentMethod) {

        BigDecimal totalInitialPrice = product.getPricingType().calculation().apply(product, odooInstallationData);

        return paymentMethod.getConvenienceFeeFixed()
                .add(totalInitialPrice.multiply(paymentMethod.getConvenienceFeePercentage()))
                .setScale(2, RoundingMode.DOWN);

    }

    @Override
    public BigDecimal calculate(ServiceRequest serviceRequest, PaymentMethod paymentMethod) {

        Product product = serviceRequest.getProduct();
        //Installation installation = serviceRequest.getInstallation();
        OdooInstallationData odooInstallationData = serviceRequest.getOdooInstallationData();

        BigDecimal totalInitialPrice = product.getPricingType()
                .calculation()
                .apply(product, odooInstallationData)
                .multiply(BigDecimal.valueOf(serviceRequest.getQuantity()));


        return paymentMethod.getConvenienceFeeFixed()
                .add(totalInitialPrice.multiply(paymentMethod.getConvenienceFeePercentage()))
                .setScale(2, RoundingMode.DOWN);

    }

    @Override
    public BigDecimal calculate(Subscription subscription, PaymentMethod paymentMethod) {
        //return this.calculate(subscription.getProduct(), subscription.getInstallation(), paymentMethod);
        return this.calculate(subscription.getProduct(), subscription.getOdooInstallationData(), paymentMethod);
    }

}