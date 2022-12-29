package com.my1stle.customer.portal.serviceImpl.paypal;

import com.braintreepayments.http.HttpResponse;
import com.my1stle.customer.portal.service.paypal.PayPalClient;
import com.paypal.core.PayPalEnvironment;
import com.paypal.core.PayPalHttpClient;
import com.paypal.orders.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class DefaultPayPalClient implements PayPalClient {

    private final static String ENVIRONMENT_PRODUCTION = "production" ;

    private PayPalHttpClient client  ;

    public DefaultPayPalClient(
            @Value("${paypal.environment}")String environmentName,
            @Value("${paypal.client.id}")String clientId,
            @Value("${paypal.client.secret}")String secret) {

        /**
         * Set up the PayPal Java SDK environment with PayPal access credentials.
         * This sample uses SandboxEnvironment. In production, use ProductionEnvironment.
         */
        PayPalEnvironment environment;
        if(environmentName.equals(ENVIRONMENT_PRODUCTION))
                environment = new PayPalEnvironment.Live(clientId,secret) ;
            else
                environment = new PayPalEnvironment.Sandbox(clientId,secret) ;

            this.client = new PayPalHttpClient(environment);
    }

    /**
     * Method to get client object
     *
     * @return PayPalHttpClient client
     */
    private PayPalHttpClient client() {
        return this.client;
    }

    /**
     * Method to perform sample GET on an order
     *
     * @throws IOException Exceptions from the API, if any
     */
    @Override
    public Order getOrder(String orderId) throws IOException {
        OrdersGetRequest request = new OrdersGetRequest(orderId);
        HttpResponse<Order> response = client().execute(request);

        return response.result();
    }

    /**
     * Method to capture order after creation. Pass a valid, approved order ID
     * an argument to this method.
     *
     * @param orderId Authorization ID from authorizeOrder response
     * @param debug   true = print response data
     * @return HttpResponse<Capture> response received from API
     * @throws IOException Exceptions from API if any
     */
    @Override
    public HttpResponse<Order> captureOrder(String orderId, boolean debug) throws IOException {
        OrdersCaptureRequest request = new OrdersCaptureRequest(orderId);
        OrderRequest orderRequest = new OrderRequest();
        request.requestBody(orderRequest);
        //3. Call PayPal to capture an order
        HttpResponse<Order> response = client().execute(request);
        //4. Save the capture ID to your database. Implement logic to save capture to your database for future reference.
        if (debug) {
            System.out.println("Status Code: " + response.statusCode());
            System.out.println("Status: " + response.result().status());
            System.out.println("Order ID: " + response.result().id());
            System.out.println("Links: ");
            for (LinkDescription link : response.result().links()) {
                System.out.println("\t" + link.rel() + ": " + link.href());
            }
            System.out.println("Capture ids:");
            for (PurchaseUnit purchaseUnit : response.result().purchaseUnits()) {
                for (Capture capture : purchaseUnit.payments().captures()) {
                    System.out.println("\t" + capture.id());
                }
            }
            System.out.println("Buyer: ");
            Customer buyer = response.result().payer();
            System.out.println("\tEmail Address: " + buyer.emailAddress());
            System.out.println("\tName: " + buyer.name().fullName());
           // System.out.println("\tPhone Number: " + buyer.phone().countryCode() + buyer.phone().nationalNumber());
        }
        return response;
    }


}
