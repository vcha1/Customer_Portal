package com.my1stle.customer.portal.service.pricing;

import com.my1stle.customer.portal.service.model.Installation;
import com.my1stle.customer.portal.service.model.Product;
import com.my1stle.customer.portal.service.odoo.OdooInstallationData;
import org.baeldung.persistence.model.User;

import java.math.BigDecimal;

public interface ProductDiscountCalculator {

    BigDecimal getTotalDiscountAmount(User user, OdooInstallationData odooInstallationData, Product product, int productQuantity);

}
