package com.my1stle.customer.portal.service.scheduling;

public class NoAvailabilityException extends Exception {

    public NoAvailabilityException(String message) {
        super(message);
    }

    NoAvailabilityException(String message, Throwable cause) {
        super(message, cause);
    }
}
