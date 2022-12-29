package com.my1stle.customer.portal.serviceImpl.model;

import com.my1stle.customer.portal.persistence.model.OrderEntity;
import com.my1stle.customer.portal.service.model.Order;
import com.my1stle.customer.portal.service.model.OrderItem;
import com.my1stle.customer.portal.service.model.PaymentDetail;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.baeldung.persistence.model.User;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class OrderProxy implements Order {

    private Long id;
    private User user;
    private BigDecimal amountDue;
    private BigDecimal amountPaid;
    private ZonedDateTime orderDate;
    private List<OrderItem> orderItems;
    private List<PaymentDetail> paymentDetails;

    private OrderProxy() {

    }

    public static OrderProxy from(OrderEntity entity) {

        Objects.requireNonNull("Expected non-null order entity!");

        OrderProxy proxy = new OrderProxy();

        proxy.id = entity.getId();
        proxy.user = entity.getPayer();
        proxy.amountDue = entity.getAmountDue();
        proxy.amountPaid = entity.getAmountDue();
        proxy.orderDate = entity.getOrderDate();
        proxy.orderItems = entity.getOrderItems()
                .stream()
                .map(item -> new OrderItemProxy(proxy, item))
                .collect(Collectors.toList());
        proxy.paymentDetails = new ArrayList<>(entity.getPaymentEntries());

        return proxy;

    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public User getUser() {
        return user;
    }

    @Override
    public ZonedDateTime getOrderDate() {
        return orderDate;
    }

    @Override
    public BigDecimal getAmountDue() {
        return amountDue;
    }

    @Override
    public BigDecimal getAmountPaid() {
        return amountPaid;
    }

    @Override
    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    @Override
    public List<PaymentDetail> getPaymentDetails() {
        return paymentDetails;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.JSON_STYLE)
                .append("id", id)
                .append("amountDue", amountDue)
                .append("amountPaid", amountPaid)
                .append("orderDate", orderDate)
                .append("orderItems", orderItems)
                .append("paymentDetails", paymentDetails)
                .toString();
    }
}
