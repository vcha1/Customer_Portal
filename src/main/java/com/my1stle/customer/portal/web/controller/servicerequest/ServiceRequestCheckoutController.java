package com.my1stle.customer.portal.web.controller.servicerequest;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.my1stle.customer.portal.service.model.PaymentMethod;
import com.my1stle.customer.portal.service.model.ServiceRequest;
import com.my1stle.customer.portal.service.payment.PaymentMethodService;
import com.my1stle.customer.portal.service.payment.PaymentMethods;
import com.my1stle.customer.portal.service.paypal.ServiceRequestPaymentProcessor;
import com.my1stle.customer.portal.service.pricing.ServiceRequestPricingService;
import com.my1stle.customer.portal.service.servicerequest.ServiceRequestService;
//import com.my1stle.customer.portal.service.stripe.Server;
import com.my1stle.customer.portal.service.stripe.StripeClass;
import com.my1stle.customer.portal.web.controller.StripeController;
import com.my1stle.customer.portal.web.exception.ResourceNotFoundException;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import org.baeldung.persistence.model.User;
import org.hibernate.ResourceClosedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

//import static com.my1stle.customer.portal.service.stripe.Server.calculateOrderAmount;
//import static com.my1stle.customer.portal.service.stripe.Server.test;
import static spark.Spark.post;
import static spark.Spark.staticFiles;

@RequestMapping(value = "/service-request/checkout")
@Controller
public class ServiceRequestCheckoutController {

    private String payPalClientId;
    private ServiceRequestService serviceRequestService;
    private ServiceRequestPaymentProcessor serviceRequestPaymentProcessor;
    private ServiceRequestPricingService serviceRequestPricingService;
    private PaymentMethodService paymentMethodService;
    private Long serviceId;
    private BigDecimal totalPrice;
    private String paymentIntentId;

    public ServiceRequest serviceRequest;

    @Autowired
    public ServiceRequestCheckoutController(
            @Value("${paypal.client.id}") String payPalClientId,
            ServiceRequestService serviceRequestService,
            ServiceRequestPaymentProcessor serviceRequestPaymentProcessor,
            ServiceRequestPricingService serviceRequestPricingService,
            PaymentMethodService paymentMethodService) {
        this.payPalClientId = payPalClientId;
        this.serviceRequestService = serviceRequestService;
        this.serviceRequestPaymentProcessor = serviceRequestPaymentProcessor;
        this.serviceRequestPricingService = serviceRequestPricingService;
        this.paymentMethodService = paymentMethodService;
    }

    @RequestMapping(value = "/payment/{serviceRequestId}", method = RequestMethod.GET)
    public String selectPaymentAction(
            Model model,
            @PathVariable Long serviceRequestId) {

        ServiceRequest request = serviceRequestService.getById(serviceRequestId);
        this.serviceId = serviceRequestId;
        if (null == request || null != request.getPaymentDetail())
            throw new ResourceNotFoundException("Request not found!");

        PaymentMethod paymentMethod = this.paymentMethodService.getByName(PaymentMethods.STRIPE)
                .orElseThrow(() -> new ResourceClosedException(String.format("%s payment method not found!", PaymentMethods.STRIPE)));

        this.serviceRequest = request;
        addPricing(model, request, paymentMethod);


        //return "serviceRequest/checkout/payment.html";
        return "serviceRequest/checkout/stripeclient.html";

    }

    @RequestMapping(value = "/confirm/{orderId}")
    public String confirmOrderAction(@PathVariable String orderId, Model model) throws IOException {

        //PaymentMethod paymentMethod = this.paymentMethodService.getByName(PaymentMethods.PAYPAL)
        //        .orElseThrow(() -> new ResourceClosedException(String.format("%s payment method not found!", PaymentMethods.PAYPAL)));
        PaymentMethod paymentMethod = this.paymentMethodService.getByName(PaymentMethods.STRIPE)
                .orElseThrow(() -> new ResourceClosedException(String.format("%s payment method not found!", PaymentMethods.STRIPE)));

        ServiceRequest serviceRequest = serviceRequestPaymentProcessor.confirmOrder(orderId);

        addPricing(model, serviceRequest, paymentMethod);

        return "serviceRequest/checkout/confirm.html";
    }

    @RequestMapping(value = "/finalize/{orderId}")
    public String processPaymentAction(@PathVariable String orderId, Model model) throws IOException {

        ServiceRequest serviceRequest = serviceRequestPaymentProcessor.processServiceRequestPayment(orderId);
        if (null == serviceRequest || null == serviceRequest.getPaymentDetail())
            throw new ResourceNotFoundException("Request not found!");

        //PaymentMethod paymentMethod = this.paymentMethodService.getByName(PaymentMethods.PAYPAL)
        //        .orElseThrow(() -> new ResourceClosedException(String.format("%s payment method not found!", PaymentMethods.PAYPAL)));
        PaymentMethod paymentMethod = this.paymentMethodService.getByName(PaymentMethods.STRIPE)
                .orElseThrow(() -> new ResourceClosedException(String.format("%s payment method not found!", PaymentMethods.STRIPE)));

        addPricing(model, serviceRequest, paymentMethod);
        model.addAttribute("order", serviceRequestService.generateOrder(serviceRequest));

        return "serviceRequest/checkout/final.html";

    }

