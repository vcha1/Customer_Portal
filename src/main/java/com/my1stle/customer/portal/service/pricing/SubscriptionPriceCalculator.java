package com.my1stle.customer.portal.service.pricing;

import com.my1stle.customer.portal.service.model.Installation;
import com.my1stle.customer.portal.service.model.PaymentMethod;
import com.my1stle.customer.portal.service.model.Product;
import com.my1stle.customer.portal.service.model.Subscription;
import com.my1stle.customer.portal.service.odoo.OdooInstallationData;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.function.BiFunction;

@Service
public class SubscriptionPriceCalculator implements BiFunction<Subscription, PaymentMethod, BigDecimal> {

    private FeeCalculator feeCalculator;

    public SubscriptionPriceCalculator(FeeCalculator feeCalculator) {
        this.feeCalculator = feeCalculator;
    }

    /*
    @Override
    public BigDecimal apply(Subscription subscription, PaymentMethod paymentMethod) {

        Product product = subscription.getProduct();
        Installation installation = subscription.getInstallation();


        return subscription.getPrice()
                .add(feeCalculator.calculate(product, installation, paymentMethod))
                .setScale(2, RoundingMode.DOWN);
    }
    */
    @Override
    public BigDecimal apply(Subscription subscription, PaymentMethod paymentMethod) {

        Product product = subscription.getProduct();
        OdooInstallationData odooInstallationData = subscription.getOdooInstallationData();


        return subscription.getPrice()
                .add(feeCalculator.calculate(product, odooInstallationData, paymentMethod))
                .setScale(2, RoundingMode.DOWN);
    }
}