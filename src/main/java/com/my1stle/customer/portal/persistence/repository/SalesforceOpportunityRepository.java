package com.my1stle.customer.portal.persistence.repository;

import com.my1stle.customer.portal.persistence.model.SalesforceOpportunity;

import java.util.Optional;

public interface SalesforceOpportunityRepository {

    Optional<SalesforceOpportunity> findById(String opportunityId);

}
