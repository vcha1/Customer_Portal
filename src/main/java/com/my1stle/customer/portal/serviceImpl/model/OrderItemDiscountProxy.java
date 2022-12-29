package com.my1stle.customer.portal.serviceImpl.model;

import com.my1stle.customer.portal.persistence.model.OrderItemDiscountEntity;
import com.my1stle.customer.portal.persistence.model.ProductDiscountEntity;
import com.my1stle.customer.portal.service.model.OrderItemDiscount;
import com.my1stle.customer.portal.service.model.ProductDiscount;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.Optional;

class OrderItemDiscountProxy implements OrderItemDiscount {

    private String name;
    private String description;
    private BigDecimal amount;
    private ProductDiscountEntity productDiscount;

    private OrderItemDiscountProxy() {

    }

    static OrderItemDiscountProxy from(OrderItemDiscountEntity orderItemDiscountEntity) {

        Objects.requireNonNull(orderItemDiscountEntity, "Expected non-null order item discount entity!");

        OrderItemDiscountProxy proxy = new OrderItemDiscountProxy();

        proxy.name = orderItemDiscountEntity.getName();
        proxy.description = orderItemDiscountEntity.getDescription();
        proxy.amount = orderItemDiscountEntity.getAmount();
        proxy.productDiscount = orderItemDiscountEntity.getProductDiscountEntity().orElse(null);

        return proxy;

    }


    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public BigDecimal getAmount() {
        return amount;
    }

    @Override
    public Optional<ProductDiscount> getProductDiscount() {
        return Optional.ofNullable(productDiscount);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.JSON_STYLE)
                .append("name", name)
                .append("description", description)
                .append("amount", amount)
                .append("productDiscount", productDiscount)
                .toString();
    }
}
