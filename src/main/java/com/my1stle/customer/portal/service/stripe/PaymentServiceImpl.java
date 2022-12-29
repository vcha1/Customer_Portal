package com.my1stle.customer.portal.service.stripe;

import java.util.HashMap;
import java.util.Map;

import com.stripe.model.Customer;
import org.baeldung.persistence.model.User;
import org.springframework.stereotype.Service;

import com.stripe.exception.ApiConnectionException;
import com.stripe.exception.AuthenticationException;
import com.stripe.exception.CardException;
import com.stripe.exception.InvalidRequestException;
import com.stripe.exception.RateLimitException;
import com.stripe.exception.StripeException;

import com.stripe.Stripe;
import com.stripe.model.Charge;

@Service("paymentService")
public class PaymentServiceImpl implements PaymentService {

    private static final String TEST_STRIPE_SECRET_KEY = "stripe.api.key";

    public PaymentServiceImpl() {
        Stripe.apiKey = TEST_STRIPE_SECRET_KEY;
    }

    public String createCustomer(User user) {

        Map<String, Object> customerParams = new HashMap<String, Object>();
        customerParams.put("description",
                user.getFirstName() + " " + user.getLastName());
        customerParams.put("email", user.getEmail());

        String id = null;

        try {
            // Create customer
            Customer stripeCustomer = Customer.create(customerParams);
            id = stripeCustomer.getId();
            System.out.println(stripeCustomer);
        } catch (CardException e) {
            // Transaction failure
        } catch (RateLimitException e) {
            // Too many requests made to the API too quickly
        } catch (InvalidRequestException e) {
            // Invalid parameters were supplied to Stripe's API
        } catch (AuthenticationException e) {
            // Authentication with Stripe's API failed (wrong API key?)
        } catch (ApiConnectionException e) {
            // Network communication with Stripe failed
        } catch (StripeException e) {
            // Generic error
        } catch (Exception e) {
            // Something else happened unrelated to Stripe
        }

        return id;
    }

    public void chargeCreditCard(Order order){

        // Stripe requires the charge amount to be in cents
        int chargeAmountCents = (int) order.getChargeAmountDollars() * 100;

        Map<String, Object> chargeParams = new HashMap<String, Object>();
        chargeParams.put("amount", chargeAmountCents);
        chargeParams.put("currency", "usd");
        //chargeParams.put("description", "Monthly Charges");
        //chargeParams.put("customer", user.getStripeCustomerId());
        chargeParams.put("customer", order.getUserStripeId());

        try {
            // Submit charge to credit card
            Charge charge = Charge.create(chargeParams);
            System.out.println(charge);
        } catch (CardException e) {
            // Transaction was declined
            System.out.println("Status is: " + e.getCode());
            System.out.println("Message is: " + e.getMessage());
        } catch (RateLimitException e) {
            // Too many requests made to the API too quickly
        } catch (InvalidRequestException e) {
            // Invalid parameters were supplied to Stripe's API
        } catch (AuthenticationException e) {
            // Authentication with Stripe's API failed (wrong API key?)
        } catch (ApiConnectionException e) {
            // Network communication with Stripe failed
        } catch (StripeException e) {
            // Generic error
        } catch (Exception e) {
            // Something else happened unrelated to Stripe
        }
    }
}