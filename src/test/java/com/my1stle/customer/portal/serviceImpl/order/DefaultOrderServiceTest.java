package com.my1stle.customer.portal.serviceImpl.order;

import com.my1stle.customer.portal.persistence.model.OrderEntity;
import com.my1stle.customer.portal.persistence.model.ProductDiscountEntity;
import com.my1stle.customer.portal.persistence.model.ServiceRequestEntity;
import com.my1stle.customer.portal.service.model.Order;
import com.my1stle.customer.portal.service.model.ServiceRequest;
import com.my1stle.customer.portal.service.order.OrderService;
import com.my1stle.customer.portal.service.pricing.ServiceRequestPricingService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Unit Test for {@link DefaultOrderService}
 */
@RunWith(MockitoJUnitRunner.class)
public class DefaultOrderServiceTest {

    private OrderService orderService;

    @Mock
    private ServiceRequestPricingService mockServiceRequestPricingService;

    @Mock
    private JpaRepository<ServiceRequestEntity, Long> mockServiceRequestEntityLongJpaRepository;

    @Mock
    private JpaRepository<ProductDiscountEntity, Long> mockProductDiscountEntityLongJpaRepository;

    @Mock
    private JpaRepository<OrderEntity, Long> mockOrderEntityJpaRepository;

    @Captor
    private ArgumentCaptor<OrderEntity> argumentCaptorOrderEntity;

    @Before
    public void setUp() throws Exception {

        this.orderService = new DefaultOrderService(
                mockServiceRequestPricingService,
                mockServiceRequestEntityLongJpaRepository,
                mockProductDiscountEntityLongJpaRepository,
                mockOrderEntityJpaRepository);

    }

    @Test
    public void shouldGenerateOrderFromPaidServiceRequest() {

        // Given
        Long serviceRequestId = 1869L;
        Long productDiscountId = 1L;
        int quantity = 1;
        BigDecimal paidAmount = BigDecimal.valueOf(100.00);
        BigDecimal amountDue = BigDecimal.valueOf(100.00);
        ServiceRequest paidServiceRequest = mock(ServiceRequest.class);

        // Stubbing
        ServiceRequestEntity stubbedServiceRequestEntity = mock(ServiceRequestEntity.class, Answers.RETURNS_DEEP_STUBS);
        OrderEntity stubbedOrderEntity = mock(OrderEntity.class, Answers.RETURNS_DEEP_STUBS);
        when(paidServiceRequest.getId()).thenReturn(serviceRequestId);
        when(stubbedServiceRequestEntity.getQuantity()).thenReturn(quantity);
        when(stubbedServiceRequestEntity.getPaymentDetail().getAmount()).thenReturn(paidAmount);
        when(stubbedServiceRequestEntity.getProduct().getDiscounts()).thenReturn(Collections.emptySet());
        when(stubbedOrderEntity.getOrderItems()).thenReturn(Collections.emptyList());
        when(stubbedOrderEntity.getPaymentEntries()).thenReturn(Collections.emptyList());
        when(mockServiceRequestEntityLongJpaRepository.findById(eq(serviceRequestId))).thenReturn(Optional.of(stubbedServiceRequestEntity));
        when(mockServiceRequestPricingService.getRequiredDepositAmount(any(ServiceRequest.class))).thenReturn(BigDecimal.ZERO);
        when(mockServiceRequestPricingService.getSubTotalAmount(any(ServiceRequest.class))).thenReturn(amountDue);
        when(mockOrderEntityJpaRepository.save(any(OrderEntity.class))).thenReturn(stubbedOrderEntity);

        // When
        Order generate = this.orderService.generate(paidServiceRequest);

        // Then
        verify(mockServiceRequestEntityLongJpaRepository).findById(eq(serviceRequestId));
        verify(mockServiceRequestPricingService).getRequiredDepositAmount(any(ServiceRequest.class));
        verify(mockServiceRequestPricingService).getSubTotalAmount(any(ServiceRequest.class));
        verify(mockProductDiscountEntityLongJpaRepository, times(0)).findById(eq(productDiscountId));
        verify(mockOrderEntityJpaRepository).save(argumentCaptorOrderEntity.capture());

        OrderEntity capturedOrder = argumentCaptorOrderEntity.getValue();

        assertEquals(0, amountDue.compareTo(capturedOrder.getAmountDue()));
        assertEquals(0, paidAmount.compareTo(capturedOrder.getAmountPaid()));
        assertEquals(1, capturedOrder.getOrderItems().size());
        assertEquals(1, capturedOrder.getPaymentEntries().size());
        assertTrue(capturedOrder.getOrderItems().get(0).getOrderItemDiscounts().isEmpty());

    }


