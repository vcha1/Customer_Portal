package com.my1stle.customer.portal.serviceImpl.contactus;

import com.my1stle.customer.portal.service.contactus.ContactUsResult;

class SalesforceContactUsResult implements ContactUsResult {

    private Boolean isSuccessful;
    private String errorMessage;

    private SalesforceContactUsResult() {

    }

    SalesforceContactUsResult(Boolean isSuccessful, String errorMessage) {
        this.isSuccessful = isSuccessful;
        this.errorMessage = errorMessage;
    }

    @Override
    public Boolean isSuccessful() {
        return this.isSuccessful;
    }

    @Override
    public String errorMessage() {
        return errorMessage;
    }

}
