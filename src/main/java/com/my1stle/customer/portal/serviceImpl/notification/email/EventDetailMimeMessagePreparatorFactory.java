package com.my1stle.customer.portal.serviceImpl.notification.email;

import com.dev1stle.scheduling.system.v1.service.truck.roll.api.model.EventDetail;
import com.my1stle.customer.portal.service.notification.Notification;
import com.my1stle.customer.portal.service.notification.email.MimeMessagePreparatorException;
import com.my1stle.customer.portal.service.notification.email.MimeMessagePreparatorFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

@Service
class EventDetailMimeMessagePreparatorFactory implements MimeMessagePreparatorFactory<EventDetail> {

    private final Map<Notification, Function<EventDetail, MimeMessagePreparator>> handlers = new HashMap<>();

    @Autowired
    EventDetailMimeMessagePreparatorFactory(
            Function<EventDetail, MimeMessagePreparator> scheduledInstallationEventDetailNotification,
            Function<EventDetail, MimeMessagePreparator> scheduledOpportunityEventDetailNotification
    ) {
        handlers.put(Notification.SCHEDULED_INSTALLATION_EVENT_DETAIL_NOTIFICATION, scheduledInstallationEventDetailNotification);
        handlers.put(Notification.SCHEDULED_OPPORTUNITY_EVENT_DETAIL_NOTIFICATION, scheduledOpportunityEventDetailNotification);
    }


    /**
     * @param eventDetail
     * @param notification
     * @return a new instance of MimeMessagePeparator
     */
    @Override
    public MimeMessagePreparator create(EventDetail eventDetail, Notification notification) throws MimeMessagePreparatorException {

        Objects.requireNonNull(eventDetail, String.format("Non-null %s expected", EventDetail.class.getSimpleName()));
        Objects.requireNonNull(notification, String.format("Non-null %s expected", Notification.class.getSimpleName()));

        Function<EventDetail, MimeMessagePreparator> handler = handlers.get(notification);

        if (null == handler) {
            throw new MimeMessagePreparatorException(String.format("Notification %s not yet supported!", notification.name()));
        }

        return handler.apply(eventDetail);

    }

}