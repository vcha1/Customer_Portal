package com.my1stle.customer.portal.service.pricing;

import com.my1stle.customer.portal.service.model.Installation;
import com.my1stle.customer.portal.service.model.Product;
import com.my1stle.customer.portal.service.odoo.OdooInstallationData;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class DefaultSubtotalCalculator implements SubtotalCalculator {

    /*
    @Override
    public BigDecimal getSubtotal(Installation installation, Product product, int quantity) {

        return product.getPricingType()
                .calculation()
                .apply(product, installation)
                .multiply(BigDecimal.valueOf(quantity));


    }
    */

    @Override
    public BigDecimal getSubtotal(OdooInstallationData odooInstallationData, Product product, int quantity) {

        return product.getPricingType()
                .calculation()
                .apply(product, odooInstallationData)
                .multiply(BigDecimal.valueOf(quantity));


    }

}
