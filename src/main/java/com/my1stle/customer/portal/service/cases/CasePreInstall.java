package com.my1stle.customer.portal.service.cases;

import java.util.HashMap;
import java.util.Map;

@Deprecated
public final class CasePreInstall {

    public static final Map<String, ISSUE> ISSUE_MAP= new HashMap<String, ISSUE>(){{
        put(ISSUE.TAX_QUESTIONS.getViewName(), ISSUE.TAX_QUESTIONS);
        put(ISSUE.STATUS_OF_CONSTRUCTION.getViewName(), ISSUE.STATUS_OF_CONSTRUCTION);
        put(ISSUE.REQUEST_CONTACT_FROM_SALES_REP.getViewName(), ISSUE.REQUEST_CONTACT_FROM_SALES_REP);
        put(ISSUE.REQUEST_COPIES_OF_CONTRACT_DOCS.getViewName(), ISSUE.REQUEST_COPIES_OF_CONTRACT_DOCS);
        put(ISSUE.STATUS_OF_REIMBURSEMENT.getViewName(), ISSUE.STATUS_OF_REIMBURSEMENT);
        put(ISSUE.CANCELLATION.getViewName(), ISSUE.CANCELLATION);
        put(ISSUE.OTHER.getViewName(), ISSUE.OTHER);
    }};

    public enum ISSUE{
        TAX_QUESTIONS("Tax Questions", "Tax Questions"),
        STATUS_OF_CONSTRUCTION("Status of Construction", "Status of Construction"),
        REQUEST_COPIES_OF_CONTRACT_DOCS("Request copies of contract documents", "Request copies of contract documents"),
        REQUEST_CONTACT_FROM_SALES_REP("Request contact from Sales Representative", "Request contact from Sales Representative"),
        STATUS_OF_REIMBURSEMENT("Status of reimbursement", "Status of reimbursement"),
        CANCELLATION("Cancellation", "Cancellation"),
        OTHER("Other", "Other");



        private String viewName;
        private String idName;

        public String getViewName(){
            return this.viewName;
        }

        public String getIdName(){
            return this.idName;
        }


        private ISSUE(String idName,
                      String viewName){
            this.idName = idName;
            this.viewName = viewName;
        }
    }
}
