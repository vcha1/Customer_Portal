package com.my1stle.customer.portal.service.installation.configuration.salesforce.client;

import com.dev1stle.repository.salesforce.client.SalesforceClient;
import com.sforce.ws.ConnectionException;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class SalesforceClientConfiguration {


    @Bean
    @Primary
    public SalesforceClient salesforceClient(
            @Value("${1le.salesforce.username}") String username,
            @Value("${1le.salesforce.password}") String password,
            @Value("${1le.salesforce.auth.endpoint}") String authEndpoint) {
        try {
            return new SalesforceClient(username, password, authEndpoint);
        } catch (ConnectionException e) {
            throw new BeanInitializationException(e.getMessage(), e);
        }

    }

}
