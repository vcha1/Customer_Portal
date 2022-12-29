package com.my1stle.customer.portal.serviceImpl.notification;

import com.dev1stle.scheduling.system.v1.service.truck.roll.api.model.EventDetail;
import com.my1stle.customer.portal.service.notification.Notification;
import com.my1stle.customer.portal.service.notification.NotificationService;
import com.my1stle.customer.portal.service.notification.email.MimeMessagePreparatorException;
import com.my1stle.customer.portal.service.notification.email.MimeMessagePreparatorFactory;
import com.my1stle.customer.portal.service.slack.SlackChannel;
import com.my1stle.customer.portal.service.slack.SlackMessage;
import com.my1stle.customer.portal.service.slack.SlackService;
import com.my1stle.customer.portal.service.time.zone.TimeZoneService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class EventDetailNotificationService implements NotificationService<EventDetail> {

    private JavaMailSender javaMailSender;
    private MimeMessagePreparatorFactory<EventDetail> eventDetailMimeMessagePreparatorFactory;
    private SlackService slackService;
    private TimeZoneService timeZoneService;

    private static final Logger LOGGER = LoggerFactory.getLogger(EventDetailNotificationService.class);

    @Autowired
    public EventDetailNotificationService(
            JavaMailSender javaMailSender,
            MimeMessagePreparatorFactory<EventDetail> eventDetailMimeMessagePreparatorFactory,
            SlackService slackService,
            TimeZoneService timeZoneService) {

        this.javaMailSender = javaMailSender;
        this.eventDetailMimeMessagePreparatorFactory = eventDetailMimeMessagePreparatorFactory;
        this.slackService = slackService;
        this.timeZoneService = timeZoneService;
    }

    /**
     * @param eventDetail
     * @param notification
     * @implNote this implementations sends out a notification to customer via email
     * @implNote sends a notification to #customer-scheduling slack channel. See DEV-570
     */
    @Override
    public void send(EventDetail eventDetail, Notification notification) {

        sendEmail(eventDetail, notification);

        sendSlackNotification(eventDetail);

    }

    private void sendEmail(EventDetail eventDetail, Notification notification) {
        try {
            MimeMessagePreparator mimeMessagePreparator = eventDetailMimeMessagePreparatorFactory.create(eventDetail, notification);
            javaMailSender.send(mimeMessagePreparator);
        } catch (MailException e) {
            LOGGER.error("Unable to send notification! {} ", e.getMessage());
        } catch (MimeMessagePreparatorException e) {
            LOGGER.error("Unable to prepare email message! {} ", e.getMessage());
        }
    }

    private void sendSlackNotification(EventDetail eventDetail) {

        ZoneId timezone = this.timeZoneService.getBySingleLineAddress(eventDetail.getAddress());
        ZonedDateTime appointmentTime = eventDetail.getStartDateTime().withZoneSameInstant(timezone);
        String appointmentLocalDateTime = DateTimeFormatter.ofPattern("MM-dd-yyyy hh:mm a z").format(appointmentTime);

        slackService.postMessage(
                new SlackMessage(String.format("<https://ox.1stlightenergy.com/truck-roll/management/view-detail?id=%s|%s> has been scheduled for %s",
                        eventDetail.getId(),
                        eventDetail.getName(),
                        appointmentLocalDateTime)
                ),
                SlackChannel.CUSTOMER_SCHEDULING);

    }

}