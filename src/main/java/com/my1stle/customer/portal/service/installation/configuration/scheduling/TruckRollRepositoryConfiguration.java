package com.my1stle.customer.portal.service.installation.configuration.scheduling;

import com.dev1stle.repository.salesforce.SalesforceObjectRepository;
import com.dev1stle.repository.salesforce.client.SalesforceClient;
import com.dev1stle.scheduling.system.v1.model.salesforce.TruckRoll;
import com.dev1stle.scheduling.system.v1.repository.TruckRollRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class TruckRollRepositoryConfiguration {

    @Bean
    @Primary
    @Autowired
    public SalesforceObjectRepository<TruckRoll> truckRollSalesforceObjectRepository(
            SalesforceClient salesforceClient) {

        return new TruckRollRepository(salesforceClient);

    }

}
