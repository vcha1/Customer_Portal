package com.my1stle.customer.portal.serviceImpl.slack;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.my1stle.customer.portal.service.slack.SlackChannel;
import com.my1stle.customer.portal.service.slack.SlackMessage;
import com.my1stle.customer.portal.service.slack.SlackService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
class DefaultSlackService implements SlackService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultSlackService.class);

    /**
     * @param message
     * @param slackChannel
     */
    @Override
    public void postMessage(SlackMessage message, SlackChannel slackChannel) {

        HttpResponse<String> httpResponse;

        try {

            httpResponse = Unirest.post(slackChannel.getWebhookUrl())
                    .body(message)
                    .asString();

        } catch (UnirestException e) {
            LOGGER.error(String.format("Unable to send slack message to webhook url [%s]", slackChannel.toString()), e);
            return;
        }

        if (!HttpStatus.valueOf(httpResponse.getStatus()).is2xxSuccessful()) {
            LOGGER.error("Unable to send slack message to webhook url [{}]\n{}", slackChannel.toString(), httpResponse.getBody());
        }

    }

}