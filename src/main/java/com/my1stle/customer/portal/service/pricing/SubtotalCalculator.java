package com.my1stle.customer.portal.service.pricing;

import com.my1stle.customer.portal.service.model.Installation;
import com.my1stle.customer.portal.service.model.Product;
import com.my1stle.customer.portal.service.odoo.OdooInstallationData;

import java.math.BigDecimal;

public interface SubtotalCalculator {

    //BigDecimal getSubtotal(Installation installation, Product product, int quantity);

    BigDecimal getSubtotal(OdooInstallationData odooInstallationData, Product product, int quantity);

}
