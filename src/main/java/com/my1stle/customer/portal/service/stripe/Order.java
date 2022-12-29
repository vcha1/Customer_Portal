package com.my1stle.customer.portal.service.stripe;

import com.my1stle.customer.portal.service.stripe.PaymentServiceImpl;
import org.baeldung.persistence.model.User;

public class Order {
    private long chargedAmountDollars;
    private String currency;
    private String userStripeId;

    Order (long chargedAmountDollars, String currency, User user) {
        this.chargedAmountDollars = chargedAmountDollars;
        this.currency = currency;

        PaymentServiceImpl stripeService = new PaymentServiceImpl();

        if (user.getStripeId() == null) {
            this.userStripeId = stripeService.createCustomer(user);
            user.setStripeId(this.userStripeId);
        }else if (user.getStripeId() != null) {
            this.userStripeId = user.getStripeId();
        }

    }

    long getChargeAmountDollars(){
        return chargedAmountDollars;
    }

    String getCurrency(){
        return currency;
    }

    String getUserStripeId(){
        return userStripeId;
    }

}
