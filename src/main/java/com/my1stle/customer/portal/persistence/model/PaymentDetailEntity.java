package com.my1stle.customer.portal.persistence.model;

import com.my1stle.customer.portal.service.model.PaymentDetail;
import com.my1stle.customer.portal.service.model.PaymentMethod;
import com.my1stle.customer.portal.serviceImpl.model.PaymentMethodProxy;
import org.baeldung.persistence.model.User;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

@Entity
@Table(name = "payment_detail")
public class PaymentDetailEntity implements PaymentDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    @Column(nullable = false, name = "amount", precision = 10, scale = 2)
    private BigDecimal amount;

    @Column(nullable = false, name = "convenience_fee", precision = 10, scale = 2)
    private BigDecimal convenienceFee;

    @Column(nullable = false, name = "total", precision = 10, scale = 2)
    private BigDecimal total;

    @Column(nullable = false, name = "external_id")
    private String externalId;

    @OneToOne
    @JoinColumn(nullable = false, name = "payment_method_id")
    private PaymentMethodEntity paymentMethod;

    @Column(nullable = false, name = "created_date")
    private ZonedDateTime createdDate;

    public PaymentDetailEntity() {

    }

    public PaymentDetailEntity(User owner, PaymentMethodEntity paymentMethod, BigDecimal amount, BigDecimal convenienceFee, String externalId) {

        this.owner = owner;
        this.paymentMethod = paymentMethod;
        this.amount = amount;
        this.convenienceFee = convenienceFee;
        this.externalId = externalId;

        this.total = amount.add(convenienceFee);

    }

    // Pre DML state operations
    @PrePersist
    private void prePersist() {
        ZonedDateTime now = ZonedDateTime.now(ZoneOffset.UTC);
        this.createdDate = now;
    }

    // Getters
    @Override
    public Long getId() {
        return id;
    }

    @Override
    public User getOwner() {
        return owner;
    }

    @Override
    public BigDecimal getAmount() {
        return amount;
    }

    @Override
    public BigDecimal getConvenienceFee() {
        return convenienceFee;
    }

    @Override
    public BigDecimal getTotal() {
        return total;
    }

    @Override
    public String getExternalId() {
        return externalId;
    }

    @Override
    public PaymentMethod getPaymentMethod() {
        return PaymentMethodProxy.from(paymentMethod);
    }

    @Override
    public ZonedDateTime getCreatedDate() {
        return createdDate;
    }

    // Setters
    public void setOwner(User owner) {
        this.owner = owner;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public void setConvenienceFee(BigDecimal convenienceFee) {
        this.convenienceFee = convenienceFee;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public void setPaymentMethod(PaymentMethodEntity paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

}
