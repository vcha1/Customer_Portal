package com.my1stle.customer.portal.service.installation.configuration.adobe.sign.client;

import com.adobe.sign.api.AgreementsApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class AgreementsClientConfiguration {
	@Bean
	@Primary
	public AgreementsApi agreementsApi() {
		return new AgreementsApi();
	}
}
