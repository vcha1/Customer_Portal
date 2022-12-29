package com.my1stle.customer.portal.service.cases;

import java.util.HashMap;
import java.util.Map;

public final class CaseStatus{

    public static final Map<String, STATUS> STATUS_MAP= new HashMap<String, STATUS>(){{
        put(STATUS.NEW.getViewName(), STATUS.NEW);
        put(STATUS.IN_PROGRESS.getViewName(), STATUS.IN_PROGRESS);
        put(STATUS.RMA.getViewName(), STATUS.RMA);
        put(STATUS.ESCALATED.getViewName(), STATUS.ESCALATED);
        put(STATUS.SCHEDULING.getViewName(), STATUS.SCHEDULING);
        put(STATUS.SCHEDULED.getViewName(), STATUS.SCHEDULED);
        put(STATUS.RESOLUTION.getViewName(), STATUS.RESOLUTION);
        put(STATUS.BILLING.getViewName(), STATUS.BILLING);
        put(STATUS.CLOSED.getViewName(), STATUS.CLOSED);
        put(STATUS.CANCELLED.getViewName(), STATUS.CANCELLED);
        put(STATUS.SE_TRANSFER.getViewName(), STATUS.SE_TRANSFER);
    }};

    public static enum STATUS{
        NEW(1L, "New"),
        IN_PROGRESS(2L, "In Progress"),
        RMA(3L, "RMA"),
        ESCALATED(4L, "ESCALATED"),
        SCHEDULING(5L, "Scheduling"),
        SCHEDULED(6L, "Scheduled"),
        RESOLUTION(7L, "Resolution"),
        BILLING(8L, "Billing"),
        CLOSED(9L, "Closed"),
        CANCELLED(10L, "Cancelled"),
        SE_TRANSFER(11L, "SE Transfer");


        private Long id;
        private String viewName;

        public Long getId() {
            return this.id;
        }

        public String getViewName() {
            return this.viewName;
        }

        private STATUS(
                Long id,
                String viewName
        ) {
            this.id = id;
            this.viewName = viewName;
        }


    }
}
