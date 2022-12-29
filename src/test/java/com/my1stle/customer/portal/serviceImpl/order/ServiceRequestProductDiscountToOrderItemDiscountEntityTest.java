package com.my1stle.customer.portal.serviceImpl.order;

import com.my1stle.customer.portal.persistence.model.OrderItemDiscountEntity;
import com.my1stle.customer.portal.persistence.model.ProductDiscountEntity;
import com.my1stle.customer.portal.service.model.ProductDiscount;
import com.my1stle.customer.portal.service.model.ServiceRequest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.function.Function;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Unit test for {@link ServiceRequestProductDiscountToOrderItemDiscountEntity}
 */
@RunWith(MockitoJUnitRunner.class)
public class ServiceRequestProductDiscountToOrderItemDiscountEntityTest {

    private Function<ProductDiscount, OrderItemDiscountEntity> function;

    @Mock
    private ServiceRequest mockServiceRequest;

    @Mock
    private JpaRepository<ProductDiscountEntity, Long> mockProductDiscountEntityLongJpaRepository;

    @Before
    public void setUp() throws Exception {
        this.function = new ServiceRequestProductDiscountToOrderItemDiscountEntity(mockServiceRequest, mockProductDiscountEntityLongJpaRepository);
    }

    @Test
    public void shouldApply() {

        // Given
        Long productDiscountId = 1869L;

        // Stubbing
        ProductDiscount productDiscount = mock(ProductDiscount.class);
        when(productDiscount.getId()).thenReturn(productDiscountId);
        when(mockProductDiscountEntityLongJpaRepository.findById(eq(productDiscountId))).thenReturn(Optional.of(mock(ProductDiscountEntity.class)));

        // When
        OrderItemDiscountEntity orderItemDiscountEntity = this.function.apply(productDiscount);

        // Then
        verify(mockProductDiscountEntityLongJpaRepository).findById(productDiscountId);
        assertNotNull(orderItemDiscountEntity);

    }
}