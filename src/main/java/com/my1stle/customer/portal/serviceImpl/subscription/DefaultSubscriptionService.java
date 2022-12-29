package com.my1stle.customer.portal.serviceImpl.subscription;

import com.adobe.sign.model.agreements.AgreementCreationInfo;
import com.adobe.sign.model.agreements.AgreementCreationResponse;
import com.braintreepayments.http.HttpResponse;
import com.my1stle.customer.portal.persistence.model.OrderEntity;
import com.my1stle.customer.portal.persistence.model.OrderItemEntity;
import com.my1stle.customer.portal.persistence.model.PaymentDetailEntity;
import com.my1stle.customer.portal.persistence.model.PaymentMethodEntity;
import com.my1stle.customer.portal.persistence.model.ProductEntity;
import com.my1stle.customer.portal.persistence.model.SubscriptionEntity;
import com.my1stle.customer.portal.persistence.model.SubscriptionTermsOfServiceEntity;
import com.my1stle.customer.portal.persistence.repository.OrderRepository;
import com.my1stle.customer.portal.persistence.repository.PaymentMethodRepository;
import com.my1stle.customer.portal.persistence.repository.ProductRepository;
import com.my1stle.customer.portal.persistence.repository.SubscriptionRepository;
import com.my1stle.customer.portal.service.InstallationService;
import com.my1stle.customer.portal.service.ProductService;
import com.my1stle.customer.portal.service.adobe.sign.client.facade.AdobeSignFacadeClient;
import com.my1stle.customer.portal.service.model.Installation;
import com.my1stle.customer.portal.service.model.PaymentMethod;
import com.my1stle.customer.portal.service.model.Product;
import com.my1stle.customer.portal.service.model.ProductAgreementDocument;
import com.my1stle.customer.portal.service.model.Subscription;
import com.my1stle.customer.portal.service.odoo.OdooInstallationData;
import com.my1stle.customer.portal.service.payment.PaymentMethods;
import com.my1stle.customer.portal.service.paypal.PayPalClient;
import com.my1stle.customer.portal.service.paypal.PaypalOrderStatus;
import com.my1stle.customer.portal.service.pricing.FeeCalculator;
import com.my1stle.customer.portal.service.pricing.PaymentSchedule;
import com.my1stle.customer.portal.service.pricing.SubscriptionPriceCalculator;
import com.my1stle.customer.portal.service.product.ProductAgreementDocumentService;
import com.my1stle.customer.portal.service.subscription.SubscriptionException;
import com.my1stle.customer.portal.service.subscription.SubscriptionService;
import com.my1stle.customer.portal.serviceImpl.model.PaymentMethodProxy;
import com.my1stle.customer.portal.web.dto.subscription.SubscriptionAgreementDto;
import com.my1stle.customer.portal.web.exception.ResourceNotFoundException;
import com.paypal.orders.Order;
import com.paypal.orders.PurchaseUnit;
import org.baeldung.persistence.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * TODO reduce number of depencenies
 */
@Service
public class DefaultSubscriptionService implements SubscriptionService {

    private ProductService productService;
    private InstallationService installationService;
    private SubscriptionRepository subscriptionRepository;
    private OrderRepository orderRepository;
    private ProductRepository productRepository;
    private PaymentMethodRepository paymentMethodRepository;
    private SubscriptionFactory subscriptionFactory;
    private SubscriptionAgreementCreationInfoFactory subscriptionAgreementCreationInfoFactory;
    private ProductAgreementDocumentService productAgreementDocumentService;
    private AdobeSignFacadeClient adobeSignFacadeClient;
    private PayPalClient payPalClient;
    private FeeCalculator feeCalculator;
    private SubscriptionPriceCalculator subscriptionPriceCalculator;

