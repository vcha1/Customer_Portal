package com.my1stle.customer.portal.service.odoo;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class OdooOpportunityRepositoryService implements OdooOpportunityRepository {
    private static final String ODOO_OPPORTUNITY_MODEL_NAME = "crm.lead";
    private static final List<String> ODOO_OPPORTUNITY_FIELDS = Arrays.asList(
            "id", "name"
    );

    private final OdooObjectConnection objectConnection;

    @Autowired
    public OdooOpportunityRepositoryService(
            OdooObjectConnection objectConnection
    ) {
        this.objectConnection = objectConnection;
    }

    @Override
    public String getModelName() {
        return ODOO_OPPORTUNITY_MODEL_NAME;
    }
}
