package com.my1stle.customer.portal.serviceImpl.servicerequest;

import com.dev1stle.repository.salesforce.SalesforceObjectRepository;
import com.dev1stle.repository.salesforce.client.SalesforceClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SalesforceServiceCaseRepository extends SalesforceObjectRepository<SalesforceServiceCase> {

    @Autowired
    public SalesforceServiceCaseRepository(SalesforceClient client) {
        super(client);
    }

}