    @Autowired
    public DefaultSubscriptionService(
            ProductService productService,
            InstallationService installationService,
            SubscriptionRepository subscriptionRepository,
            OrderRepository orderRepository,
            ProductRepository productRepository,
            PaymentMethodRepository paymentMethodRepository,
            SubscriptionFactory subscriptionFactory,
            SubscriptionAgreementCreationInfoFactory subscriptionAgreementCreationInfoFactory,
            ProductAgreementDocumentService productAgreementDocumentService,
            AdobeSignFacadeClient adobeSignFacadeClient,
            PayPalClient payPalClient,
            FeeCalculator feeCalculator, SubscriptionPriceCalculator subscriptionPriceCalculator) {
        this.productService = productService;
        this.installationService = installationService;
        this.subscriptionRepository = subscriptionRepository;
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.paymentMethodRepository = paymentMethodRepository;
        this.subscriptionFactory = subscriptionFactory;
        this.subscriptionAgreementCreationInfoFactory = subscriptionAgreementCreationInfoFactory;
        this.productAgreementDocumentService = productAgreementDocumentService;
        this.adobeSignFacadeClient = adobeSignFacadeClient;
        this.payPalClient = payPalClient;
        this.feeCalculator = feeCalculator;
        this.subscriptionPriceCalculator = subscriptionPriceCalculator;
    }

    @Override
    public Optional<Subscription> getByOwnerAndSubscriptionId(Long ownerId, Long subscriptionId) {

        return subscriptionRepository.findByIdAndOwnerId(subscriptionId, ownerId)
                .map(subscriptionEntity -> subscriptionFactory.create(subscriptionEntity));

    }

    @Override
    public Optional<Subscription> getByOwnerProductAndInstallationId(Long ownerId, Long productId, String installationId) {

        return subscriptionRepository.findByOwnerIdAndProductIdAndInstallationId(ownerId, productId, installationId)
                .map(subscriptionEntity -> subscriptionFactory.create(subscriptionEntity));
    }

    @Override
    public List<Subscription> getOwnersActiveSubscriptions(Long ownerId, String installationId) {
        return this.subscriptionRepository.findByOwnerIdAndInstallationIdAndActiveIsTrue(ownerId, installationId)
                .stream()
                .map(entity -> subscriptionFactory.create(entity))
                .collect(Collectors.toList());
    }

    /**
     * @param ownerId
     * @param subscriptionId
     * @param orderId
     * @throws SubscriptionException
     * @implNote checks against PayPal and activates the subscription
     */
    @Override
    @Transactional
    public Subscription activateSubscription(Long ownerId, Long subscriptionId, String orderId) throws SubscriptionException {

        SubscriptionEntity subscriptionEntity = this.subscriptionRepository
                .findByIdAndOwnerId(subscriptionId, ownerId)
                .orElseThrow(() -> new ResourceNotFoundException("Subscription Not Found!"));

        // Hard code check for now
        PaymentMethodEntity paymentMethodEntity = this.paymentMethodRepository
                .findByName(PaymentMethods.PAYPAL)
                .orElseThrow(() -> new ResourceNotFoundException("PayPal Payment Method Not Found!"));

        Subscription subscription = this.subscriptionFactory.create(subscriptionEntity);
        PaymentMethod paymentMethod = PaymentMethodProxy.from(paymentMethodEntity);

        Order payPalOrder = null;

        try {
            payPalOrder = payPalClient.getOrder(orderId);
        } catch (IOException e) {
            throw new RuntimeException("Unable to retrieve order details!", e);
        }

        // Validating
        PurchaseUnit purchaseUnit = payPalOrder.purchaseUnits().get(0);
        Long subscriptionIdFromOrder = Long.valueOf(purchaseUnit.customId());

        if (!subscription.getId().equals(subscriptionIdFromOrder)) {
            throw new SubscriptionException("Subscription Ids Do Not Match!");
        }

        BigDecimal productPrice = subscription.getPrice();
        BigDecimal payPalOrderTotal = BigDecimal.valueOf(Double.valueOf(purchaseUnit.amount().value()));
        BigDecimal convenienceFee = feeCalculator.calculate(subscription, paymentMethod);
        BigDecimal total = this.getTotalPrice(subscription);

        if (0 != total.compareTo(payPalOrderTotal)) {
            throw new SubscriptionException("Subscription Price and Order Price Do Not Match!");
        }

        if (!payPalOrder.status().equals(PaypalOrderStatus.APPROVED)) {
            throw new SubscriptionException("Order has not been approved!");
        }

        // Start of transaction
        try {
            HttpResponse<Order> httpPayPalOrder = this.payPalClient.captureOrder(orderId, true);
        } catch (IOException e) {
            throw new RuntimeException("Unable to capture order!", e);
        }

        ProductEntity selectedProduct = subscriptionEntity.getProduct();

        PaymentDetailEntity paymentDetailEntity = new PaymentDetailEntity(
                subscriptionEntity.getOwner(),
                paymentMethodEntity,
                productPrice,
                convenienceFee,
                orderId
        );

        OrderItemEntity orderItem = new OrderItemEntity(
                selectedProduct,
                1, // hardcode for now
                productPrice
        );

        ZonedDateTime now = ZonedDateTime.now(ZoneOffset.UTC);

        OrderEntity order = new OrderEntity(
                subscriptionEntity.getOwner(),
                productPrice,
                payPalOrderTotal,
                Collections.singletonList(orderItem),
                Collections.singletonList(paymentDetailEntity)
        );

        order.setOrderDate(now);

        OrderEntity savedOrder = this.orderRepository.save(order);

        subscriptionEntity.setActive(true);
        subscriptionEntity.setStartingDate(now);
        subscriptionEntity.setExpirationDate(selectedProduct.getPaymentSchedule().calculateStatementDate(now));
        subscriptionEntity.addOrder(savedOrder);

        SubscriptionEntity saved = this.subscriptionRepository.save(subscriptionEntity);

        return this.subscriptionFactory.create(saved);

    }

