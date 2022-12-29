package com.my1stle.customer.portal.serviceImpl.notification.email;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;


/**
 * This implementation uses Thymeleaf's TemplateEngine to generate email content
 */
class ThymeleafHtmlMailContentBuilder implements MailContentBuilder {

    private TemplateEngine templateEngine;
    private Context context;
    private String templatePath;

    ThymeleafHtmlMailContentBuilder(TemplateEngine templateEngine, Context context, String templatePath) {
        this.templateEngine = templateEngine;
        this.context = context;
        this.templatePath = templatePath;
    }

    @Override
    public final String build() {
        return templateEngine.process(templatePath, context);
    }

    public ThymeleafHtmlMailContentBuilder setVariable(String variableName, Object value) {
        this.context.setVariable(variableName, value);
        return this;
    }

}