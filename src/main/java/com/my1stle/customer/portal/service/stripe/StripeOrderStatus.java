package com.my1stle.customer.portal.service.stripe;

public class StripeOrderStatus {
    public static String CREATED = "CREATED";
    public static String SAVED = "SAVED";
    public static String APPROVED = "APPROVED";
    public static String VOIDED = "VOIDED";
    public static String COMPLETED = "COMPLETED";


    private StripeOrderStatus() {
    }
}