    /**
     * @param subscription
     * @return total price for subscription
     * @implNote total price = product_price + (product price * convenience_cost_percentage) + convenience_cost_fixed
     * @see <a href="https://docs.oracle.com/javase/8/docs/api/java/math/RoundingMode.html#DOWN">Rounding Down</a>
     */
    @Override
    public BigDecimal getTotalPrice(Subscription subscription) {

        // Hard code check for paypal only for now
        PaymentMethodEntity paymentMethodEntity = this.paymentMethodRepository
                .findByName(PaymentMethods.PAYPAL).orElseThrow(() -> new ResourceNotFoundException("Paypal Payment Not Found!"));

        return subscriptionPriceCalculator.apply(subscription, PaymentMethodProxy.from(paymentMethodEntity)).setScale(2, RoundingMode.DOWN);

    }
    /*
    @Override
    @Transactional
    public Subscription subscribe(SubscriptionAgreementDto subscriptionAgreementDto) throws SubscriptionException {

        Objects.requireNonNull(subscriptionAgreementDto, "subscription request must not be null");

        Product product = productService.getById(subscriptionAgreementDto.getProductId());

        if (null == product) {
            throw new ResourceNotFoundException("Product Not Found!");
        }

        Installation installation = installationService.getInstallationById(subscriptionAgreementDto.getInstallationId());

        if (null == installation) {
            throw new ResourceNotFoundException("Installation Not Found!");
        }

        isSubscriptionProduct(product);

        validate(product, installation);

        return create(subscriptionAgreementDto.getUser(), product, installation);
    }
    */
    @Override
    @Transactional
    public Subscription subscribe(SubscriptionAgreementDto subscriptionAgreementDto) throws SubscriptionException {

        Objects.requireNonNull(subscriptionAgreementDto, "subscription request must not be null");

        Product product = productService.getById(subscriptionAgreementDto.getProductId());

        if (null == product) {
            throw new ResourceNotFoundException("Product Not Found!");
        }

        OdooInstallationData odooInstallationData = new OdooInstallationData(subscriptionAgreementDto.getInstallationId(), "project.Task");

        if (null == odooInstallationData) {
            throw new ResourceNotFoundException("Installation Not Found!");
        }

        isSubscriptionProduct(product);

        validate(product, odooInstallationData);

        return create(subscriptionAgreementDto.getUser(), product, odooInstallationData);
    }

    @Override
    public Optional<Resource> getSubscriptionAgreementResource(Long ownerId, Long subscriptionId) {


        Optional<Subscription> byOwnerAndSubscriptionId = this.getByOwnerAndSubscriptionId(ownerId, subscriptionId);

        if (!byOwnerAndSubscriptionId.isPresent()) {
            return Optional.empty();
        }

        Resource agreementResource = this.adobeSignFacadeClient.agreements()
                .getAgreementResource(byOwnerAndSubscriptionId.get().getSubscriptionTermsOfService().getAgreementId());

        if (null == agreementResource) {
            return Optional.empty();
        } else {
            return Optional.of(agreementResource);
        }

    }


