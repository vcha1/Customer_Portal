package com.my1stle.customer.portal.service.model;

import com.my1stle.customer.portal.service.odoo.OdooInstallationData;
import org.baeldung.persistence.model.User;

import java.math.BigDecimal;

public interface ProductDiscount {

    long getId();

    Product getProduct();

    String getName();

    boolean getIsActive();

    String getDescription();

    //BigDecimal getTotalDiscountAmount(User user, Installation installation, int quantity);
    BigDecimal getTotalDiscountAmount(User user, OdooInstallationData odooInstallationData, int quantity);

}