package com.my1stle.customer.portal.service.pricing;

import com.my1stle.customer.portal.service.model.Installation;
import com.my1stle.customer.portal.service.model.Product;
import com.my1stle.customer.portal.service.odoo.OdooInstallationData;
import org.baeldung.persistence.model.User;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class DefaultProductDiscountCalculator implements ProductDiscountCalculator {
    /*
    @Override
    public BigDecimal getTotalDiscountAmount(User user, Installation installation, Product product, int productQuantity) {

        return product.getDiscounts()
                .stream()
                .map(discount -> discount.getTotalDiscountAmount(user, installation, productQuantity))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

    }

     */

    @Override
    public BigDecimal getTotalDiscountAmount(User user, OdooInstallationData odooInstallationData, Product product, int productQuantity) {

        return product.getDiscounts()
                .stream()
                .map(discount -> discount.getTotalDiscountAmount(user, odooInstallationData, productQuantity))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

    }
}
