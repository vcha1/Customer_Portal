package com.my1stle.customer.portal.service.cases;

import java.util.HashMap;
import java.util.Map;

public final class CaseBilling {

    public static final Map<String, STATUS> BILLING_MAP= new HashMap<String, STATUS>(){{
       put(STATUS.X1ST_LIGHT_WORKMANSHIP.getViewName(), STATUS.X1ST_LIGHT_WORKMANSHIP) ;
       put(STATUS.BILL_CUSTOMER.getViewName(), STATUS.BILL_CUSTOMER) ;
       put(STATUS.GIFT.getViewName(), STATUS.GIFT) ;
       put(STATUS.NO_CHARGE.getViewName(), STATUS.NO_CHARGE) ;
       put(STATUS.REFERRAL_GIFT.getViewName(), STATUS.REFERRAL_GIFT) ;
       put(STATUS.WARRANTY_CLAIM_MANUFACTURER.getViewName(), STATUS.WARRANTY_CLAIM_MANUFACTURER) ;
    }};
    public enum STATUS{

        X1ST_LIGHT_WORKMANSHIP("1st Light Workmapship"),
        BILL_CUSTOMER("Bill Customer"),
        GIFT("Gift"),
        NO_CHARGE("No Charge"),
        REFERRAL_GIFT("Referral Gift"),
        WARRANTY_CLAIM_MANUFACTURER("Warranty Claim Manufacturer");

        private String viewName;

        public String getViewName(){
            return this.viewName;
        }

        private STATUS(String viewName){
           this.viewName = viewName;
        }
    }
}
