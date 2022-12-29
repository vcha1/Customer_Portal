package com.my1stle.customer.portal.persistence.repository;

import com.dev1stle.repository.salesforce.SalesforceObjectRepository;
import com.dev1stle.repository.salesforce.client.SalesforceClient;
import com.my1stle.customer.portal.persistence.model.SalesforceOpportunity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DefaultSalesforceOpportunityRepository extends SalesforceObjectRepository<SalesforceOpportunity> implements SalesforceOpportunityRepository {

    @Autowired
    public DefaultSalesforceOpportunityRepository(SalesforceClient client) {
        super(client);
    }

}