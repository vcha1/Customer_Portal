package com.my1stle.customer.portal.web.controller;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.annotations.SerializedName;
import com.my1stle.customer.portal.service.stripe.StripeClass;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.BufferedReader;
import java.io.IOException;

@Controller
//@RestController
//@RequestMapping(value = "/installation/18215")
public class StripeController {

    //below code works
    /*
    private static Gson gson = new Gson();
    public StripeClass stripeClass = new StripeClass();
    static class CreatePayment {
        @SerializedName("items")
        Object[] items;

        public Object[] getItems() {
            return items;
        }
    }

    static int calculateOrderAmount(Object[] items) {
        // Replace this constant with a calculation of the order's amount
        // Calculate the order total on the server to prevent
        // people from directly manipulating the amount on the client
        return 1400;

    }

    static class CreatePaymentResponse {
        private String clientSecret;
        public CreatePaymentResponse(String clientSecret) {
            this.clientSecret = clientSecret;
        }
    }
     */






    /*
    @PostMapping("/create-payment-intent")
    public String StripeTest(HttpServletRequest request, HttpServletResponse response) throws StripeException {
        System.out.println("Stripe Controller Begins");
        Gson gson = new Gson();
        response.setContentType("application/json");
        Stripe.apiKey = "sk_test_51MEH73FUBi5jGHMWDKDFJCQjFUV7Kyf3WI23O5eORb9ZUrxQbuO14JtCkh4zCdUgeZUvGC09xkUHZz29kSYwlpQ100oT7lZRlC";

        try {
            System.out.println("Stripe Controller");
            StringBuilder buffer = new StringBuilder();
            BufferedReader reader = request.getReader();
            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }
            String dataBody = buffer.toString();

            CreatePayment postBody = gson.fromJson(dataBody,
                    CreatePayment.class);

            PaymentIntentCreateParams createParams = new PaymentIntentCreateParams.Builder()
                    .setCurrency("USD")
                    .setAmount(new Long(calculateOrderAmount(postBody.getItems())))
                    .build();
            // Create a PaymentIntent with the order amount and currency
            PaymentIntent intent = PaymentIntent.create(createParams);
            // Send publishable key and PaymentIntent  details to client
            return gson.toJson(new CreatePaymentResponse(intent.getClientSecret()));
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            return "";
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }

    }
    */
    /*
    @PostMapping("/create-payment-intent")
    public CreatePaymentResponse createPaymentIntent(@RequestBody @Valid CreatePayment createPayment)throws StripeException {
        System.out.println("Second");
        long x = 10000;
        PaymentIntentCreateParams createParams = new
                PaymentIntentCreateParams.Builder()
                .setCurrency("usd")
                .setAmount(x)
                .build();

        PaymentIntent intent = PaymentIntent.create(createParams);
        return new CreatePaymentResponse(intent.getClientSecret());
    }*/









    ///below code works to get stripe to work
    /*
    @PostMapping(value = "/service-request/checkout/payment/stripe")
    public String createPaymentIntent(Model model) throws StripeException {
        Stripe.apiKey = "sk_test_51MEH73FUBi5jGHMWDKDFJCQjFUV7Kyf3WI23O5eORb9ZUrxQbuO14JtCkh4zCdUgeZUvGC09xkUHZz29kSYwlpQ100oT7lZRlC";
        //System.out.println("Second");
        //response.setContentType("application/json");
        //CreatePayment postBody = gson.fromJson(request.body(), CreatePayment.class);
        long x = 25000;
        PaymentIntentCreateParams createParams = new
                PaymentIntentCreateParams.Builder()
                .setAmount(x)
                .setCurrency("usd")
                //.setPaymentMethod("card")
                .setAutomaticPaymentMethods(
                        PaymentIntentCreateParams.AutomaticPaymentMethods
                                .builder()
                                .setEnabled(true)
                                .build()
                )
          .build();

        PaymentIntent intent = PaymentIntent.create(createParams);
        CreatePaymentResponse paymentResponse = new CreatePaymentResponse(intent.getClientSecret());


        String test = gson.toJson(paymentResponse);
        stripeClass.setStripeInfo(test);

        //System.out.println(paymentResponse);
        System.out.println(gson.toJson(paymentResponse));

//        return gson.toJson(paymentResponse);
        return "redirect:/service-request/checkout/payment/stripe";
    }

    //@RequestMapping(value = "/create-payment-intent", method = RequestMethod.GET)
    @GetMapping(value = "/service-request/checkout/payment/stripe")
    public String getTest(Model model) {
        //System.out.println("Get Second");
        //System.out.println(stripeClass.getStripeInfo());
        model.addAttribute("stripeClass", stripeClass);
        return "serviceRequest/checkout/stripetest2.html";
    }
    */



}