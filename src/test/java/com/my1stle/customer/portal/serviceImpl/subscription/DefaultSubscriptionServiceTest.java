package com.my1stle.customer.portal.serviceImpl.subscription;

import com.adobe.sign.model.agreements.AgreementCreationInfo;
import com.adobe.sign.model.agreements.AgreementCreationResponse;
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
import com.my1stle.customer.portal.service.pricing.PricingType;
import com.my1stle.customer.portal.service.pricing.SubscriptionPriceCalculator;
import com.my1stle.customer.portal.service.product.ProductAgreementDocumentService;
import com.my1stle.customer.portal.service.subscription.SubscriptionException;
import com.paypal.orders.Order;
import com.paypal.orders.PurchaseUnit;
import org.baeldung.persistence.model.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Unit Test for {@link DefaultSubscriptionService}
 */
@RunWith(MockitoJUnitRunner.class)
public class DefaultSubscriptionServiceTest {

    private DefaultSubscriptionService subscriptionService;

    @Mock
    private SubscriptionRepository mockSubscriptionRepository;

    @Mock
    private ProductRepository mockProductRepository;

    @Mock
    private SubscriptionFactory mockSubscriptionFactory;

    @Mock
    private ProductService mockProductService;

    @Mock
    private InstallationService mockInstallationService;

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private AdobeSignFacadeClient mockAdobeSignFacadeClient;

    @Mock
    private ProductAgreementDocumentService mockProductAgreementDocumentService;

    @Mock
    private SubscriptionAgreementCreationInfoFactory mockSubscriptionAgreementCreationInfoFactory;

    @Mock
    private PayPalClient mockPayPalClient;

    @Mock
    private OrderRepository mockOrderRepository;

    @Mock
    private PaymentMethodRepository mockPaymentMethodRepository;

    @Mock
    private FeeCalculator mockFeeCalculator;

    @Mock
    private SubscriptionPriceCalculator mockSubscriptionPriceCalculator;

    @Before
    public void setUp() throws Exception {

        this.subscriptionService = new DefaultSubscriptionService(
                mockProductService,
                mockInstallationService,
                mockSubscriptionRepository,
                mockOrderRepository,
                mockProductRepository,
                mockPaymentMethodRepository,
                mockSubscriptionFactory,
                mockSubscriptionAgreementCreationInfoFactory,
                mockProductAgreementDocumentService,
                mockAdobeSignFacadeClient,
                mockPayPalClient,
                mockFeeCalculator,
                mockSubscriptionPriceCalculator);
    }

    @Test
    public void shouldThrowSubscriptionExceptionWhenProductDoesNotHaveAnAgreementDocument() {

        // Given
        Long productId = 1869L;
        String productName = "Foobar";
        Product product = mock(Product.class);
        //Installation installation = mock(Installation.class);
        OdooInstallationData odooInstallationData = mock(OdooInstallationData.class);

        // Stubbing
        when(product.getId()).thenReturn(productId);
        when(product.getName()).thenReturn(productName);
        when(mockProductAgreementDocumentService.getByProductId(eq(productId))).thenReturn(Optional.empty());

        // When
        try {
            this.subscriptionService.validate(product, odooInstallationData);
        } catch (SubscriptionException e) {
            // Then
            verify(mockProductAgreementDocumentService, times(1)).getByProductId(eq(productId));
            assertEquals(String.format("Product %s does not have an agreement document!", productName), e.getMessage());
            return;
        }

        fail("Expected SubscriptionException to be thrown!");

    }

    @Test
    public void shouldGetSubscriptionById() {

        // Given
        Long subscriptionId = 1869L;
        Long ownerId = 420L;

        // Stubbing
        User stubbedOwner = mock(User.class);
        SubscriptionEntity stubbedSubscriptionEntity = mock(SubscriptionEntity.class);
        Subscription stubSubscription = mock(Subscription.class);

        when(mockSubscriptionRepository.findByIdAndOwnerId(eq(subscriptionId), eq(ownerId))).thenReturn(Optional.of(stubbedSubscriptionEntity));
        when(mockSubscriptionFactory.create(any(SubscriptionEntity.class))).thenReturn(stubSubscription);

        // When
        Optional<Subscription> subscription = this.subscriptionService.getByOwnerAndSubscriptionId(ownerId, subscriptionId);

        // Then
        verify(mockSubscriptionRepository, times(1)).findByIdAndOwnerId(eq(subscriptionId), eq(ownerId));
        verify(mockSubscriptionFactory, times(1)).create(any(SubscriptionEntity.class));


    }

