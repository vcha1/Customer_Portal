package com.my1stle.customer.portal.web.dialect;

class ChronoCurrentUserLocalTimeProcessor extends ChronoCurrentUserZonedDateTimeProcessor {

    private final static String attributeName = "renderCurrentUserLocalTime";
    private final static String pattern = "hh:mm a";

    ChronoCurrentUserLocalTimeProcessor(String dialectPrefix) {
        super(dialectPrefix, attributeName);
    }

    @Override
    protected String pattern() {
        return pattern;
    }

}
