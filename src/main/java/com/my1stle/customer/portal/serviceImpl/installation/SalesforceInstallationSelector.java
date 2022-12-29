package com.my1stle.customer.portal.serviceImpl.installation;

import com.dev1stle.repository.salesforce.SalesforceObjectRepository;
import com.dev1stle.repository.specification.salesforce.WhereClause;
import com.my1stle.customer.portal.persistence.model.InstallationSalesforceObject;
import com.my1stle.customer.portal.service.installation.InstallationSelector;
import com.my1stle.customer.portal.service.model.Installation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * This implementation selects Installations from Salesforce
 */
@Service
public class SalesforceInstallationSelector implements InstallationSelector {

    private SalesforceObjectRepository<InstallationSalesforceObject> installationSalesforceObjectRepository;

    @Autowired
    public SalesforceInstallationSelector(SalesforceObjectRepository<InstallationSalesforceObject> installationSalesforceObjectRepository) {
        this.installationSalesforceObjectRepository = installationSalesforceObjectRepository;
    }

    @Override
    public List<Installation> selectByCustomerEmail(String email) {

        List<InstallationSalesforceObject> results = this.installationSalesforceObjectRepository.query(new WhereClause(
                String.format("Email_Address__c = '%s'", email)
        ));

        return new ArrayList<>(results);

    }

    @Override
    public List<Installation> selectByCustomerEmailAndContractApprovedDateIsBefore(String email, LocalDate date) {

        Objects.requireNonNull(date);

        List<InstallationSalesforceObject> results = this.installationSalesforceObjectRepository.query(new WhereClause(
                String.format("Email_Address__c = '%s' AND Contract_Approved_Date__c < %s", email, DateTimeFormatter.ISO_LOCAL_DATE.format(date))
        ));

        return new ArrayList<>(results);

    }

}