    @Test
    public void shouldGetSubscriptionByOwnerProductAndInstallationIds() {

        // Given
        String installationId = "some installation id";
        Long ownerId = 420L;
        Long productId = 1L;

        // Stubbing
        SubscriptionEntity stubbedSubscriptionEntity = mock(SubscriptionEntity.class);
        Subscription stubSubscription = mock(Subscription.class);

        when(mockSubscriptionRepository.findByOwnerIdAndProductIdAndInstallationId(eq(ownerId), eq(productId), eq(installationId))).thenReturn(Optional.of(stubbedSubscriptionEntity));
        when(mockSubscriptionFactory.create(any(SubscriptionEntity.class))).thenReturn(stubSubscription);

        // When
        Optional<Subscription> subscription = this.subscriptionService.getByOwnerProductAndInstallationId(ownerId, productId, installationId);

        // Then
        verify(mockSubscriptionRepository, times(1)).findByOwnerIdAndProductIdAndInstallationId(eq(ownerId), eq(productId), eq(installationId));
        verify(mockSubscriptionFactory, times(1)).create(any(SubscriptionEntity.class));


    }

    @Test
    public void shouldCreateASubscription() {

        // Given
        Long productId = 1869L;
        String productName = "Foobar";
        Long ownerId = 420L;
        String userEmail = "development@1stlightenergy.com";
        String installationId = "some installation id";
        String installationName = "Foobar Installation";
        String externalLibraryDocumentId = "some library document id";
        Long subscriptionId = 999L;
        String agreementId = "some agreement id";

        // Stubbing
        User user = mock(User.class);
        ProductEntity stubProductEntity = mock(ProductEntity.class);
        ProductAgreementDocument stubProductAgreementDocument = mock(ProductAgreementDocument.class);
        Product product = mock(Product.class);
        //Installation installation = mock(Installation.class);
        OdooInstallationData odooInstallationData = mock(OdooInstallationData.class);
        SubscriptionEntity stubbedSubscriptionEntity = mock(SubscriptionEntity.class);
        SubscriptionTermsOfServiceEntity stubbedSubscriptionTermsOfServiceEntity = mock(SubscriptionTermsOfServiceEntity.class);
        AgreementCreationResponse stubbedAgreementCreationResponse = mock(AgreementCreationResponse.class);
        Subscription stubbedSubscription = mock(Subscription.class);

        when(product.getId()).thenReturn(productId);
        when(odooInstallationData.getId()).thenReturn(installationName);
        when(stubbedAgreementCreationResponse.getAgreementId()).thenReturn(agreementId);
        when(stubbedSubscriptionEntity.getSubscriptionTermsOfServiceEntity()).thenReturn(stubbedSubscriptionTermsOfServiceEntity);
        when(stubProductEntity.getPricingType()).thenReturn(PricingType.FIXED);

        when(mockSubscriptionRepository.save(any(SubscriptionEntity.class))).thenReturn(stubbedSubscriptionEntity);
        when(mockProductRepository.getOne(eq(productId))).thenReturn(stubProductEntity);
        when(mockAdobeSignFacadeClient.agreements().send(any(AgreementCreationInfo.class))).thenReturn(stubbedAgreementCreationResponse);
        when(mockSubscriptionFactory.create(any(SubscriptionEntity.class))).thenReturn(stubbedSubscription);

        // When
        //Subscription subscription = this.subscriptionService.create(user, product, installation);
        Subscription subscription = this.subscriptionService.create(user, product, odooInstallationData);

        // Then
        verify(mockProductRepository, times(1)).getOne(eq(productId));
        verify(mockAdobeSignFacadeClient, times(2)).agreements();
        verify(stubbedSubscriptionTermsOfServiceEntity).setAgreementId(eq(agreementId));
        verify(mockSubscriptionRepository, times(2)).save(any(SubscriptionEntity.class));
        verify(mockSubscriptionFactory, times(1)).create(any(SubscriptionEntity.class));

    }

