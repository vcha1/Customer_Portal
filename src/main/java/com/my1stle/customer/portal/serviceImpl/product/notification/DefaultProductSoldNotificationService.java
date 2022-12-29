package com.my1stle.customer.portal.serviceImpl.product.notification;

import com.google.common.base.Strings;
import com.my1stle.customer.portal.service.product.notification.ProductAndCustomerInformation;
import com.my1stle.customer.portal.service.product.notification.ProductSoldNotificationService;
import com.my1stle.customer.portal.service.util.MediaTypeUtil;
import org.apache.tika.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamSource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.internet.MimeMessage;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class DefaultProductSoldNotificationService implements ProductSoldNotificationService {
    private final JavaMailSender javaMailSender;
    private final String noReplyEmail;
    private final List<String> recipientEmails;
    private final TemplateEngine templateEngine;
    private final String TEMPLATE_NAME = "email/product-sold-notification";

    public DefaultProductSoldNotificationService(
            JavaMailSender javaMailSender,
            @Value("${customer.portal.no.reply.email}") String noReplyEmail,
            @Value("${product.sold.notification.recipients}") String productSoldRecipients,
            TemplateEngine templateEngine
    ) {
        this.javaMailSender = javaMailSender;
        this.noReplyEmail = noReplyEmail;
        this.templateEngine = templateEngine;

        if(Strings.isNullOrEmpty(productSoldRecipients)) {
            this.recipientEmails = Collections.emptyList();
        }
        else {
            String[] recipients = productSoldRecipients.split(",");
            this.recipientEmails = Stream.of(recipients)
                    .map(String::trim)
                    .collect(Collectors.toList());
        }
    }

    @Override
    public void sendNotification(ProductAndCustomerInformation information) {
        MimeMessagePreparator preparator = prepareMessage(information);
        this.javaMailSender.send(preparator);
    }

    private MimeMessagePreparator prepareMessage(ProductAndCustomerInformation information) {
        Context thymeleafContext = generateContext(information);
        String emailContent = this.templateEngine.process(TEMPLATE_NAME, thymeleafContext);

        return new Preparator(
                this.noReplyEmail,
                this.recipientEmails,
                "Product Purchased in Customer Portal",
                emailContent
        );
    }

    private Context generateContext(ProductAndCustomerInformation information) {
        Context context = new Context();

        context.setVariable("installationName", information.getInstallationName());
        context.setVariable("productName", information.getProductName());
        context.setVariable("productQuantity", information.getProductQuantity());
        context.setVariable("totalPaid", information.getTotalPaid());

        return context;
    }

    private static class Preparator implements MimeMessagePreparator {
        private final String from;
        private final List<String> recipients;
        private final String subject;
        private final String content;

        public Preparator(String from, List<String> recipients, String subject, String content) {
            this.from = from;
            this.recipients = recipients;
            this.subject = subject;
            this.content = content;
        }

        @Override
        public void prepare(MimeMessage mimeMessage) throws Exception {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            helper.setFrom(this.from);
            helper.setTo(this.recipients.toArray(new String[0]));
            helper.setSubject(this.subject);
            helper.setText(this.content, true);
        }
    }
}
