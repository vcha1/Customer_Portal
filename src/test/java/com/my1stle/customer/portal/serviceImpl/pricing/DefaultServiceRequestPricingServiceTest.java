package com.my1stle.customer.portal.serviceImpl.pricing;

import com.my1stle.customer.portal.service.model.Installation;
import com.my1stle.customer.portal.service.model.PaymentMethod;
import com.my1stle.customer.portal.service.model.Product;
import com.my1stle.customer.portal.service.model.ServiceRequest;
import com.my1stle.customer.portal.service.odoo.OdooInstallationData;
import com.my1stle.customer.portal.service.pricing.FeeCalculator;
import com.my1stle.customer.portal.service.pricing.ProductDiscountCalculator;
import com.my1stle.customer.portal.service.pricing.ServiceRequestPricingService;
import com.my1stle.customer.portal.service.pricing.SubtotalCalculator;
import com.my1stle.customer.portal.service.product.ProductPricing;
import org.baeldung.persistence.model.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Unit Test for {@link DefaultServiceRequestPricingService}
 */
@RunWith(MockitoJUnitRunner.class)
public class DefaultServiceRequestPricingServiceTest {

    private ServiceRequestPricingService serviceRequestPricingService;

    @Mock
    private SubtotalCalculator mockSubtotalCalculator;

    @Mock
    private ProductDiscountCalculator mockProductDiscountCalculator;

    @Mock
    private FeeCalculator mockFeeCalculator;

    @Before
    public void setUp() throws Exception {
        this.serviceRequestPricingService = new DefaultServiceRequestPricingService(mockSubtotalCalculator, mockProductDiscountCalculator, mockFeeCalculator);
    }

    @Test
    public void shouldGetTotalPrice() {

        // Given
        User user = mock(User.class);
        //Installation installation = mock(Installation.class);
        OdooInstallationData odooInstallationData = mock(OdooInstallationData.class);
        Product product = mock(Product.class);
        int quantity = 1;
        BigDecimal amount = BigDecimal.ONE;

        // Stubbing
        ServiceRequest serviceRequest = mock(ServiceRequest.class);
        PaymentMethod paymentMethod = mock(PaymentMethod.class);
        when(serviceRequest.getUser()).thenReturn(user);
        when(serviceRequest.getProduct()).thenReturn(product);
        //(serviceRequest.getInstallation()).thenReturn(installation);
        when(serviceRequest.getOdooInstallationData()).thenReturn(odooInstallationData);
        when(serviceRequest.getQuantity()).thenReturn(quantity);
        //when(mockSubtotalCalculator.getSubtotal(any(Installation.class), any(Product.class), eq(quantity))).thenReturn(amount);
        //when(mockProductDiscountCalculator.getTotalDiscountAmount(any(User.class), any(Installation.class), any(Product.class), eq(quantity))).thenReturn(BigDecimal.ZERO);
        when(mockSubtotalCalculator.getSubtotal(any(OdooInstallationData.class), any(Product.class), eq(quantity))).thenReturn(amount);
        when(mockProductDiscountCalculator.getTotalDiscountAmount(any(User.class), any(OdooInstallationData.class), any(Product.class), eq(quantity))).thenReturn(BigDecimal.ZERO);
        when(mockFeeCalculator.calculate(any(ServiceRequest.class), any(PaymentMethod.class))).thenReturn(BigDecimal.ZERO);

        // When
        BigDecimal totalPrice = this.serviceRequestPricingService.getTotalPrice(serviceRequest, paymentMethod);

        // Then
        //verify(mockSubtotalCalculator).getSubtotal(any(Installation.class), any(Product.class), eq(quantity));
        //verify(mockProductDiscountCalculator).getTotalDiscountAmount(any(User.class), any(Installation.class), any(Product.class), eq(quantity));
        verify(mockSubtotalCalculator).getSubtotal(any(OdooInstallationData.class), any(Product.class), eq(quantity));
        verify(mockProductDiscountCalculator).getTotalDiscountAmount(any(User.class), any(OdooInstallationData.class), any(Product.class), eq(quantity));
        verify(mockFeeCalculator).calculate(any(ServiceRequest.class), any(PaymentMethod.class));

        assertEquals(0, totalPrice.compareTo(BigDecimal.ONE));

    }

