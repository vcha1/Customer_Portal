package com.my1stle.customer.portal.service.installation.configuration.adobe.sign.client;


import com.my1stle.customer.portal.service.adobe.sign.client.facade.AdobeSignFacadeClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class AdobeSignFacadeClientConfiguration {

    @Bean
    @Primary
    public AdobeSignFacadeClient adobeSignFacadeClient(@Value("${adobe.sign.api.token}") String token) {
        return new AdobeSignFacadeClient(token);
    }

}