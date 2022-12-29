package com.my1stle.customer.portal.web.dialect;

class ChronoCurrentUserDateProcessor extends ChronoCurrentUserZonedDateTimeProcessor {

    private final static String attributeName = "renderCurrentUserLocalDate";
    private final static String pattern = "MM/dd/yyyy";

    ChronoCurrentUserDateProcessor(String dialectPrefix) {
        super(dialectPrefix, attributeName);
    }

    @Override
    protected String pattern() {
        return pattern;
    }

}
