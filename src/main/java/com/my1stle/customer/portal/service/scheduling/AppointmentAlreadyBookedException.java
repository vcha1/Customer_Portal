package com.my1stle.customer.portal.service.scheduling;

public class AppointmentAlreadyBookedException extends Exception {

    public AppointmentAlreadyBookedException(String message) {
        super(message);
    }

    public AppointmentAlreadyBookedException(String message, Throwable cause) {
        super(message, cause);
    }

}
