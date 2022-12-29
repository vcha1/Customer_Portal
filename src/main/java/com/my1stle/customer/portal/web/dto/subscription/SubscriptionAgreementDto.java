package com.my1stle.customer.portal.web.dto.subscription;

import org.baeldung.persistence.model.User;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class SubscriptionAgreementDto {

    @NotNull
    private Long productId;

    @NotNull
    @NotBlank
    private String installationId;

    private User user;

    public SubscriptionAgreementDto() {

    }

    // Getters
    public Long getProductId() {
        return productId;
    }

    public String getInstallationId() {
        return installationId;
    }

    public User getUser() {
        return user;
    }

    // Setters
    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public void setInstallationId(String installationId) {
        this.installationId = installationId;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
