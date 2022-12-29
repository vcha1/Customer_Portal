package com.my1stle.customer.portal.serviceImpl.order;

import com.my1stle.customer.portal.persistence.model.OrderEntity;
import com.my1stle.customer.portal.persistence.model.OrderItemDiscountEntity;
import com.my1stle.customer.portal.persistence.model.OrderItemEntity;
import com.my1stle.customer.portal.persistence.model.PaymentDetailEntity;
import com.my1stle.customer.portal.persistence.model.ProductDiscountEntity;
import com.my1stle.customer.portal.persistence.model.ProductEntity;
import com.my1stle.customer.portal.persistence.model.ServiceRequestEntity;
import com.my1stle.customer.portal.service.model.Order;
import com.my1stle.customer.portal.service.model.ServiceRequest;
import com.my1stle.customer.portal.service.order.OrderService;
import com.my1stle.customer.portal.service.pricing.ServiceRequestPricingService;
import com.my1stle.customer.portal.serviceImpl.model.OrderProxy;
import com.my1stle.customer.portal.web.exception.ResourceNotFoundException;
import org.baeldung.persistence.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class DefaultOrderService implements OrderService {

    private final ServiceRequestPricingService serviceRequestPricingService;
    private final JpaRepository<ServiceRequestEntity, Long> serviceRequestEntityLongJpaRepository;
    private final JpaRepository<ProductDiscountEntity, Long> productDiscountEntityLongJpaRepository;
    private final JpaRepository<OrderEntity, Long> orderEntityJpaRepository;

    @Autowired
    public DefaultOrderService(
            ServiceRequestPricingService serviceRequestPricingService,
            JpaRepository<ServiceRequestEntity, Long> serviceRequestEntityLongJpaRepository,
            JpaRepository<ProductDiscountEntity, Long> productDiscountEntityLongJpaRepository,
            JpaRepository<OrderEntity, Long> orderEntityJpaRepository) {
        this.serviceRequestPricingService = serviceRequestPricingService;
        this.serviceRequestEntityLongJpaRepository = serviceRequestEntityLongJpaRepository;
        this.productDiscountEntityLongJpaRepository = productDiscountEntityLongJpaRepository;
        this.orderEntityJpaRepository = orderEntityJpaRepository;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Order generate(ServiceRequest paidServiceRequest) {

        ServiceRequestEntity serviceRequestEntity = this.serviceRequestEntityLongJpaRepository
                .findById(paidServiceRequest.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Service Request Not Found!"));

        User user = serviceRequestEntity.getUser();
        ProductEntity product = serviceRequestEntity.getProduct();
        PaymentDetailEntity paymentDetail = serviceRequestEntity.getPaymentDetail();
        int quantity = serviceRequestEntity.getQuantity();

        if (quantity < 0) {
            throw new IllegalArgumentException("Expected Service Request's quantity to be greater than zero!");
        }

        Objects.requireNonNull(paymentDetail, "Payment Detail Was Excepted!");

        BigDecimal requiredDepositAmount = this.serviceRequestPricingService.getRequiredDepositAmount(paidServiceRequest);
        BigDecimal subTotal = this.serviceRequestPricingService.getSubTotalAmount(paidServiceRequest);
        BigDecimal amountDue = requiredDepositAmount.compareTo(BigDecimal.ZERO) > 0 ? requiredDepositAmount : subTotal;
        BigDecimal amountPaid = paymentDetail.getAmount();

        if (amountPaid.compareTo(amountDue) < 0) {
            throw new IllegalArgumentException("Amount paid is less than amount due!");
        }

        Set<OrderItemDiscountEntity> itemDiscounts = product.getDiscounts()
                .stream()
                .map(new ServiceRequestProductDiscountToOrderItemDiscountEntity(paidServiceRequest, productDiscountEntityLongJpaRepository))
                .collect(Collectors.toSet());

        OrderItemEntity orderItem = new OrderItemEntity(product, quantity, amountDue, itemDiscounts);
        orderItem.setServiceRequestEntity(serviceRequestEntity);

        OrderEntity order = new OrderEntity(
                user,
                amountDue,
                amountPaid,
                Collections.singletonList(orderItem),
                Collections.singletonList(paymentDetail)
        );

        order.setOrderDate(ZonedDateTime.now(ZoneOffset.UTC));

        OrderEntity savedOrder = this.orderEntityJpaRepository.save(order);

        return OrderProxy.from(savedOrder);

    }

}
