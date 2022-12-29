package com.my1stle.customer.portal.service.contactus;

public enum ContactUsReason {

    REQUESTING_QUOTE("Requesting Quote"), PRODUCT_ENQUIRY("Product/Service Enquiry");

    private String label;

    private ContactUsReason(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

}