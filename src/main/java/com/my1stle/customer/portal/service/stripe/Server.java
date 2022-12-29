package com.my1stle.customer.portal.service.stripe;

import static spark.Spark.get;
import static spark.Spark.post;
import static spark.Spark.staticFiles;
import static spark.Spark.port;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.stripe.Stripe;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;

import java.nio.file.Paths;

public class Server {
    private static Gson gson = new Gson();

    static class CreatePayment {
        @SerializedName("items")
        Object[] items;

        public Object[] getItems() {
            return items;
        }
    }

    static class CreatePaymentResponse {
        private String clientSecret;
        public CreatePaymentResponse(String clientSecret) {
            this.clientSecret = clientSecret;
        }
    }

    static int calculateOrderAmount(Object[] items) {
        // Replace this constant with a calculation of the order's amount
        // Calculate the order total on the server to prevent
        // people from directly manipulating the amount on the client
        return 1400;
    }

    public static void test(){
        System.out.println("Here Outside 1");
        //port(5000);

        staticFiles.externalLocation(Paths.get("src/main/resources/templates/serviceRequest/checkout").toAbsolutePath().toString());
        System.out.println("Here Outside 2");
        // This is your test secret API key.
        Stripe.apiKey = "sk_test_51MEH73FUBi5jGHMWDKDFJCQjFUV7Kyf3WI23O5eORb9ZUrxQbuO14JtCkh4zCdUgeZUvGC09xkUHZz29kSYwlpQ100oT7lZRlC";

        post("/create-payment-intent", (request, response) -> {
            System.out.println("Here Inside 1");
            response.type("application/json");

            CreatePayment postBody = gson.fromJson(request.body(), CreatePayment.class);
            PaymentIntentCreateParams createParams = new PaymentIntentCreateParams.Builder()
                    .setCurrency("usd")
                    .setAmount(new Long(calculateOrderAmount(postBody.getItems())))
                    .build();
            // Create a PaymentIntent with the order amount and currency
            PaymentIntent intent = PaymentIntent.create(createParams);
            System.out.println("Here Inside 2");
            CreatePaymentResponse paymentResponse = new CreatePaymentResponse(intent.getClientSecret());
            return gson.toJson(paymentResponse);
        });

        System.out.println("End");
    }

}