    private void addPricing(Model model, ServiceRequest serviceRequest, PaymentMethod paymentMethod) {

        final BigDecimal requiredDepositAmount = this.serviceRequestPricingService.getRequiredDepositAmount(serviceRequest);
        final BigDecimal totalPrice = this.serviceRequestPricingService.getTotalPrice(serviceRequest, paymentMethod);
        final BigDecimal subTotalAmount = serviceRequestPricingService.getSubTotalAmount(serviceRequest);
        final BigDecimal discountAmount = serviceRequestPricingService.getDiscountAmount(serviceRequest);
        final BigDecimal convenienceFee = serviceRequestPricingService.getConvenienceFee(serviceRequest, paymentMethod);
        final BigDecimal amountDue = requiredDepositAmount.compareTo(BigDecimal.ZERO) > 0 ? requiredDepositAmount.add(convenienceFee) : totalPrice;
        this.totalPrice = totalPrice;
        model.addAttribute("serviceRequest", serviceRequest);
        model.addAttribute("subTotal", subTotalAmount);
        model.addAttribute("totalDiscount", discountAmount);
        model.addAttribute("convenience_fee", convenienceFee);
        model.addAttribute("totalPrice", totalPrice);
        model.addAttribute("requiredDepositAmount", requiredDepositAmount);
        model.addAttribute("amountDue", amountDue);
        model.addAttribute("payPalClientId", this.payPalClientId);

    }





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

    @PostMapping(value = "/payment/stripe")
    public String createPaymentIntent(Model model, @AuthenticationPrincipal User user) throws StripeException {
        Stripe.apiKey = "sk_test_51MEH73FUBi5jGHMWDKDFJCQjFUV7Kyf3WI23O5eORb9ZUrxQbuO14JtCkh4zCdUgeZUvGC09xkUHZz29kSYwlpQ100oT7lZRlC";
        //response.setContentType("application/json");
        //CreatePayment postBody = gson.fromJson(request.body(), CreatePayment.class);

        Double y = Double.valueOf(this.totalPrice.toString()) * 100;
        long x = y.longValue();

        // Create the customer ID to be used for stripe (Sending customer emails)
        Map<String, Object> customerParams = new HashMap<String, Object>();
        customerParams.put("name",
                user.getFirstName() + " " + user.getLastName());
        customerParams.put("email", user.getEmail());
        Customer stripeCustomer = Customer.create(customerParams);
        String customerId = stripeCustomer.getId();

        //Create stripe payment intent
        PaymentIntentCreateParams createParams = new
                PaymentIntentCreateParams.Builder()
                .setAmount(x)
                .setCurrency("usd")
                .setCustomer(customerId)
                .setAutomaticPaymentMethods(
                        PaymentIntentCreateParams.AutomaticPaymentMethods
                                .builder()
                                .setEnabled(true)
                                .build()
                )
                .build();

        PaymentIntent intent = PaymentIntent.create(createParams);
        this.paymentIntentId = intent.getId();
        CreatePaymentResponse paymentResponse = new CreatePaymentResponse(intent.getClientSecret());


        String test = gson.toJson(paymentResponse);
        stripeClass.setStripeInfo(test);


        //return "redirect:/payment/stripe";
        return String.format("redirect:/service-request/checkout/payment/stripe/%s", this.serviceId);
    }

    @GetMapping(value = "/payment/stripe/{serviceRequestIds}")
    public String getPaymentHtml(Model model,
                          @PathVariable Long serviceRequestIds) {

        model.addAttribute("stripeClass", stripeClass);

        ServiceRequest request = serviceRequestService.getById(serviceRequestIds);
        this.serviceId = serviceRequestIds;
        if (null == request || null != request.getPaymentDetail())
            throw new ResourceNotFoundException("Request not found!");

        PaymentMethod paymentMethod = this.paymentMethodService.getByName(PaymentMethods.STRIPE)
                .orElseThrow(() -> new ResourceClosedException(String.format("%s payment method not found!", PaymentMethods.STRIPE)));

        addPricing(model, request, paymentMethod);

        model.addAttribute("paymentIntentId", this.paymentIntentId);

        return "serviceRequest/checkout/stripe.html";
    }


    @RequestMapping(value = "/finalize/stripe/{paymentIntentId}")
    public String processPaymentStripe(@PathVariable String paymentIntentId, Model model) throws IOException {

        ServiceRequest serviceRequest1 = serviceRequestPaymentProcessor.confirmOrderStripe(paymentIntentId, this.serviceRequest);
        ServiceRequest serviceRequest = serviceRequestPaymentProcessor.processServiceRequestPaymentStripe(paymentIntentId, this.serviceRequest);
        if (null == serviceRequest || null != serviceRequest.getPaymentDetail())
            throw new ResourceNotFoundException("Request not found!");

        //PaymentMethod paymentMethod = this.paymentMethodService.getByName(PaymentMethods.PAYPAL)
        //        .orElseThrow(() -> new ResourceClosedException(String.format("%s payment method not found!", PaymentMethods.PAYPAL)));
        PaymentMethod paymentMethod = this.paymentMethodService.getByName(PaymentMethods.STRIPE)
                .orElseThrow(() -> new ResourceClosedException(String.format("%s payment method not found!", PaymentMethods.STRIPE)));

        addPricing(model, serviceRequest, paymentMethod);
        model.addAttribute("order", serviceRequestService.generateOrder(serviceRequest));

        return "serviceRequest/checkout/final.html";

    }

}
