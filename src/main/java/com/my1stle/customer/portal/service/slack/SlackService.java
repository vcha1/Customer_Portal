package com.my1stle.customer.portal.service.slack;

public interface SlackService {
    void postMessage(SlackMessage message, SlackChannel slackChannel);
}
