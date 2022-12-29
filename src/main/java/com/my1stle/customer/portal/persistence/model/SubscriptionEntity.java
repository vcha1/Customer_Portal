package com.my1stle.customer.portal.persistence.model;

import com.my1stle.customer.portal.service.model.Installation;
import com.my1stle.customer.portal.service.odoo.OdooInstallationData;
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
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.math.BigDecimal;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "subscription",
        uniqueConstraints =
        @UniqueConstraint(columnNames = {"product_id", "installation_id"}))
public class SubscriptionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    @Column(name = "price", nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private ProductEntity product;

    @Column(name = "installation_id", nullable = false)
    private String installationId;

    @OneToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "subscription_agreement_id", nullable = false)
    private SubscriptionTermsOfServiceEntity subscriptionTermsOfServiceEntity;

    @Column(name = "active", columnDefinition = "boolean default false", nullable = false)
    private Boolean active = false;

    @Column(name = "starting_date", nullable = true)
    private ZonedDateTime startingDate;

    @Column(name = "expiration_date", nullable = true)
    private ZonedDateTime expirationDate;

    @OneToMany
    @JoinTable(
            name = "subscription_order",
            joinColumns = @JoinColumn(name = "subscription_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "order_id", referencedColumnName = "id")
    )
    private List<OrderEntity> orders = new ArrayList<>();

    @Column(name = "created_date", nullable = false)
    private ZonedDateTime createdDate;

    @Column(name = "last_modified_date", nullable = false)
    private ZonedDateTime lastModifiedDate;

    // Constructors;
    protected SubscriptionEntity() {

    }

    public SubscriptionEntity(
            User owner,
            ProductEntity selectedProduct,
//            Installation selectedInstallation,
            OdooInstallationData selectedInstallation,
            SubscriptionTermsOfServiceEntity subscriptionTermsOfServiceEntity) {
        this.owner = owner;
        this.product = selectedProduct;
        this.installationId = selectedInstallation.getId().toString();
        this.subscriptionTermsOfServiceEntity = subscriptionTermsOfServiceEntity;
        this.price = selectedProduct.getPricingType().calculation().apply(selectedProduct, selectedInstallation);
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

        if (null != expirationDate) {
            this.expirationDate = this.expirationDate.withZoneSameInstant(ZoneOffset.UTC);
        }

        if (null != startingDate) {
            this.startingDate = this.startingDate.withZoneSameInstant(ZoneOffset.UTC);
        }

        if (null != expirationDate) {
            this.expirationDate = this.expirationDate.withZoneSameInstant(ZoneOffset.UTC);
        }
    }

    // Getters
    public Long getId() {
        return id;
    }

    public User getOwner() {
        return owner;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public ProductEntity getProduct() {
        return product;
    }

    public String getInstallationId() {
        return installationId;
    }

    public SubscriptionTermsOfServiceEntity getSubscriptionTermsOfServiceEntity() {
        return subscriptionTermsOfServiceEntity;
    }

    public Boolean getActive() {
        return active;
    }

    public ZonedDateTime getCreatedDate() {
        return createdDate;
    }

    public ZonedDateTime getLastModifiedDate() {
        return lastModifiedDate;
    }

    public ZonedDateTime getStartingDate() {
        return startingDate;
    }

    public ZonedDateTime getExpirationDate() {
        return expirationDate;
    }

    public List<OrderEntity> getOrders() {
        return orders;
    }

    // Setters
    public void setOwner(User owner) {
        this.owner = owner;
    }

    public void setProduct(ProductEntity product) {
        this.product = product;
    }

    public void setInstallationId(String installationId) {
        this.installationId = installationId;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public void setStartingDate(ZonedDateTime startingDate) {
        this.startingDate = startingDate;
    }

    public void setExpirationDate(ZonedDateTime expirationDate) {
        this.expirationDate = expirationDate;
    }

    public void addOrder(OrderEntity order) {
        this.orders.add(order);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof SubscriptionEntity)) return false;

        SubscriptionEntity that = (SubscriptionEntity) o;

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
                .append("owner", owner)
                .append("price", price)
                .append("product", product)
                .append("installationId", installationId)
                .append("subscriptionTermsOfServiceEntity", subscriptionTermsOfServiceEntity)
                .append("activated", active)
                .append("startingDate", startingDate)
                .append("expirationDate", expirationDate)
                .append("orders", orders)
                .append("createdDate", createdDate)
                .append("lastModifiedDate", lastModifiedDate)
                .toString();
    }
}
