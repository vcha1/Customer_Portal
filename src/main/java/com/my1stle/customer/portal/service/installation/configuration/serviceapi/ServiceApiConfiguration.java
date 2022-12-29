package com.my1stle.customer.portal.service.installation.configuration.serviceapi;

import com.my1stle.customer.portal.service.serviceapi.ServiceApiCredentials;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServiceApiConfiguration {


    @Bean
    public ServiceApiCredentials serviceApiCredentials(
            @Value("${service.api.host}") String host,
            @Value("${service.api.environment}") String environment,
            @Value("${service.api.key}") String apiKey) {
        return new DefaultServiceApiCredentials(host, environment, apiKey);
    }


    private static class DefaultServiceApiCredentials implements ServiceApiCredentials {

        private String host;
        private String environment;
        private String apiKey;

        private DefaultServiceApiCredentials(String host, String environment, String apiKey) {
            this.host = host;
            this.environment = environment;
            this.apiKey = apiKey;
        }

        @Override
        public String getHost() {
            return host;
        }

        @Override
        public String getEnvironment() {
            return environment;
        }

        @Override
        public String getApiKey() {
            return apiKey;
        }

    }

}
