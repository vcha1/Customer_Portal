package com.my1stle.customer.portal.service.cases;

public class CaseServiceException extends Exception {

    public CaseServiceException(String message) {
        super(message);
    }

    public CaseServiceException(String message, Throwable cause) {
        super(message, cause);
    }


}
