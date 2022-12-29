package com.my1stle.customer.portal.serviceImpl.task;

import com.dev1stle.repository.salesforce.SalesforceObjectRepository;
import com.dev1stle.repository.salesforce.client.SalesforceClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
class SalesforceTaskRepository extends SalesforceObjectRepository<SalesforceTask> {

    @Autowired
    public SalesforceTaskRepository(SalesforceClient client) {
        super(client);
    }

}