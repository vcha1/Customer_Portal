package com.my1stle.customer.portal.service.installation.configuration.scheduling;

import com.dev1stle.repository.salesforce.client.SalesforceClient;
import com.dev1stle.scheduling.system.v1.repository.TruckRollRepository;
import com.dev1stle.scheduling.system.v1.service.truck.roll.ExistingTruckRollService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ExistingTruckRollServiceConfiguration {

    @Bean
    @Autowired
    public ExistingTruckRollService existingTruckRollService(SalesforceClient salesforceClient) {
        return new TruckRollRepository(salesforceClient);
    }
}
