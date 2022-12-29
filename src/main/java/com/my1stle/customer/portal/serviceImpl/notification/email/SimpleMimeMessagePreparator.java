package com.my1stle.customer.portal.serviceImpl.notification.email;

import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;

import javax.mail.internet.MimeMessage;
import java.util.List;

/**
 * A simple MimeMessagePreparator to cover common email scenarios.
 */
public class SimpleMimeMessagePreparator implements MimeMessagePreparator {
    private final String from;
    private final List<String> recipients;
    private final String subject;
    private final String content;
    private final boolean isHtml;

    /**
     * Creates a new preparator with the given values and with the content as HTML.
     * @param from the sender
     * @param recipients the intended recipients
     * @param subject the subject line
     * @param content the HTML content of the email
     */
    public SimpleMimeMessagePreparator(String from, List<String> recipients, String subject, String content) {
        this(from, recipients, subject, content, true);
    }

    /**
     * Creates a new preparator with the given values.
     * @param from the sender
     * @param recipients the intended recipients
     * @param subject the subject line
     * @param content the content of the email
     * @param contentIsHtml whether the content is HTML or not
     */
    public SimpleMimeMessagePreparator(String from, List<String> recipients, String subject, String content, boolean contentIsHtml) {
        this.from = from;
        this.recipients = recipients;
        this.subject = subject;
        this.content = content;
        this.isHtml = contentIsHtml;
    }

    @Override
    public void prepare(MimeMessage mimeMessage) throws Exception {
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
        helper.setFrom(this.from);
        helper.setTo(this.recipients.toArray(new String[0]));
        helper.setSubject(this.subject);
        helper.setText(this.content, this.isHtml);
    }
}
