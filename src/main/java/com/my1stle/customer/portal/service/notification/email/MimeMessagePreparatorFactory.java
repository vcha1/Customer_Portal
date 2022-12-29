package com.my1stle.customer.portal.service.notification.email;

import com.my1stle.customer.portal.service.notification.Notification;
import org.springframework.mail.javamail.MimeMessagePreparator;

public interface MimeMessagePreparatorFactory<T> {

    /**
     * @param t
     * @param notification
     * @return a new instance of MimeMessagePeparator
     */
    MimeMessagePreparator create(T t, Notification notification) throws MimeMessagePreparatorException;

}