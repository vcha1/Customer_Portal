package com.my1stle.customer.portal.web.dto.contactus;

import com.my1stle.customer.portal.service.contactus.ContactUsReason;
import org.baeldung.validation.ValidEmail;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class ContactUsDto {

    @NotNull
    private String firstName;

    @NotNull
    private String lastName;

    @ValidEmail
    @NotNull
    private String email;

    @NotNull
    private String phone;

    @NotNull
    private ContactUsReason reason;

    @NotNull
    private Long productId;

    @NotNull
    private String installationId;

    @NotNull
    @Size(max = 255, message = "Exceeded message limit size")
    private String message;

    public ContactUsDto() {

    }

    // Getters
    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public ContactUsReason getReason() {
        return reason;
    }

    public Long getProductId() {
        return productId;
    }

    public String getInstallationId() {
        return installationId;
    }

    public String getMessage() {
        return message;
    }

    // Setters
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setReason(ContactUsReason reason) {
        this.reason = reason;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public void setInstallationId(String installationId) {
        this.installationId = installationId;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}