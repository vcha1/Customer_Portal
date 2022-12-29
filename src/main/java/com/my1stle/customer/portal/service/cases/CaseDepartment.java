package com.my1stle.customer.portal.service.cases;

import java.util.HashMap;
import java.util.Map;

public final class CaseDepartment{
    public static final Map<String, DEPARTMENT> DEPARTMENT_MAP= new HashMap<String, DEPARTMENT>(){{
        put(DEPARTMENT.ACCOUNTS_PAYABLE.getViewName(), DEPARTMENT.ACCOUNTS_PAYABLE);
        put(DEPARTMENT.ACCOUNTS_RECEIVABLE.getViewName(), DEPARTMENT.ACCOUNTS_RECEIVABLE);
        put(DEPARTMENT.COMMERCIAL.getViewName(), DEPARTMENT.COMMERCIAL);
        put(DEPARTMENT.DESIGN.getViewName(), DEPARTMENT.DESIGN);
        put(DEPARTMENT.FINANCES.getViewName(), DEPARTMENT.FINANCES);
        put(DEPARTMENT.LIGHTING_CONVERSATION.getViewName(), DEPARTMENT.LIGHTING_CONVERSATION);
        put(DEPARTMENT.OPERATIONS.getViewName(), DEPARTMENT.OPERATIONS);
        put(DEPARTMENT.SALES.getViewName(), DEPARTMENT.SALES);
        put(DEPARTMENT.S_RECS.getViewName(), DEPARTMENT.S_RECS);
        put(DEPARTMENT.TECHNICAL_SERVICE.getViewName(), DEPARTMENT.TECHNICAL_SERVICE);
        put(DEPARTMENT.REBATES.getViewName(), DEPARTMENT.REBATES);
    }};
    public enum DEPARTMENT {
        ACCOUNTS_PAYABLE("Accounts Payable", "Accounts Payable"),
        ACCOUNTS_RECEIVABLE("Accounts Receivable", "Accounts Receivable"),
        COMMERCIAL("Commercial", "Commercial"),
        DESIGN("Design", "Design"),
        FINANCES("Finances", "Finances"),
        LIGHTING_CONVERSATION("Lighting Conversation", "Lighting Conversation"),
        OPERATIONS("Operations", "Operations"),
        SALES("Sales", "Sales"),
        S_RECS("S-RECs", "S-RECs"),
        TECHNICAL_SERVICE("Technical/Service", "Technical/Service"),
        REBATES("Rebates", "Rebates");

        private String idName;
        private String viewName;

        public String getIdName(){
            return this.idName;
        }

        public String getViewName(){
            return this.viewName;
        }

        private DEPARTMENT(
                String idName,
                String viewName
        ){
            this.idName = idName;
            this.viewName = viewName;
        }

    }
}
