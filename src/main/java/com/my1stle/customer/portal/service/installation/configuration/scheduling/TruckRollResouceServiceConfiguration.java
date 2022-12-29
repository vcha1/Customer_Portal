package com.my1stle.customer.portal.service.installation.configuration.scheduling;

import com.dev1stle.repository.salesforce.client.SalesforceClient;
import com.dev1stle.scheduling.system.v1.repository.TruckRollResourceRepository;
import com.dev1stle.scheduling.system.v1.service.truck.roll.resource.TruckRollResourceService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class TruckRollResouceServiceConfiguration {

    @Bean
    @Primary
    public TruckRollResourceService truckRollResourceService(SalesforceClient salesforceClient) {
        return new TruckRollResourceRepository(salesforceClient);
    }

}
