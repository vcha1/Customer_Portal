package com.my1stle.customer.portal.web.exception;

public class UserRegistrationInstallationNotFoundException extends RuntimeException {

    public UserRegistrationInstallationNotFoundException() {
        super();
    }

    public UserRegistrationInstallationNotFoundException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public UserRegistrationInstallationNotFoundException(final String message) {
        super(message);
    }

    public UserRegistrationInstallationNotFoundException(final Throwable cause) {
        super(cause);
    }

}
