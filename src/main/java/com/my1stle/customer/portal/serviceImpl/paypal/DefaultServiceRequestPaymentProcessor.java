package com.my1stle.customer.portal.serviceImpl.paypal;

import com.braintreepayments.http.HttpResponse;
import com.my1stle.customer.portal.service.model.Installation;
import com.my1stle.customer.portal.service.model.PaymentDetail;
import com.my1stle.customer.portal.service.model.PaymentMethod;
import com.my1stle.customer.portal.service.model.Product;
import com.my1stle.customer.portal.service.model.ServiceRequest;
import com.my1stle.customer.portal.service.odoo.OdooInstallationData;
import com.my1stle.customer.portal.service.payment.PaymentMethodService;
import com.my1stle.customer.portal.service.payment.PaymentMethods;
import com.my1stle.customer.portal.service.paypal.PayPalClient;
import com.my1stle.customer.portal.service.paypal.PaypalOrderStatus;
import com.my1stle.customer.portal.service.paypal.ServiceRequestPaymentProcessor;
import com.my1stle.customer.portal.service.pricing.ServiceRequestPricingService;
import com.my1stle.customer.portal.service.product.notification.ProductAndCustomerInformation;
import com.my1stle.customer.portal.service.product.notification.ProductSoldNotificationService;
import com.my1stle.customer.portal.service.servicerequest.ServiceRequestErrorNotificationService;
import com.my1stle.customer.portal.service.servicerequest.ServiceRequestScheduler;
import com.my1stle.customer.portal.service.servicerequest.ServiceRequestService;
import com.my1stle.customer.portal.serviceImpl.PaymentDetailService;
import com.my1stle.customer.portal.web.exception.InternalServerErrorException;
import com.paypal.orders.Order;
import com.paypal.orders.PurchaseUnit;
import org.hibernate.ResourceClosedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