    @Test
    public void shouldGetRequiredDepositAmount() {

        // Given
        User user = mock(User.class);
        //Installation installation = mock(Installation.class);
        OdooInstallationData odooInstallationData = mock(OdooInstallationData.class);
        Product product = mock(Product.class);
        int quantity = 1;
        BigDecimal amount = BigDecimal.valueOf(100);
        BigDecimal requiredDepositPercentage = BigDecimal.valueOf(0.10);

        // Stubbing
        ServiceRequest serviceRequest = mock(ServiceRequest.class);
        PaymentMethod paymentMethod = mock(PaymentMethod.class);
        when(serviceRequest.getProduct()).thenReturn(product);
        when(product.getRequiredDepositPercentage()).thenReturn(requiredDepositPercentage);
        //when(serviceRequest.getInstallation()).thenReturn(installation);
        when(serviceRequest.getOdooInstallationData()).thenReturn(odooInstallationData);
        when(serviceRequest.getQuantity()).thenReturn(quantity);
        //when(mockSubtotalCalculator.getSubtotal(any(Installation.class), any(Product.class), eq(quantity))).thenReturn(amount);
        when(mockSubtotalCalculator.getSubtotal(any(OdooInstallationData.class), any(Product.class), eq(quantity))).thenReturn(amount);

        // When
        BigDecimal requiredDepositAmount = this.serviceRequestPricingService.getRequiredDepositAmount(serviceRequest);

        // Then
        //verify(mockSubtotalCalculator).getSubtotal(any(Installation.class), any(Product.class), eq(quantity));
        verify(mockSubtotalCalculator).getSubtotal(any(OdooInstallationData.class), any(Product.class), eq(quantity));

        assertEquals(0, requiredDepositAmount.compareTo(BigDecimal.valueOf(10.00)));

    }

    @Test
    public void shouldGetMaxRequiredDepositAmountWhenCalculatedRequiredAmountExceedsMaximumRequiredAmount() {

        // Given
        User user = mock(User.class);
        //Installation installation = mock(Installation.class);
        OdooInstallationData odooInstallationData = mock(OdooInstallationData.class);
        Product product = mock(Product.class);
        int quantity = 1;
        BigDecimal amount = ProductPricing.MAX_REQUIRED_DEPOSIT_AMOUNT.multiply(BigDecimal.valueOf(10));
        BigDecimal requiredDepositPercentage = BigDecimal.valueOf(0.99);

        // Stubbing
        ServiceRequest serviceRequest = mock(ServiceRequest.class);
        PaymentMethod paymentMethod = mock(PaymentMethod.class);
        when(serviceRequest.getProduct()).thenReturn(product);
        when(product.getRequiredDepositPercentage()).thenReturn(requiredDepositPercentage);
        when(serviceRequest.getOdooInstallationData()).thenReturn(odooInstallationData);
        when(serviceRequest.getQuantity()).thenReturn(quantity);
        //when(mockSubtotalCalculator.getSubtotal(any(Installation.class), any(Product.class), eq(quantity))).thenReturn(amount);
        when(mockSubtotalCalculator.getSubtotal(any(OdooInstallationData.class), any(Product.class), eq(quantity))).thenReturn(amount);

        // When
        BigDecimal requiredDepositAmount = this.serviceRequestPricingService.getRequiredDepositAmount(serviceRequest);

        // Then
        //verify(mockSubtotalCalculator).getSubtotal(any(Installation.class), any(Product.class), eq(quantity));
        verify(mockSubtotalCalculator).getSubtotal(any(OdooInstallationData.class), any(Product.class), eq(quantity));

        assertEquals(0, requiredDepositAmount.compareTo(ProductPricing.MAX_REQUIRED_DEPOSIT_AMOUNT));

    }

    @Test
    public void shouldThrowIllegalArgumentExceptionIfProductRequiredPercentageAmountExceeds100Percent() {

        // Given
        User user = mock(User.class);
        Installation installation = mock(Installation.class);
        Product product = mock(Product.class);
        int quantity = 1;
        BigDecimal amount = BigDecimal.valueOf(100);
        BigDecimal requiredDepositPercentage = BigDecimal.valueOf(2); // i.e 200%;

        // Stubbing
        ServiceRequest serviceRequest = mock(ServiceRequest.class);
        PaymentMethod paymentMethod = mock(PaymentMethod.class);
        when(serviceRequest.getProduct()).thenReturn(product);
        when(product.getRequiredDepositPercentage()).thenReturn(requiredDepositPercentage);

        // When
        try {
            BigDecimal requiredDepositAmount = this.serviceRequestPricingService.getRequiredDepositAmount(serviceRequest);
        } catch (IllegalArgumentException e) {

            // Then
            //verify(mockSubtotalCalculator, times(0)).getSubtotal(any(Installation.class), any(Product.class), eq(quantity));
            //verify(mockProductDiscountCalculator, times(0)).getTotalDiscountAmount(any(User.class), any(Installation.class), any(Product.class), eq(quantity));
            verify(mockSubtotalCalculator, times(0)).getSubtotal(any(OdooInstallationData.class), any(Product.class), eq(quantity));
            verify(mockProductDiscountCalculator, times(0)).getTotalDiscountAmount(any(User.class), any(OdooInstallationData.class), any(Product.class), eq(quantity));
            verify(mockFeeCalculator, times(0)).calculate(any(ServiceRequest.class), any(PaymentMethod.class));

            return;

        }

        fail(String.format("Expected %s to be thrown!", IllegalArgumentException.class.getSimpleName()));

    }

}