    @Test
    public void shouldThrowIllegalArgumentExceptionWhenServiceRequestQuantityIsLessThanOrEqualToZero() {

        // Given
        Long serviceRequestId = 1869L;
        Long productDiscountId = 1L;
        int quantity = -1;
        BigDecimal paidAmount = BigDecimal.valueOf(100.00);
        BigDecimal amountDue = BigDecimal.valueOf(100.00);
        ServiceRequest paidServiceRequest = mock(ServiceRequest.class);

        // Stubbing
        ServiceRequestEntity stubbedServiceRequestEntity = mock(ServiceRequestEntity.class, Answers.RETURNS_DEEP_STUBS);
        OrderEntity stubbedOrderEntity = mock(OrderEntity.class, Answers.RETURNS_DEEP_STUBS);
        when(paidServiceRequest.getId()).thenReturn(serviceRequestId);
        when(stubbedServiceRequestEntity.getQuantity()).thenReturn(quantity);
        when(stubbedServiceRequestEntity.getPaymentDetail().getAmount()).thenReturn(paidAmount);
        when(stubbedServiceRequestEntity.getProduct().getDiscounts()).thenReturn(Collections.emptySet());
        when(stubbedOrderEntity.getOrderItems()).thenReturn(Collections.emptyList());
        when(stubbedOrderEntity.getPaymentEntries()).thenReturn(Collections.emptyList());
        when(mockServiceRequestEntityLongJpaRepository.findById(eq(serviceRequestId))).thenReturn(Optional.of(stubbedServiceRequestEntity));

        // When
        try {
            Order generate = this.orderService.generate(paidServiceRequest);
        } catch (IllegalArgumentException e) {

            // Then
            verify(mockServiceRequestEntityLongJpaRepository, times(1)).findById(eq(serviceRequestId));
            verify(mockServiceRequestPricingService, times(0)).getRequiredDepositAmount(any(ServiceRequest.class));
            verify(mockServiceRequestPricingService, times(0)).getSubTotalAmount(any(ServiceRequest.class));
            verify(mockProductDiscountEntityLongJpaRepository, times(0)).findById(eq(productDiscountId));
            verify(mockOrderEntityJpaRepository, times(0)).save(argumentCaptorOrderEntity.capture());

            assertEquals("Expected Service Request's quantity to be greater than zero!", e.getMessage());

            return;
        }

        fail(String.format("Expected %s to be thrown!", IllegalArgumentException.class.getSimpleName()));


    }

    @Test
    public void shouldThrowIllegalArgumentExceptionWhenServiceRequestPaymentDetailAmountIsLessThanTheAmountDue() {

        // Given
        Long serviceRequestId = 1869L;
        Long productDiscountId = 1L;
        int quantity = 1;
        BigDecimal paidAmount = BigDecimal.valueOf(1.00);
        BigDecimal amountDue = BigDecimal.valueOf(100.00);
        ServiceRequest paidServiceRequest = mock(ServiceRequest.class);

        // Stubbing
        ServiceRequestEntity stubbedServiceRequestEntity = mock(ServiceRequestEntity.class, Answers.RETURNS_DEEP_STUBS);
        OrderEntity stubbedOrderEntity = mock(OrderEntity.class, Answers.RETURNS_DEEP_STUBS);
        when(paidServiceRequest.getId()).thenReturn(serviceRequestId);
        when(stubbedServiceRequestEntity.getQuantity()).thenReturn(quantity);
        when(stubbedServiceRequestEntity.getPaymentDetail().getAmount()).thenReturn(paidAmount);
        when(stubbedServiceRequestEntity.getProduct().getDiscounts()).thenReturn(Collections.emptySet());
        when(stubbedOrderEntity.getOrderItems()).thenReturn(Collections.emptyList());
        when(stubbedOrderEntity.getPaymentEntries()).thenReturn(Collections.emptyList());
        when(mockServiceRequestEntityLongJpaRepository.findById(eq(serviceRequestId))).thenReturn(Optional.of(stubbedServiceRequestEntity));
        when(mockServiceRequestPricingService.getRequiredDepositAmount(any(ServiceRequest.class))).thenReturn(BigDecimal.ZERO);
        when(mockServiceRequestPricingService.getSubTotalAmount(any(ServiceRequest.class))).thenReturn(amountDue);

        // When
        try {
            Order generate = this.orderService.generate(paidServiceRequest);
        } catch (IllegalArgumentException e) {

            // Then
            verify(mockServiceRequestEntityLongJpaRepository, times(1)).findById(eq(serviceRequestId));
            verify(mockServiceRequestPricingService, times(1)).getRequiredDepositAmount(any(ServiceRequest.class));
            verify(mockServiceRequestPricingService, times(1)).getSubTotalAmount(any(ServiceRequest.class));
            verify(mockProductDiscountEntityLongJpaRepository, times(0)).findById(eq(productDiscountId));
            verify(mockOrderEntityJpaRepository, times(0)).save(argumentCaptorOrderEntity.capture());

            assertEquals("Amount paid is less than amount due!", e.getMessage());

            return;
        }

        fail(String.format("Expected %s to be thrown!", IllegalArgumentException.class.getSimpleName()));

    }

}