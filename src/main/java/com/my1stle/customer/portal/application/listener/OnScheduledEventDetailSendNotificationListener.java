package com.my1stle.customer.portal.application.listener;

import com.dev1stle.scheduling.system.v1.service.truck.roll.api.model.EventDetail;
import com.dev1stle.scheduling.system.v1.service.util.SalesforceIdPrefixUtil;
import com.my1stle.customer.portal.application.event.OnScheduledEventDetail;
import com.my1stle.customer.portal.service.notification.Notification;
import com.my1stle.customer.portal.service.notification.NotificationService;
import com.my1stle.customer.portal.service.util.IsValidSalesforceId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class OnScheduledEventDetailSendNotificationListener implements ApplicationListener<OnScheduledEventDetail> {

    private final NotificationService<EventDetail> notificationService;

    private final static Logger LOGGER = LoggerFactory.getLogger(OnScheduledEventDetailSendNotificationListener.class);

    @Autowired
    public OnScheduledEventDetailSendNotificationListener(NotificationService<EventDetail> notificationService) {
        this.notificationService = notificationService;
    }

    @Override
    public void onApplicationEvent(final OnScheduledEventDetail onScheduledEventDetail) {
        Objects.requireNonNull(onScheduledEventDetail);
        EventDetail eventDetail = onScheduledEventDetail.getEventDetail();
        String salesforceId = eventDetail.getExternalId();
        if (IsValidSalesforceId.getInstance().test(salesforceId)) {
            SalesforceIdPrefixUtil.IdPrefix idPrefix = SalesforceIdPrefixUtil.get(salesforceId);
            sendNotification(eventDetail, idPrefix);
        }
    }

    private void sendNotification(EventDetail eventDetail, SalesforceIdPrefixUtil.IdPrefix idPrefix) {
        if (SalesforceIdPrefixUtil.IdPrefix.Installation.equals(idPrefix)) {
            this.notificationService.send(eventDetail, Notification.SCHEDULED_INSTALLATION_EVENT_DETAIL_NOTIFICATION);
        } else if (SalesforceIdPrefixUtil.IdPrefix.Opportunity.equals(idPrefix)) {
            this.notificationService.send(eventDetail, Notification.SCHEDULED_OPPORTUNITY_EVENT_DETAIL_NOTIFICATION);
        }
    }

}