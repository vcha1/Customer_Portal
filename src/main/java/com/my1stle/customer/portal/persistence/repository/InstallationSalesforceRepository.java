package com.my1stle.customer.portal.persistence.repository;

import com.my1stle.customer.portal.service.installation.configuration.salesforce.repository.InstallationRepository;
import com.my1stle.customer.portal.persistence.model.InstallationSalesforceObject;
import com.dev1stle.repository.specification.salesforce.WhereClause;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InstallationSalesforceRepository {

    private InstallationRepository installationSalesforceObjectRepository;

    @Autowired
    public InstallationSalesforceRepository(InstallationRepository installationSalesforceObjectRepository) {
        this.installationSalesforceObjectRepository = installationSalesforceObjectRepository;
    }

    public List<InstallationSalesforceObject> selectByCustomerAccountId(String customerAccountId) {
        return this.installationSalesforceObjectRepository.query(new WhereClause(
                String.format("Account__c = '%s'", customerAccountId)
        ));
    }

    public List<InstallationSalesforceObject> selectByCustomerEmail(String email) {
        //This returns the installation ID that will be used throughout the project to get the right data
        return this.installationSalesforceObjectRepository.query(new WhereClause(
                String.format("Email_Address__c = '%s'", email)
        ));
    }

    public InstallationSalesforceObject selectByIdAndCustomerEmail(String id, String email) {
        /*TODO: reinstate this code (the expected behavior) when looking at installations on an unrestricted individual basis is no longer necessary
        List<InstallationSalesforceObject> results = this.installationSalesforceObjectRepository.query(new WhereClause(
                String.format("Email_Address__c = '%s' AND Id = '%s'", email, id)
        ));
         */

        /* TODO: remove this when the expected behavior code above is reinstated */
        List<InstallationSalesforceObject> results = this.installationSalesforceObjectRepository.query(new WhereClause(
                String.format("Id = '%s'", id)
        ));
        /**/

        if (results.size() > 1)
            throw new RuntimeException("More than one result found!");

        if (results.size() == 0)
            return null;
        return results.get(0);

    }
}
