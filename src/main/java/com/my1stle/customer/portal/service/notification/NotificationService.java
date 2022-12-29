package com.my1stle.customer.portal.service.notification;

public interface NotificationService<T> {

    /**
     * @param payload
     * @param notification
     * @implSpec sends out a notification based on the payload
     */
    void send(T payload, Notification notification);

}
