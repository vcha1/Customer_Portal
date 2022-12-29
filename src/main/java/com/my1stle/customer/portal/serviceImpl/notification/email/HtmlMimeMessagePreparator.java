package com.my1stle.customer.portal.serviceImpl.notification.email;

import com.my1stle.customer.portal.service.util.MediaTypeUtil;
import org.apache.tika.io.IOUtils;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamSource;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;

import javax.mail.internet.MimeMessage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Prepares MimeMessage as html content
 */
public class HtmlMimeMessagePreparator implements MimeMessagePreparator {

    private String from;
    private List<String> recipients;
    private String subject;
    private MailContentBuilder mailContentBuilder;

    private List<String> bccs = new ArrayList<>();
    private Map<String, InputStreamSource> inputStreamSourceMap = new HashMap<>();

    public HtmlMimeMessagePreparator(
            String from,
            List<String> recipients,
            String subject,
            MailContentBuilder mailContentBuilder) {

        this.from = from;
        this.recipients = recipients;
        this.subject = subject;
        this.mailContentBuilder = mailContentBuilder;

    }

    HtmlMimeMessagePreparator withBcc(List<String> bcc) {
        this.bccs = bcc;
        return this;
    }

    HtmlMimeMessagePreparator addInline(String name, InputStreamSource inputStreamSource) {
        this.inputStreamSourceMap.put(name, inputStreamSource);
        return this;
    }

    @Override
    public void prepare(MimeMessage mimeMessage) throws Exception {

        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
        messageHelper.setFrom(from);
        messageHelper.setTo(recipients.toArray(new String[0]));
        messageHelper.setSubject(subject);
        String content = mailContentBuilder.build();
        messageHelper.setText(content, true);
        if (!bccs.isEmpty()) {
            messageHelper.setBcc(bccs.toArray(new String[0]));
        }

        for (String sourceName : inputStreamSourceMap.keySet()) {
            InputStreamSource inputStreamSource = inputStreamSourceMap.get(sourceName);
            String type = MediaTypeUtil.getMimeType(inputStreamSource.getInputStream()).getType().toString();
            messageHelper.addInline(sourceName, new ByteArrayResource(IOUtils.toByteArray(inputStreamSource.getInputStream())), type);
        }

    }

}