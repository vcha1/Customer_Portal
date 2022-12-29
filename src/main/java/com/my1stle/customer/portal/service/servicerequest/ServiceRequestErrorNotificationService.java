package com.my1stle.customer.portal.service.servicerequest;

public interface ServiceRequestErrorNotificationService {
    void sendPostPaymentProcessingErrorNotification(String description, Throwable e);
}