@Service
public class DefaultServiceRequestPaymentProcessor implements ServiceRequestPaymentProcessor {
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultServiceRequestPaymentProcessor.class);

    private PayPalClient payPalClient;
    private ServiceRequestService serviceRequestService;
    private PaymentDetailService paymentDetailService;
    private ServiceRequestScheduler truckRollServiceRequestScheduler;
    private ServiceRequestScheduler salesforceServiceCaseServiceRequestScheduler;
    private final ServiceRequestErrorNotificationService serviceRequestErrorNotificationService;
    private ServiceRequestPricingService serviceRequestPricingService;
    private PaymentMethodService paymentMethodService;
    private final ProductSoldNotificationService productSoldNotificationService;

    @Autowired
    public DefaultServiceRequestPaymentProcessor(
            ServiceRequestService serviceRequestService,
            PayPalClient payPalClient,
            PaymentDetailService paymentDetailService,
            @Qualifier("truckRollServiceRequestScheduler") ServiceRequestScheduler truckRollServiceRequestScheduler,
            @Qualifier("salesforceServiceCaseServiceRequestScheduler") ServiceRequestScheduler salesforceServiceCaseServiceRequestScheduler,
            ServiceRequestErrorNotificationService serviceRequestErrorNotificationService,
            ServiceRequestPricingService serviceRequestPricingService,
            PaymentMethodService paymentMethodService,
            ProductSoldNotificationService productSoldNotificationService
    ) {

        this.serviceRequestService = serviceRequestService;
        this.payPalClient = payPalClient;
        this.paymentDetailService = paymentDetailService;
        this.truckRollServiceRequestScheduler = truckRollServiceRequestScheduler;
        this.salesforceServiceCaseServiceRequestScheduler = salesforceServiceCaseServiceRequestScheduler;
        this.serviceRequestErrorNotificationService = serviceRequestErrorNotificationService;
        this.serviceRequestPricingService = serviceRequestPricingService;
        this.paymentMethodService = paymentMethodService;
        this.productSoldNotificationService = productSoldNotificationService;
    }


    /**
     * 1 - Order must be valid.
     * 2 - Related Service Request must be valid.
     * 3 - Price must match.
     *
     * @param orderId String
     * @return ServiceRequest - Related ServiceRequest found.
     */
    private DefaultValidationResult validateOrderAmount(String orderId) throws IOException {

        Order order = payPalClient.getOrder(orderId);
        PurchaseUnit purchaseUnit = order.purchaseUnits().get(0);
        Long serviceRequestId = Long.valueOf(purchaseUnit.customId());
        ServiceRequest serviceRequest = this.serviceRequestService.getById(serviceRequestId);

        if (null == serviceRequest) {
            return new DefaultValidationResult(false, "Service Request not found!");
        }

        PaymentMethod paymentMethod = this.paymentMethodService.getByName(PaymentMethods.PAYPAL)
                .orElseThrow(() -> new ResourceClosedException(String.format("%s payment method not found!", PaymentMethods.PAYPAL)));

        final BigDecimal orderTotal = BigDecimal.valueOf(Double.valueOf(purchaseUnit.amount().value()));
        final BigDecimal requiredDepositAmount = serviceRequestPricingService.getRequiredDepositAmount(serviceRequest);
        final BigDecimal convenienceFee = serviceRequestPricingService.getConvenienceFee(serviceRequest, paymentMethod);
        final BigDecimal initialServiceRequestTotal = requiredDepositAmount.compareTo(BigDecimal.ZERO) > 0 ? requiredDepositAmount.add(convenienceFee) : serviceRequestPricingService.getTotalPrice(serviceRequest, paymentMethod);

        if (0 != orderTotal.compareTo(initialServiceRequestTotal))
            return new DefaultValidationResult(false, "Total amounts don't match!");

        return new DefaultValidationResult(order, serviceRequest);
    }


    @Override
    public ServiceRequest confirmOrder(String orderId) throws IOException {

        ValidationResult result = this.validateOrderAmount(orderId);

        if (!result.isValid())
            throw new RuntimeException(result.getErrorMessage());

        ServiceRequest serviceRequest = result.getServiceRequest();
        Order order = result.getOrder();

        if (null == serviceRequest)
            throw new RuntimeException("Service Request must exist");

        if (null != serviceRequest.getPaymentDetail())
            throw new RuntimeException("Payment detail must not be set!");

        if (!order.status().equals(PaypalOrderStatus.APPROVED)) {
            throw new RuntimeException(String.format("Invalid order status: %s", order.status()));
        }

        //PaymentMethod paymentMethod = this.paymentMethodService.getByName(PaymentMethods.PAYPAL)
        //        .orElseThrow(() -> new ResourceClosedException(String.format("%s payment method not found!", PaymentMethods.PAYPAL)));

        PaymentMethod paymentMethod = this.paymentMethodService.getByName(PaymentMethods.STRIPE)
                .orElseThrow(() -> new ResourceClosedException(String.format("%s payment method not found!", PaymentMethods.STRIPE)));

        final BigDecimal requiredDepositAmount = serviceRequestPricingService.getRequiredDepositAmount(serviceRequest);
        final BigDecimal convenienceFee = serviceRequestPricingService.getConvenienceFee(serviceRequest, paymentMethod);
        final BigDecimal serviceRequestTotal = serviceRequestPricingService.getSubTotalAmount(serviceRequest);
        final BigDecimal initialPaidAmount = requiredDepositAmount.compareTo(BigDecimal.ZERO) > 0 ? requiredDepositAmount : serviceRequestTotal;

        PaymentDetail detail = this.paymentDetailService.save(initialPaidAmount, convenienceFee, paymentMethod, orderId);
        this.serviceRequestService.setPaymentDetail(serviceRequest, detail);

        return serviceRequest;
    }

    @Override
    public ServiceRequest processServiceRequestPayment(String orderId) throws IOException {

        ValidationResult validationResult = this.validateOrderAmount(orderId);

        if (!validationResult.isValid())
            throw new RuntimeException("Order validation failed!");

        ServiceRequest serviceRequest = validationResult.getServiceRequest();
        Order order = validationResult.getOrder();

        if (null == serviceRequest)
            throw new RuntimeException("Service Request must exist");

        if (null == serviceRequest.getPaymentDetail())
            throw new RuntimeException("Payment detail must be set!");

        if (!order.status().equals(PaypalOrderStatus.APPROVED)) {
            throw new RuntimeException(String.format("Invalid order status: %s", order.status()));
        }

        HttpResponse<Order> payPalOrder = this.payPalClient.captureOrder(orderId, true);

        LOGGER.info("Handling post-payment-processed actions for order {}", orderId);
        this.handleSchedulingAndInformationCapture(serviceRequest, orderId);

        LOGGER.info("Sending payment-processed notification for order {}: starting", orderId);
        this.productSoldNotificationService.sendNotification(new NotificationInformation(serviceRequest));
        LOGGER.info("Sending payment-processed notification for order {}: finished", orderId);

        return serviceRequest;
    }

    private void handleSchedulingAndInformationCapture(ServiceRequest serviceRequest, String orderId) {
        InternalServerErrorException potentialException = null;
        String potentialFailedAction = null;

        if (appointmentTimeSelected(serviceRequest)) {
            try {
                this.truckRollServiceRequestScheduler.scheduleServiceRequest(serviceRequest);
            }
            catch (InternalServerErrorException e) {
                potentialException = e;
                potentialFailedAction = "scheduling the service request";
            }
        }
        else {
            try {
                this.salesforceServiceCaseServiceRequestScheduler.scheduleServiceRequest(serviceRequest);
            }
            catch (InternalServerErrorException e) {
                potentialException = e;
                potentialFailedAction = "capturing the service request information in Case";
            }
        }

        if(potentialException == null)
            return;

        String errorId = UUID.randomUUID().toString();
        LOGGER.error("Error Id : {}\nFailed {} for order {}", errorId, potentialFailedAction, orderId, potentialException);

        String errorMessage = generateSchedulingErrorMessage(errorId, orderId, potentialFailedAction, serviceRequest);
        this.serviceRequestErrorNotificationService.sendPostPaymentProcessingErrorNotification(errorMessage, potentialException);
    }

    private String generateSchedulingErrorMessage(String errorId, String orderId, String failedAction, ServiceRequest serviceRequest) {
        StringBuilder messageBuilder = new StringBuilder();
        messageBuilder.append(String.format(
                "Payment for order %s was processed, but subsequently %s failed.\nLog Id: %s",
                orderId, failedAction, errorId
        ));

        Long serviceRequestId = serviceRequest == null ? null : serviceRequest.getId();
        messageBuilder.append(String.format("\nService Request Id: %s", serviceRequestId));
        /*
        if(serviceRequest != null && serviceRequest.getInstallation() != null) {
            Installation install = serviceRequest.getInstallation();
            messageBuilder.append(String.format(
                    "\nInstallation Name: %s\nInstallation Id: %s",
                    install.getName(), install.getId()
            ));
        }
        */
        if(serviceRequest != null && serviceRequest.getOdooInstallationData() != null) {
            OdooInstallationData install = serviceRequest.getOdooInstallationData();
            messageBuilder.append(String.format(
                    "\nInstallation Name: %s\nInstallation Id: %s",
                    install.getName(), install.getId()
            ));
        }

        return messageBuilder.toString();
    }

    private static boolean appointmentTimeSelected(ServiceRequest serviceRequest) {
        return (serviceRequest.getStartTime() != null && serviceRequest.getResource() != null && serviceRequest.getResource().getId() != null);

    }


    private static class NotificationInformation implements ProductAndCustomerInformation {
        private final ServiceRequest serviceRequest;

        NotificationInformation(ServiceRequest serviceRequest) {
            this.serviceRequest = serviceRequest;
        }


        @Override
        public String getInstallationName() {
            //return Optional.ofNullable(this.serviceRequest.getInstallation())
            return Optional.ofNullable(this.serviceRequest.getOdooInstallationData())
                    .map(OdooInstallationData::getName)
                    .orElse("N/A");
        }

        @Override
        public String getProductName() {
            return Optional.ofNullable(this.serviceRequest.getProduct())
                    .map(Product::getName)
                    .orElse("Unknown");
        }

        @Override
        public int getProductQuantity() {
            return this.serviceRequest.getQuantity();
        }

        @Override
        public BigDecimal getTotalPaid() {
            return Optional.ofNullable(this.serviceRequest.getPaymentDetail())
                    .map(PaymentDetail::getTotal)
                    .orElse(BigDecimal.ZERO);
        }
    }



    @Override
    public ServiceRequest confirmOrderStripe(String paymentIntentId, ServiceRequest serviceRequest) throws IOException {

        PaymentMethod paymentMethod = this.paymentMethodService.getByName(PaymentMethods.STRIPE)
                .orElseThrow(() -> new ResourceClosedException(String.format("%s payment method not found!", PaymentMethods.STRIPE)));

        final BigDecimal requiredDepositAmount = serviceRequestPricingService.getRequiredDepositAmount(serviceRequest);
        final BigDecimal convenienceFee = serviceRequestPricingService.getConvenienceFee(serviceRequest, paymentMethod);
        final BigDecimal serviceRequestTotal = serviceRequestPricingService.getSubTotalAmount(serviceRequest);
        final BigDecimal initialPaidAmount = requiredDepositAmount.compareTo(BigDecimal.ZERO) > 0 ? requiredDepositAmount : serviceRequestTotal;

        PaymentDetail detail = this.paymentDetailService.save(initialPaidAmount, convenienceFee, paymentMethod, paymentIntentId);
        this.serviceRequestService.setPaymentDetail(serviceRequest, detail);

        return serviceRequest;
    }

    @Override
    public ServiceRequest processServiceRequestPaymentStripe(String paymentIntentId, ServiceRequest serviceRequest) throws IOException {

        LOGGER.info("Handling post-payment-processed actions for order {}", paymentIntentId);
        this.handleSchedulingAndInformationCapture(serviceRequest, paymentIntentId);

        LOGGER.info("Sending payment-processed notification for order {}: starting", paymentIntentId);
        this.productSoldNotificationService.sendNotification(new NotificationInformation(serviceRequest));
        LOGGER.info("Sending payment-processed notification for order {}: finished", paymentIntentId);

        return serviceRequest;
    }
}
