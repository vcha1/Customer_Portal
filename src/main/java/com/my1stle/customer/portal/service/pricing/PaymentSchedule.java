package com.my1stle.customer.portal.service.pricing;

import java.time.ZonedDateTime;

public enum PaymentSchedule {

    YEARLY {
        @Override
        public String getLabel() {
            return "Yearly";
        }

        @Override
        public ZonedDateTime calculateStatementDate(ZonedDateTime from) {
            return from.plusYears(1L);
        }

    },

    SINGLE_PAYMENT {
        @Override
        public String getLabel() {
            return "Single Payment";
        }

        @Override
        public ZonedDateTime calculateStatementDate(ZonedDateTime from) {
            return null;
        }

    };

    public abstract String getLabel();

    public abstract ZonedDateTime calculateStatementDate(ZonedDateTime from);

}