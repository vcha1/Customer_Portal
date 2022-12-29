package com.my1stle.customer.portal.serviceImpl.order;

import com.my1stle.customer.portal.persistence.model.OrderItemDiscountEntity;
import com.my1stle.customer.portal.persistence.model.ProductDiscountEntity;
import com.my1stle.customer.portal.service.model.ProductDiscount;
import com.my1stle.customer.portal.service.model.ServiceRequest;
import com.my1stle.customer.portal.web.exception.ResourceNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Objects;
import java.util.function.Function;

class ServiceRequestProductDiscountToOrderItemDiscountEntity implements Function<ProductDiscount, OrderItemDiscountEntity> {

    private ServiceRequest serviceRequest;
    private JpaRepository<ProductDiscountEntity, Long> productDiscountEntityLongJpaRepository;

    ServiceRequestProductDiscountToOrderItemDiscountEntity(ServiceRequest serviceRequest, JpaRepository<ProductDiscountEntity, Long> productDiscountEntityLongJpaRepository) {
        this.serviceRequest = serviceRequest;
        this.productDiscountEntityLongJpaRepository = productDiscountEntityLongJpaRepository;
    }

    @Override
    public OrderItemDiscountEntity apply(ProductDiscount productDiscount) {

        Objects.requireNonNull(productDiscount);

        ProductDiscountEntity productDiscountEntity = this.productDiscountEntityLongJpaRepository.findById(productDiscount.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Product Discount Not Found!"));

        return new OrderItemDiscountEntity(this.serviceRequest, productDiscountEntity);

    }
}
