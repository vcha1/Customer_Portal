package com.my1stle.customer.portal.persistence.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Entity
@Table(name = "order_item")
public class OrderItemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "price_per_unit", nullable = false)
    private BigDecimal pricePerUnit;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "total_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalPrice;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "order_id", nullable = false)
    private OrderEntity order;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id", nullable = false)
    private ProductEntity productEntity;

    @Column(name = "created_date", nullable = false)
    private ZonedDateTime createdDate;

    @Column(name = "last_modified_date", nullable = false)
    private ZonedDateTime lastModifiedDate;

    @OneToMany(
            mappedBy = "orderItem",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private Set<OrderItemDiscountEntity> orderItemDiscounts = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "service_request_id", nullable = true)
    private ServiceRequestEntity serviceRequestEntity;

    protected OrderItemEntity() {

    }

    public OrderItemEntity(ProductEntity productEntity, Integer quantity, BigDecimal totalPrice) {
        this.productEntity = productEntity;
        this.description = productEntity.getName();
        this.pricePerUnit = productEntity.getPricePerUnit();
        this.quantity = quantity;
        this.totalPrice = totalPrice;
    }

    public OrderItemEntity(ProductEntity productEntity, Integer quantity, BigDecimal totalPrice, Set<OrderItemDiscountEntity> orderItemDiscounts) {

        this(productEntity, quantity, totalPrice);

        for (OrderItemDiscountEntity itemDiscountEntity : orderItemDiscounts) {
            itemDiscountEntity.setOrderItem(this);
        }

        this.orderItemDiscounts = orderItemDiscounts;

    }

    // Pre DML state operations
    @PrePersist
    public void prePersist() {
        ZonedDateTime now = ZonedDateTime.now(ZoneOffset.UTC);
        this.createdDate = now;
        this.lastModifiedDate = now;

    }

    @PreUpdate
    public void preUpdate() {
        this.lastModifiedDate = ZonedDateTime.now(ZoneOffset.UTC);
    }

    // Getters
    public Long getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public BigDecimal getPricePerUnit() {
        return pricePerUnit;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public OrderEntity getOrder() {
        return order;
    }

    public ProductEntity getProductEntity() {
        return productEntity;
    }

    public Set<OrderItemDiscountEntity> getOrderItemDiscounts() {
        return orderItemDiscounts;
    }

    public Optional<ServiceRequestEntity> getServiceRequestEntity() {
        return Optional.ofNullable(serviceRequestEntity);
    }

    // Setter
    public void setOrder(OrderEntity order) {
        this.order = order;
    }

    public void setServiceRequestEntity(ServiceRequestEntity serviceRequestEntity) {
        this.serviceRequestEntity = serviceRequestEntity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof OrderItemEntity)) return false;

        OrderItemEntity that = (OrderItemEntity) o;

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
                .append("description", description)
                .append("pricePerUnit", pricePerUnit)
                .append("quantity", quantity)
                .append("totalPrice", totalPrice)
                .append("order", order)
                .append("productEntity", productEntity)
                .append("createdDate", createdDate)
                .append("lastModifiedDate", lastModifiedDate)
                .toString();
    }
}