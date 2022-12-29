package com.my1stle.customer.portal.service.installation.configuration.salesforce.repository;

import com.my1stle.customer.portal.service.installation.configuration.salesforce.deserialization.LocalDateDeserializer;
import com.my1stle.customer.portal.persistence.model.InstallationSalesforceObject;
import com.dev1stle.repository.salesforce.SalesforceObjectRepository;
import com.dev1stle.repository.salesforce.client.SalesforceClient;
import com.dev1stle.repository.salesforce.deserializer.SalesforceDeserializerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;

@Service
public class InstallationRepository extends SalesforceObjectRepository<InstallationSalesforceObject> {

    @Autowired
    public InstallationRepository(SalesforceClient client) {
        super(client);
    }

    @Override
    protected Collection<SalesforceDeserializerAdapter<?>> salesforceDeserializerAdapters() {
        return Collections.singletonList(new SalesforceDeserializerAdapter<>(new LocalDateDeserializer()));
    }
}