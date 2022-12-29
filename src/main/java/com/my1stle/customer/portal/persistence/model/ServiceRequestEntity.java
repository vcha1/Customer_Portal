package com.my1stle.customer.portal.persistence.model;


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
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.time.ZonedDateTime;

@Entity
@Table(name = "service_request")
public class ServiceRequestEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private ProductEntity product;

    @Column(name = "quantity", nullable = false, columnDefinition = "integer default 1")
    private int quantity = 1;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "calendar_resource_id", nullable = true)
    private Long calendarResourceId;

    @Column(name = "start_time", nullable = true)
    private ZonedDateTime startTime;

    @Column(name = "installation_id", nullable = false)
    private String installationId;

    @OneToOne
    @JoinColumn(name = "payment_detail_id", nullable = true, unique = true)
    private PaymentDetailEntity paymentDetail;

    @Column(name = "event_id")
    private Long eventId;

    @Column(name = "customer_notes")
    private String customerNotes;

    public ServiceRequestEntity() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ProductEntity getProduct() {
        return product;
    }

    public void setProduct(ProductEntity product) {
        this.product = product;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Long getCalendarResourceId() {
        return calendarResourceId;
    }

    public void setCalendarResourceId(Long calendarResourceId) {
        this.calendarResourceId = calendarResourceId;
    }

    public ZonedDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(ZonedDateTime startTime) {
        this.startTime = startTime;
    }

    public String getInstallationId() {
        return installationId;
    }

    public void setInstallationId(String installationId) {
        this.installationId = installationId;
    }

    public PaymentDetailEntity getPaymentDetail() {
        return paymentDetail;
    }

    public void setPaymentDetail(PaymentDetailEntity paymentDetail) {
        this.paymentDetail = paymentDetail;
    }

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public String getCustomerNotes() {
        return customerNotes;
    }

    public void setCustomerNotes(String customerNotes) {
        this.customerNotes = customerNotes;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;

        if (!(o instanceof ServiceRequestEntity)) return false;

        ServiceRequestEntity that = (ServiceRequestEntity) o;

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
                .append("product", product)
                .append("quantity", quantity)
                .append("user", user)
                .append("calendarResourceId", calendarResourceId)
                .append("startTime", startTime)
                .append("installationId", installationId)
                .append("paymentDetail", paymentDetail)
                .append("eventId", eventId)
                .append("customerNotes", customerNotes)
                .toString();
    }
}
