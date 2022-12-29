package com.my1stle.customer.portal.serviceImpl.servicerequest;

import com.google.common.base.Strings;
import com.my1stle.customer.portal.service.servicerequest.ServiceRequestErrorNotificationService;
import com.my1stle.customer.portal.serviceImpl.notification.email.SimpleMimeMessagePreparator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class DefaultServiceRequestErrorNotificationService implements ServiceRequestErrorNotificationService {
    private final JavaMailSender javaMailSender;
    private final String noReplyEmail;
    private final List<String> recipientEmails;
    private final TemplateEngine templateEngine;
    private final String TEMPLATE_NAME = "email/simple-error-notification";

    public DefaultServiceRequestErrorNotificationService(
            JavaMailSender javaMailSender,
            @Value("${customer.portal.no.reply.email}") String noReplyEmail,
            @Value("${product.sold.notification.recipients}") String rawRecipientEmails,
            TemplateEngine templateEngine
    ) {
        this.javaMailSender = javaMailSender;
        this.noReplyEmail = noReplyEmail;
        this.templateEngine = templateEngine;

        if(Strings.isNullOrEmpty(rawRecipientEmails)) {
            this.recipientEmails = Collections.emptyList();
        }
        else {
            String[] recipients = rawRecipientEmails.split(",");
            this.recipientEmails = Stream.of(recipients)
                    .map(String::trim)
                    .collect(Collectors.toList());
        }
    }

    @Override
    public void sendPostPaymentProcessingErrorNotification(String description, Throwable e) {
        String emailContent = generateContent(description, e);

        MimeMessagePreparator preparator = new SimpleMimeMessagePreparator(
                this.noReplyEmail,
                this.recipientEmails,
                "An error prevented a purchased product from being fully processed",
                emailContent
        );

        this.javaMailSender.send(preparator);
    }

    private String generateContent(String errorDescription, Throwable e) {
        Context context = new Context();
        context.setVariable("errorDescription", errorDescription);
        context.setVariable("exception", e);

        return this.templateEngine.process(TEMPLATE_NAME, context);
    }
}
