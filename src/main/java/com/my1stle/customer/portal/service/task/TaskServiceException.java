package com.my1stle.customer.portal.service.task;

public class TaskServiceException extends Exception {

    public TaskServiceException(String message) {
        super(message);
    }

    public TaskServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public TaskServiceException(Throwable cause) {
        super(cause);
    }

}