    private void isSubscriptionProduct(Product product) throws SubscriptionException {

        if (product.getPaymentSchedule().equals(PaymentSchedule.SINGLE_PAYMENT)) {
            throw new SubscriptionException("Expected Non-Single Payment Product");
        }

    }
    /*
    protected void validate(Product product, Installation installation) throws SubscriptionException {

        Optional<ProductAgreementDocument> agreementDocument = productAgreementDocumentService.getByProductId(product.getId());

        if (!agreementDocument.isPresent()) {
            throw new SubscriptionException(String.format("Product %s does not have an agreement document!", product.getName()));
        }

    }
    */
    protected void validate(Product product, OdooInstallationData odooInstallationData) throws SubscriptionException {

        Optional<ProductAgreementDocument> agreementDocument = productAgreementDocumentService.getByProductId(product.getId());

        if (!agreementDocument.isPresent()) {
            throw new SubscriptionException(String.format("Product %s does not have an agreement document!", product.getName()));
        }

    }

    /**
     * @param user
     * @param product
     * @param odooInstallationData
     * @return a newly create subscription with the given product for the given installation
     * @implNote this implementation also sends an AdobeSign agreement based on the product's
     * agreement document
     */
    /*
    protected Subscription create(User user, Product product, Installation installation) {

        Objects.requireNonNull(user, "User expected!");
        Objects.requireNonNull(product, "Product expected!");
        Objects.requireNonNull(installation, "Installation expected!");

        SubscriptionEntity subscriptionEntity = generateAdobeSignAgreement(user, product, installation);

        return this.subscriptionFactory.create(subscriptionEntity);

    }
    */
    protected Subscription create(User user, Product product, OdooInstallationData odooInstallationData) {

        Objects.requireNonNull(user, "User expected!");
        Objects.requireNonNull(product, "Product expected!");
        Objects.requireNonNull(odooInstallationData, "Installation expected!");

        SubscriptionEntity subscriptionEntity = generateAdobeSignAgreement(user, product, odooInstallationData);

        return this.subscriptionFactory.create(subscriptionEntity);

    }

    /*
    private SubscriptionEntity generateAdobeSignAgreement(User user, Product product, Installation installation) {

        ProductEntity productEntity = productRepository.getOne(product.getId());

        SubscriptionTermsOfServiceEntity subscriptionTermsOfServiceEntity = new SubscriptionTermsOfServiceEntity();
        SubscriptionEntity entity = new SubscriptionEntity(user, productEntity, installation, subscriptionTermsOfServiceEntity);
        SubscriptionEntity subscriptionEntity = subscriptionRepository.save(entity);

        AgreementCreationInfo agreementCreationInfo = this.subscriptionAgreementCreationInfoFactory.create(subscriptionEntity);
        AgreementCreationResponse response = this.adobeSignFacadeClient.agreements().send(agreementCreationInfo);
        subscriptionEntity.getSubscriptionTermsOfServiceEntity().setAgreementId(response.getAgreementId());

        return subscriptionRepository.save(subscriptionEntity);

    }
    */

    private SubscriptionEntity generateAdobeSignAgreement(User user, Product product, OdooInstallationData odooInstallationData) {

        ProductEntity productEntity = productRepository.getOne(product.getId());

        SubscriptionTermsOfServiceEntity subscriptionTermsOfServiceEntity = new SubscriptionTermsOfServiceEntity();
        SubscriptionEntity entity = new SubscriptionEntity(user, productEntity, odooInstallationData, subscriptionTermsOfServiceEntity);
        SubscriptionEntity subscriptionEntity = subscriptionRepository.save(entity);

        AgreementCreationInfo agreementCreationInfo = this.subscriptionAgreementCreationInfoFactory.create(subscriptionEntity);
        AgreementCreationResponse response = this.adobeSignFacadeClient.agreements().send(agreementCreationInfo);
        subscriptionEntity.getSubscriptionTermsOfServiceEntity().setAgreementId(response.getAgreementId());

        return subscriptionRepository.save(subscriptionEntity);

    }

}