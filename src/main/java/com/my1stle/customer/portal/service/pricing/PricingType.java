package com.my1stle.customer.portal.service.pricing;

import com.my1stle.customer.portal.service.model.Installation;
import com.my1stle.customer.portal.service.model.Product;
import com.my1stle.customer.portal.service.odoo.OdooInstallationData;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

public enum PricingType {

    FIXED {
        @Override
        public String label() {
            return "each";
        }

        //@Override
        //public BiFunction<Product, Installation, BigDecimal> calculation() {
        //    return pricingStrategies.computeIfAbsent(FIXED, k -> (product, installation) -> product.getPricePerUnit());
        //}


        @Override
        public BiFunction<Product, OdooInstallationData, BigDecimal> calculation() {
            return pricingStrategies.computeIfAbsent(FIXED, k -> (product, installation) -> product.getPricePerUnit());
        }
    },

    PER_PANEL {
        @Override
        public String label() {
            return "per panel";
        }
        /*
        @Override
        public BiFunction<Product, Installation, BigDecimal> calculation() {
            return pricingStrategies.computeIfAbsent(PER_PANEL, k -> (product, installation) ->
                    product.getPricePerUnit().multiply(installation.getPanelCountFromItems() == null ? BigDecimal.ZERO : BigDecimal.valueOf(installation.getPanelCountFromItems())));


        }

         */

        //No longer being used so I am setting it to automatically 0
        Integer panelCount = 0;
        @Override
        public BiFunction<Product, OdooInstallationData, BigDecimal> calculation() {
            return pricingStrategies.computeIfAbsent(PER_PANEL, k -> (product, installation) ->
                    product.getPricePerUnit().multiply(panelCount == null ? BigDecimal.ZERO : BigDecimal.valueOf(panelCount)));


        }


    };

    //public abstract BiFunction<Product, Installation, BigDecimal> calculation();
    public abstract BiFunction<Product, OdooInstallationData, BigDecimal> calculation();

    public abstract String label();

    //private static final Map<PricingType, BiFunction<Product, Installation, BigDecimal>> pricingStrategies = new HashMap<>();
    private static final Map<PricingType, BiFunction<Product, OdooInstallationData, BigDecimal>> pricingStrategies = new HashMap<>();

}