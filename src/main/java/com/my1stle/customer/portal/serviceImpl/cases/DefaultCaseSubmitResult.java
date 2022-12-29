package com.my1stle.customer.portal.serviceImpl.cases;

import com.my1stle.customer.portal.service.cases.CaseSubmitResult;

public class DefaultCaseSubmitResult implements CaseSubmitResult {

    public DefaultCaseSubmitResult(
            Boolean isSuccess,
            String message
    ){
       this.isSuccess = isSuccess;
       this.message= message;
    }
    private Boolean isSuccess;
    private String message;

    @Override
    public Boolean isSuccess() {
        return this.isSuccess;
    }

    @Override
    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setSuccess(Boolean success) {
        isSuccess = success;
    }
}
