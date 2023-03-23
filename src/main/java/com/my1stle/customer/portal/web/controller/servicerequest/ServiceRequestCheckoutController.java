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
import com.my1stle.customer.portal.web.exception.ResourceNotFoundException;

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

        PaymentMethod paymentMethod = this.paymentMethodService.getByName(PaymentMethods.PAYPAL)
                .orElseThrow(() -> new ResourceClosedException(String.format("%s payment method not found!", PaymentMethods.PAYPAL)));

        this.serviceRequest = request;
        addPricing(model, request, paymentMethod);


        return "serviceRequest/checkout/payment.html";

    }

    @RequestMapping(value = "/confirm/{orderId}")
    public String confirmOrderAction(@PathVariable String orderId, Model model) throws IOException {

        PaymentMethod paymentMethod = this.paymentMethodService.getByName(PaymentMethods.PAYPAL)
                .orElseThrow(() -> new ResourceClosedException(String.format("%s payment method not found!", PaymentMethods.PAYPAL)));

        ServiceRequest serviceRequest = serviceRequestPaymentProcessor.confirmOrder(orderId);

        addPricing(model, serviceRequest, paymentMethod);

        return "serviceRequest/checkout/confirm.html";
    }

    @RequestMapping(value = "/finalize/{orderId}")
    public String processPaymentAction(@PathVariable String orderId, Model model) throws IOException {

        ServiceRequest serviceRequest = serviceRequestPaymentProcessor.processServiceRequestPayment(orderId);
        if (null == serviceRequest || null == serviceRequest.getPaymentDetail())
            throw new ResourceNotFoundException("Request not found!");

        PaymentMethod paymentMethod = this.paymentMethodService.getByName(PaymentMethods.PAYPAL)
                .orElseThrow(() -> new ResourceClosedException(String.format("%s payment method not found!", PaymentMethods.PAYPAL)));

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

}
