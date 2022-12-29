package com.my1stle.customer.portal.serviceImpl.model;

import com.my1stle.customer.portal.persistence.model.OrderItemEntity;
import com.my1stle.customer.portal.service.model.Order;
import com.my1stle.customer.portal.service.model.OrderItem;
import com.my1stle.customer.portal.service.model.OrderItemDiscount;
import com.my1stle.customer.portal.service.model.Product;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

class OrderItemProxy implements OrderItem {

    private Long id;
    private String description;
    private BigDecimal pricePerUom;
    private Integer quantity;
    private BigDecimal totalPrice;
    private Order order;
    private Product product;
    private Set<OrderItemDiscount> itemDiscounts;

    OrderItemProxy(Order order, OrderItemEntity entity) {

        Objects.requireNonNull(order, "Expected non-null order!");
        Objects.requireNonNull(entity, "Expected non-nul order item entity!");

        this.id = entity.getId();
        this.description = entity.getDescription();
        this.pricePerUom = entity.getPricePerUnit();
        this.quantity = entity.getQuantity();
        this.totalPrice = entity.getTotalPrice();
        this.order = order;
        this.product = entity.getProductEntity();
        this.itemDiscounts = entity.getOrderItemDiscounts()
                .stream()
                .map(OrderItemDiscountProxy::from)
                .collect(Collectors.toSet());

    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public BigDecimal getPricePerUnit() {
        return pricePerUom;
    }

    @Override
    public Integer getQuantity() {
        return quantity;
    }

    @Override
    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    @Override
    public Order getOrder() {
        return order;
    }

    @Override
    public Product getProduct() {
        return product;
    }

    @Override
    public Set<OrderItemDiscount> getOrderItemDiscounts() {
        return itemDiscounts;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.JSON_STYLE)
                .append("id", id)
                .append("description", description)
                .append("pricePerUom", pricePerUom)
                .append("quantity", quantity)
                .append("totalPrice", totalPrice)
                .append("order", order)
                .append("product", product)
                .append("itemDiscounts", itemDiscounts)
                .toString();
    }
}
