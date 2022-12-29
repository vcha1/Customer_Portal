package com.my1stle.customer.portal.service.slack;

public enum SlackChannel {

    CUSTOMER_SCHEDULING("https://hooks.slack.com/services/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");

    private String webhookUrl;

    SlackChannel(String webhookUrl) {
        this.webhookUrl = webhookUrl;
    }

    public String getWebhookUrl() {
        return webhookUrl;
    }
}