package com.my1stle.customer.portal.service.util;

import com.my1stle.customer.portal.service.model.Product;
import com.my1stle.customer.portal.service.pricing.PaymentSchedule;

import java.util.Collections;
import java.util.Set;

public class ProductUtil {

    private ProductUtil() {
        throw new AssertionError("Cannot be instantiated!");
    }

    private static final Set<PaymentSchedule> SUBSCRIPTION_PAYMENT_SCHEDULES = Collections.singleton(PaymentSchedule.YEARLY);

    public static boolean isSubscriptionBasedProduct(Product product) {
        return SUBSCRIPTION_PAYMENT_SCHEDULES.contains(product.getPaymentSchedule());
    }

}
