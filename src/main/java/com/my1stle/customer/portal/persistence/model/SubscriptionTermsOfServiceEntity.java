package com.my1stle.customer.portal.persistence.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

@Entity
@Table(name = "subscription_agreement")
public class SubscriptionTermsOfServiceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @OneToOne(mappedBy = "subscriptionTermsOfServiceEntity")
    private SubscriptionEntity subscription;

    @Column(name = "agreement_id", nullable = true)
    private String agreementId;

    @Column(name = "created_date", nullable = false)
    private ZonedDateTime createdDate;

    @Column(name = "last_modified_date", nullable = false)
    private ZonedDateTime lastModifiedDate;

    // Constructors
    public SubscriptionTermsOfServiceEntity() {

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

    public SubscriptionEntity getSubscription() {
        return subscription;
    }

    public String getAgreementId() {
        return agreementId;
    }

    public ZonedDateTime getCreatedDate() {
        return createdDate;
    }

    public ZonedDateTime getLastModifiedDate() {
        return lastModifiedDate;
    }

    // Setters
    public void setAgreementId(String agreementId) {
        this.agreementId = agreementId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof SubscriptionTermsOfServiceEntity)) return false;

        SubscriptionTermsOfServiceEntity that = (SubscriptionTermsOfServiceEntity) o;

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
                .append("subscription", subscription)
                .append("agreementId", agreementId)
                .append("createdDate", createdDate)
                .append("lastModifiedDate", lastModifiedDate)
                .toString();
    }
}
