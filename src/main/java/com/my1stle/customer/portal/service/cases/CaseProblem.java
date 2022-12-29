package com.my1stle.customer.portal.service.cases;


import java.util.HashMap;
import java.util.Map;

public final class CaseProblem {
    public static final Map<String, PROBLEM> PROBLEM_MAP = new HashMap<String, PROBLEM>(){{
       put(PROBLEM.NEW_SERVICE.getViewName(), PROBLEM.NEW_SERVICE);
        put(PROBLEM.CUSTOMER_QUESTION.getViewName(), PROBLEM.CUSTOMER_QUESTION);
        put(PROBLEM.PRODUCT_RECALL.getViewName(), PROBLEM.PRODUCT_RECALL);
        put(PROBLEM.REBATE.getViewName(), PROBLEM.REBATE);
        put(PROBLEM.CLEAN_UP.getViewName(), PROBLEM.CLEAN_UP);
        put(PROBLEM.REPAIR_ELECTRICAL_PROBLEM.getViewName(), PROBLEM.REPAIR_ELECTRICAL_PROBLEM);
        put(PROBLEM.REPAIR_INSTALLATION_ISSUE.getViewName(), PROBLEM.REPAIR_INSTALLATION_ISSUE);
        put(PROBLEM.REPAIR_INTERIOR.getViewName(), PROBLEM.REPAIR_INTERIOR);
        put(PROBLEM.REPAIR_INVERTER_ISSUE.getViewName(), PROBLEM.REPAIR_INVERTER_ISSUE);
        put(PROBLEM.REPAIR_MONITOR_ISSUE.getViewName(), PROBLEM.REPAIR_MONITOR_ISSUE);
        put(PROBLEM.REPAIR_PANEL_ISSUE.getViewName(), PROBLEM.REPAIR_PANEL_ISSUE);
        put(PROBLEM.REPAIR_ROOF_LEAK.getViewName(), PROBLEM.REPAIR_ROOF_LEAK);
        put(PROBLEM.SYSTEM_CHECK.getViewName(), PROBLEM.SYSTEM_CHECK);
        put(PROBLEM.SYSTEM_CLEANING.getViewName(), PROBLEM.SYSTEM_CLEANING);
        put(PROBLEM.SYSTEM_CLEANING_AND_CHECK.getViewName(), PROBLEM.SYSTEM_CLEANING_AND_CHECK);
        put(PROBLEM.POST_INSTALL_PRE_PTO_ISSUE.getViewName(), PROBLEM.POST_INSTALL_PRE_PTO_ISSUE);
        put(PROBLEM.POST_PTO_PRE_REBATE_ISSUE.getViewName(), PROBLEM.POST_PTO_PRE_REBATE_ISSUE);
        put(PROBLEM.MISSING_PHOTOS_DOCS.getViewName(), PROBLEM.MISSING_PHOTOS_DOCS);
        put(PROBLEM.BILLABLE_SERVICE.getViewName(), PROBLEM.BILLABLE_SERVICE);
    }};
    public enum PROBLEM {

        NEW_SERVICE(1L, "New Service"),
        CUSTOMER_QUESTION(2L, "Customer Question"),
        PRODUCT_RECALL(3L, "Product Recall"),
        REBATE(4L, "Rebate"),
        CLEAN_UP(5L, "Clean Up"),
        REPAIR_ELECTRICAL_PROBLEM(6L, "Repair: Electrical Problem"),
        REPAIR_INSTALLATION_ISSUE(7L, "Repair: Installation Issue"),
        REPAIR_INVERTER_ISSUE(8L, "Repair: Inverter Issue"),
        REPAIR_MONITOR_ISSUE(9L, "Repair: Monitor Issue"),
        REPAIR_PANEL_ISSUE(10L, "Repair: Panel Issue"),
        REPAIR_ROOF_LEAK(11L, "Repair: Roof Leak"),
        SYSTEM_CHECK(12L, "System Check"),
        SYSTEM_CLEANING(13L, "System Cleaning"),
        SYSTEM_CLEANING_AND_CHECK(14L, "System Cleaning and Check"),
        POST_PTO_PRE_REBATE_ISSUE(15L, "Post-PTO,Pre-Rebate Issue"),
        POST_INSTALL_PRE_PTO_ISSUE(16L, "Post-Install, Pre-PTO Issue"),
        MISSING_PHOTOS_DOCS(17L, "Missing Photos/Docs"),
        BILLABLE_SERVICE(18L, "Billable Service"),
        REPAIR_INTERIOR(19L, "Repair: Interior");


        private Long id;
        private String viewName;

        public Long getId() {
            return this.id;
        }

        public String getViewName() {
            return this.viewName;
        }

        private PROBLEM(
                Long id,
                String viewName
        ) {
            this.id = id;
            this.viewName = viewName;
        }

    }
}
