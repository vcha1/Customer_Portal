package com.my1stle.customer.portal.service.installation.configuration.scheduling;

import com.dev1stle.repository.salesforce.SalesforceObjectRepository;
import com.dev1stle.repository.salesforce.client.SalesforceClient;
import com.dev1stle.scheduling.system.v1.model.salesforce.TruckRollResource;
import com.dev1stle.scheduling.system.v1.repository.TruckRollResourceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class TruckRollResourceRepositoryConfiguration {

    @Bean
    @Primary
    @Autowired
    public SalesforceObjectRepository<TruckRollResource> truckRollResourceSalesforceObjectRepository(
            SalesforceClient salesforceClient) {

        return new TruckRollResourceRepository(salesforceClient);

    }

}
