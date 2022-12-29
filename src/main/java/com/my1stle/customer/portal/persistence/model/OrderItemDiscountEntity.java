package com.my1stle.customer.portal.persistence.model;

import com.my1stle.customer.portal.service.model.Installation;
import com.my1stle.customer.portal.service.model.ServiceRequest;
import com.my1stle.customer.portal.service.odoo.OdooInstallationData;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.baeldung.persistence.model.User;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.Optional;

@Entity
@Table(name = "order_item_discount")
public class OrderItemDiscountEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_item_id", nullable = false)
    private OrderItemEntity orderItem; // rename to order item

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @ManyToOne
    @JoinColumn(name = "product_discount_id", nullable = true)
    private ProductDiscountEntity productDiscountEntity;

    protected OrderItemDiscountEntity() {

    }

    public OrderItemDiscountEntity(String name, String description, BigDecimal amount) {

        Objects.requireNonNull(name);
        Objects.requireNonNull(description);
        Objects.requireNonNull(amount);

        this.name = name;
        this.description = description;
        this.amount = amount;
    }

    public OrderItemDiscountEntity(String name, String description, BigDecimal amount, ProductDiscountEntity productDiscountEntity) {
        this(name, description, amount);
        Objects.requireNonNull(productDiscountEntity);
        this.productDiscountEntity = productDiscountEntity;
    }

    public OrderItemDiscountEntity(ServiceRequest serviceRequest, ProductDiscountEntity productDiscountEntity) {

        User user = serviceRequest.getUser();
        //Installation installation = serviceRequest.getInstallation();
        OdooInstallationData odooInstallationData = serviceRequest.getOdooInstallationData();
        int quantity = serviceRequest.getQuantity();

        this.name = productDiscountEntity.getName();
        this.description = productDiscountEntity.getDescription();
        this.amount = productDiscountEntity.getTotalDiscountAmount(user, odooInstallationData, quantity);
        this.productDiscountEntity = productDiscountEntity;

    }

    // Getters
    public Long getId() {
        return id;
    }

    public OrderItemEntity getOrderItem() {
        return orderItem;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public Optional<ProductDiscountEntity> getProductDiscountEntity() {
        return Optional.ofNullable(productDiscountEntity);
    }

    // Setter
    void setOrderItem(OrderItemEntity orderItem) {
        this.orderItem = orderItem;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof OrderItemDiscountEntity)) return false;

        OrderItemDiscountEntity that = (OrderItemDiscountEntity) o;

        return new EqualsBuilder()
                .append(id, that.id)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(id)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("name", name)
                .append("description", description)
                .append("amount", amount)
                .append("productDiscountEntity", productDiscountEntity)
                .toString();
    }
}