    @Test
    public void shouldActivateSubscription() throws SubscriptionException, IOException {

        // Given
        Long ownerId = 1869L;
        Long subscriptionId = 1221L;
        String orderId = "some order id";
        BigDecimal subscriptionPrice = new BigDecimal(200.00);
        BigDecimal fixedFee = new BigDecimal(10.00);
        BigDecimal percentageFee = new BigDecimal(0.01); // 1 percent

        // Expecting
        BigDecimal totalPrice = new BigDecimal(212.00);

        // Stubbing
        SubscriptionEntity stubbedSubscriptionEntity = mock(SubscriptionEntity.class, Answers.RETURNS_DEEP_STUBS);
        Subscription stubbedSubscription = mock(Subscription.class);
        Order stubbedOrder = mock(Order.class, Answers.RETURNS_DEEP_STUBS);
        PurchaseUnit stubbedPurchaseUnit = mock(PurchaseUnit.class, Answers.RETURNS_DEEP_STUBS);
        PaymentMethodEntity stubbedPaymentMethod = mock(PaymentMethodEntity.class);
        Product stubbedProduct = mock(Product.class, Answers.RETURNS_DEEP_STUBS);

        when(mockSubscriptionFactory.create(any(SubscriptionEntity.class))).thenReturn(stubbedSubscription);
        when(mockSubscriptionRepository.findByIdAndOwnerId(anyLong(), anyLong())).thenReturn(Optional.of(stubbedSubscriptionEntity));
        when(mockPaymentMethodRepository.findByName(eq(PaymentMethods.PAYPAL))).thenReturn(Optional.of(stubbedPaymentMethod));

        when(stubbedPaymentMethod.getConvenienceCostFixed()).thenReturn(fixedFee);
        when(stubbedPaymentMethod.getConvenienceCostPercentage()).thenReturn(percentageFee);
        when(mockPayPalClient.getOrder(eq(orderId))).thenReturn(stubbedOrder);
        when(stubbedOrder.purchaseUnits().get(eq(0))).thenReturn(stubbedPurchaseUnit);
        when(stubbedOrder.status()).thenReturn(PaypalOrderStatus.APPROVED);
        when(stubbedPurchaseUnit.customId()).thenReturn(String.valueOf(subscriptionId));
        when(stubbedPurchaseUnit.amount().value()).thenReturn(totalPrice.toString());
        when(stubbedSubscription.getId()).thenReturn(subscriptionId);
        when(stubbedSubscription.getPrice()).thenReturn(subscriptionPrice);
        when(stubbedProduct.getPricePerUnit()).thenReturn(subscriptionPrice);
        when(stubbedProduct.getPricingType()).thenReturn(PricingType.FIXED);
        when(stubbedSubscriptionEntity.getProduct().getPaymentSchedule()).thenReturn(PaymentSchedule.YEARLY);
        when(mockSubscriptionRepository.save(any(SubscriptionEntity.class))).thenReturn(stubbedSubscriptionEntity);
        when(mockFeeCalculator.calculate(any(Subscription.class), any(PaymentMethod.class))).thenReturn(fixedFee);
        when(mockSubscriptionPriceCalculator.apply(any(Subscription.class), any(PaymentMethod.class))).thenReturn(totalPrice);

        // When
        Subscription activatedSubscription = this.subscriptionService.activateSubscription(ownerId, subscriptionId, orderId);

        // Then
        verify(mockPayPalClient, times(1)).getOrder(eq(orderId));
        verify(mockPayPalClient, times(1)).captureOrder(eq(orderId), eq(true));
        verify(stubbedSubscriptionEntity, times(1)).setActive(eq(true));
        verify(mockSubscriptionRepository, times(1)).save(any(SubscriptionEntity.class));
        verify(mockSubscriptionFactory, times(2)).create(any(SubscriptionEntity.class));

    }

}