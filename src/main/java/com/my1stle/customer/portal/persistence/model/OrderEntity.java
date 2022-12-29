package com.my1stle.customer.portal.persistence.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.baeldung.persistence.model.User;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @see <a href="https://thoughts-on-java.org/hibernate-tips-escape-table-column-names/">Hibernate Tips: How to escape table and column names</a>
 */
@Entity
@Table(name = "\"order\"") // order is a reserved keyword will cause a SQL synatx error.
public class OrderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "payer_id", nullable = false)
    private User payer;

    @Column(name = "amount_due", nullable = false, precision = 10, scale = 2)
    private BigDecimal amountDue;

    @Column(name = "amount_paid", nullable = false, precision = 10, scale = 2)
    private BigDecimal amountPaid;

    @OneToMany(
            mappedBy = "order",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<OrderItemEntity> orderItems = new ArrayList<>();

    @OneToMany(cascade = CascadeType.PERSIST)
    @JoinTable(
            name = "order_payment_detail",
            joinColumns = @JoinColumn(name = "order_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "payment_detail_id", referencedColumnName = "id")
    )
    private List<PaymentDetailEntity> paymentEntries = new ArrayList<>();

    @Column(name = "order_date", nullable = false)
    private ZonedDateTime orderDate;

    @Column(name = "created_date", nullable = false)
    private ZonedDateTime createdDate;

    @Column(name = "last_modified_date", nullable = false)
    private ZonedDateTime lastModifiedDate;

    protected OrderEntity() {

    }

    public OrderEntity(
            User payer, BigDecimal amountDue,
            BigDecimal amountPaid,
            List<OrderItemEntity> orderItems,
            List<PaymentDetailEntity> paymentEntries) {

        this.payer = payer;
        this.amountDue = amountDue;
        this.amountPaid = amountPaid;

        for (OrderItemEntity item : orderItems) {
            addOrderItem(item);
        }

        for (PaymentDetailEntity paymentEntry : paymentEntries) {
            addPaymentEntry(paymentEntry);
        }
    }

    // Pre DML state operations
    @PrePersist
    public void prePersist() {
        ZonedDateTime now = ZonedDateTime.now(ZoneOffset.UTC);
        this.createdDate = now;
        this.lastModifiedDate = now;

        if (null != orderDate) {
            this.orderDate = this.orderDate.withZoneSameInstant(ZoneOffset.UTC);
        }

    }

    @PreUpdate
    public void preUpdate() {

        this.lastModifiedDate = ZonedDateTime.now(ZoneOffset.UTC);

        if (null != orderDate) {
            this.orderDate = this.orderDate.withZoneSameInstant(ZoneOffset.UTC);
        }
    }

    // Getters
    public Long getId() {
        return id;
    }

    public User getPayer() {
        return payer;
    }

    public BigDecimal getAmountDue() {
        return amountDue;
    }

    public BigDecimal getAmountPaid() {
        return amountPaid;
    }

    public List<OrderItemEntity> getOrderItems() {
        return orderItems;
    }

    public List<PaymentDetailEntity> getPaymentEntries() {
        return paymentEntries;
    }

    public ZonedDateTime getOrderDate() {
        return orderDate;
    }

    public ZonedDateTime getCreatedDate() {
        return createdDate;
    }

    public ZonedDateTime getLastModifiedDate() {
        return lastModifiedDate;
    }

    // Setters
    public void setOrderDate(ZonedDateTime orderDate) {
        this.orderDate = orderDate;
    }

    public void setAmountDue(BigDecimal amountDue) {
        this.amountDue = amountDue;
    }

    public void setAmountPaid(BigDecimal amountPaid) {
        this.amountPaid = amountPaid;
    }

    private void addOrderItem(OrderItemEntity orderItem) {
        this.orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    private void addPaymentEntry(PaymentDetailEntity paymentEntry) {
        this.paymentEntries.add(paymentEntry);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof OrderEntity)) return false;

        OrderEntity that = (OrderEntity) o;

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
                .append("amountDue", amountDue)
                .append("amountPaid", amountPaid)
                .append("orderItems", orderItems)
                .append("paymentEntries", paymentEntries)
                .append("createdDate", createdDate)
                .append("lastModifiedDate", lastModifiedDate)
                .toString();
    }
}