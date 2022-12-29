package com.my1stle.customer.portal.serviceImpl.notification.email;

import com.dev1stle.scheduling.system.v1.service.truck.roll.api.model.EventDetail;
import com.my1stle.customer.portal.service.time.zone.TimeZoneService;
import org.apache.tika.io.IOUtils;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

abstract class AbstractScheduledEmailEventDetailNotification<T> implements Function<EventDetail, MimeMessagePreparator> {

    private final TemplateEngine templateEngine;
    private final ResourceLoader resourceLoader;
    private final TimeZoneService timeZoneService;

    private final Map<Long, String> durationLabels;

    AbstractScheduledEmailEventDetailNotification(TemplateEngine templateEngine, ResourceLoader resourceLoader, TimeZoneService timeZoneService) {

        this.templateEngine = templateEngine;
        this.resourceLoader = resourceLoader;
        this.timeZoneService = timeZoneService;

        Map<Long, String> temp = new HashMap<>();

        // init duration labels
        temp.put(15L, "15 Mins.");
        temp.put(30L, "30 Mins.");
        temp.put(60L, "1 Hr.");
        temp.put(120L, "2 Hr.");
        temp.put(180L, "3 Hr.");
        temp.put(240L, "4 Hr.");
        temp.put(300L, "5 Hr.");
        temp.put(360L, "6 Hr.");
        temp.put(420L, "7 Hr.");
        temp.put(480L, "1 Day");
        temp.put(600L, "2 Days");
        temp.put(720L, "2 Days");
        temp.put(840L, "2 Days");
        temp.put(960L, "2 Days");

        durationLabels = Collections.unmodifiableMap(temp);

    }

    abstract T getContext(EventDetail eventDetail);

    abstract String subject(EventDetail eventDetail, T context);

    abstract List<String> recipients(EventDetail eventDetail, T context);

    abstract List<String> bccs(EventDetail eventDetail, T context);

    abstract String templatePath(EventDetail eventDetail, T context);

    Map<String, Object> additionalContextVariables(EventDetail eventDetail, T context) {
        return Collections.emptyMap();
    }

    /**
     * Applies this function to the given argument.
     *
     * @param eventDetail the function argument
     * @return the function result
     */
    @Override
    public final MimeMessagePreparator apply(EventDetail eventDetail) {

        Objects.requireNonNull(eventDetail);

        T context = getContext(eventDetail);
        String subject = subject(eventDetail, context);
        List<String> recipients = recipients(eventDetail, context);
        List<String> bccs = bccs(eventDetail, context);
        String templatePath = templatePath(eventDetail, context);
        String address = eventDetail.getAddress();
        Long duration = eventDetail.getDuration();
        ZonedDateTime appointmentTime = eventDetail.getStartDateTime()
                .withZoneSameInstant(this.timeZoneService.getBySingleLineAddress(address));
        Map<String, Object> additionalContextVariables = additionalContextVariables(eventDetail, context);
        String notes = eventDetail.getNotes();
        String reason = eventDetail.getReason();

        String appointmentLocalDate = DateTimeFormatter.ofPattern("MM-dd-yyyy").format(appointmentTime);
        String appointmentLocalTime = DateTimeFormatter.ofPattern("hh:mm a z").format(appointmentTime);


        ThymeleafHtmlMailContentBuilder htmlMailContentBuilder = new ThymeleafHtmlMailContentBuilder(templateEngine, new Context(), templatePath)
                .setVariable("logo", "logo")
                .setVariable("appointment_address", address)
                .setVariable("appointment_duration", durationLabels.get(duration))
                .setVariable("appointment_duration_in_mins", duration)
                .setVariable("appointment_local_date", appointmentLocalDate)
                .setVariable("appointment_local_time", appointmentLocalTime)
                .setVariable("notes", notes)
                .setVariable("reason", reason);

        for (String key : additionalContextVariables.keySet()) {
            htmlMailContentBuilder.setVariable(key, additionalContextVariables.get(key));
        }

        return prepareEmail(subject, recipients, bccs, htmlMailContentBuilder);

    }

    private MimeMessagePreparator prepareEmail(String subject, List<String> recipients, List<String> bccs, ThymeleafHtmlMailContentBuilder htmlMailContentBuilder) {

        Resource logo = this.resourceLoader.getResource("classpath:static/img/logo/1le-logo-black-lettering.png");

        try {
            return new HtmlMimeMessagePreparator(Email.NO_REPLY, recipients, subject, htmlMailContentBuilder)
                    .withBcc(bccs)
                    .addInline("logo", new ByteArrayResource(IOUtils.toByteArray(logo.getInputStream())));

        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

}
