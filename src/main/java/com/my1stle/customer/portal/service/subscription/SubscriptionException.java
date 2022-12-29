package com.my1stle.customer.portal.service.subscription;

public class SubscriptionException extends Exception {

    private SubscriptionException() {
        throw new AssertionError();
    }

    public SubscriptionException(String message) {
        super(message);
    }

    public SubscriptionException(String message, Throwable cause) {
        super(message, cause);
    }

}
