package com.my1stle.customer.portal.service.scheduling;

public class SelfScheduleException extends Exception {

    public SelfScheduleException(String message) {
        super(message);
    }

    public SelfScheduleException(String message, Throwable cause) {
        super(message, cause);
    }

}
