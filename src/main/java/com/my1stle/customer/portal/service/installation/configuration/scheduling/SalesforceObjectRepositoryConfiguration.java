package com.my1stle.customer.portal.service.installation.configuration.scheduling;

import com.dev1stle.repository.salesforce.SalesforceObjectRepository;
import com.dev1stle.repository.salesforce.client.SalesforceClient;
import com.dev1stle.scheduling.system.v1.model.salesforce.Case;
import com.dev1stle.scheduling.system.v1.model.salesforce.Installation;
import com.dev1stle.scheduling.system.v1.model.salesforce.Opportunity;
import com.dev1stle.scheduling.system.v1.repository.CaseRepository;
import com.dev1stle.scheduling.system.v1.repository.InstallationRepository;
import com.dev1stle.scheduling.system.v1.repository.OpportunityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * This sets up the various SalesforceObjectRepository from dev.1stle.scheduling.system.v1.repository
 */
@Configuration
public class SalesforceObjectRepositoryConfiguration {

    @Bean
    @Autowired
    public SalesforceObjectRepository<Installation> installationSalesforceObjectRepository(SalesforceClient salesforceClient) {
        return new InstallationRepository(salesforceClient);
    }

    @Bean
    @Autowired
    public SalesforceObjectRepository<Case> caseSalesforceObjectRepository(SalesforceClient salesforceClient) {
        return new CaseRepository(salesforceClient);
    }

    @Bean
    @Autowired
    public SalesforceObjectRepository<Opportunity> opportunitySalesforceObjectRepository(SalesforceClient salesforceClient) {
        return new OpportunityRepository(salesforceClient);
    }

}
