package com.my1stle.customer.portal.persistence.model;

import com.dev1stle.repository.salesforce.model.SObjectApiName;
import com.dev1stle.repository.salesforce.model.SObjectField;
import com.dev1stle.repository.salesforce.model.SalesforceObject;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.time.LocalDate;
import java.time.ZonedDateTime;

@SObjectApiName(value = "Opportunity")
public class SalesforceOpportunity extends SalesforceObject {

    @SObjectField(api_name = "RecordTypeId")
    private String recordTypeId;
    @SObjectField(api_name = "RecordType.Name")
    private String recordTypeName;
    @SObjectField(api_name = "Name")
    private String name;
    @SObjectField(api_name = "StageName")
    private String stageName;
    @SObjectField(api_name = "CloseDate")
    private LocalDate closeDate;
    @SObjectField(api_name = "AccountId")
    private String accountId;
    @SObjectField(api_name = "Account.FirstName")
    private String accountFirstName;
    @SObjectField(api_name = "Account.LastName")
    private String accountLastName;
    @SObjectField(api_name = "Email_from_Account__c", read_only = true)
    private String email;
    @SObjectField(api_name = "Co_Signer_First_Name__c")
    private String coSignerFirstName;
    @SObjectField(api_name = "Co_Signer_Last_Name__c")
    private String coSignerLastName;
    @SObjectField(api_name = "Co_Signer_Email_Address__c")
    private String coSignerEmail;
    @SObjectField(api_name = "Co_Signer_Contracts__c")
    private String coSignerContracts; // multi-select picklist
    @SObjectField(api_name = "Utility_Holder_First_Name__c")
    private String utilityHolderFirstName;
    @SObjectField(api_name = "Utility_Holder_Last_Name__c")
    private String utilityHolderLastName;
    @SObjectField(api_name = "Utility_Holder_Email_Address__c")
    private String utilityHolderEmail;
    @SObjectField(api_name = "Account.ShippingStreet")
    private String street;
    @SObjectField(api_name = "Account.ShippingCity")
    private String city;
    @SObjectField(api_name = "Account.ShippingState")
    private String state;
    @SObjectField(api_name = "Account.ShippingPostalCode")
    private String zip;
    @SObjectField(api_name = "Account.Phone")
    private String homePhone;
    @SObjectField(api_name = "Account.Mobile__c")
    private String mobilePhone;
    @SObjectField(api_name = "Utility_Company__c")
    private String utilityCompanyId;
    @SObjectField(api_name = "Utility_Company__r.Name")
    private String utilityCompanyName;
    @SObjectField(api_name = "Direct_Sales_Closer_Account__c")
    private String directSalesCloserAccountId;
    @SObjectField(api_name = "Direct_Sales_Closer_Account__r.Name")
    private String directSalesCloserAccountName;
    @SObjectField(api_name = "Direct_Sales_Closer_Account__r.X1st_Light_Email__c")
    private String directSalesCloserAccountEmail;
    @SObjectField(api_name = "Direct_Sales_Finder_acct__c")
    private String directSalesFinderAccountId;
    @SObjectField(api_name = "Direct_Sales_Finder_acct__r.Name")
    private String directSalesFinderAccountName;
    @SObjectField(api_name = "Direct_Sales_Finder_acct__r.X1st_Light_Email__c")
    private String directSalesFinderAccountEmail;
    @SObjectField(api_name = "Account_Manager__c", read_only = true)
    private String accountManager;
    @SObjectField(api_name = "Account_Manager_Contact_Info__c", read_only = true)
    private String accountManagerContactInfo;
    @SObjectField(api_name = "PSA_Type__c")
    private String psaType;
    @SObjectField(api_name = "PSA_Date_Time__c")
    private ZonedDateTime psaDateTime;
    @SObjectField(api_name = "Missing_Deal_Docs__c", read_only = true)
    private String missingDealDocs;
    @SObjectField(api_name = "Closed_Lost_Reason_Other__c")
    private String reasonOther;
    @SObjectField(api_name = "Installation__r.Finance_Complete__c")
    private LocalDate financeCompleteDate;
    @SObjectField(api_name = "Installation__r.Finance_Notes__c")
    private String financeNotes;
    @SObjectField(api_name = "DSR_Notes__c")
    private String dsrNotes;
    @SObjectField(api_name = "Retail_Marketing_Area__c")
    private String operationalArea;

    private SalesforceOpportunity() {

    }

    public String getRecordTypeId() {
        return recordTypeId;
    }

    public String getRecordTypeName() {
        return recordTypeName;
    }

    public String getStageName() {
        return stageName;
    }

    public String getName() {
        return name;
    }

    public LocalDate getCloseDate() {
        return closeDate;
    }

    public String getAccountId() {
        return accountId;
    }

    public String getAccountFirstName() {
        return accountFirstName;
    }

    public String getAccountLastName() {
        return accountLastName;
    }

    public String getEmail() {
        return email;
    }

    public String getCoSignerFirstName() {
        return coSignerFirstName;
    }

    public String getCoSignerLastName() {
        return coSignerLastName;
    }

    public String getCoSignerEmail() {
        return coSignerEmail;
    }

    public String getCoSignerContracts() {
        return coSignerContracts;
    }

    public String getUtilityHolderFirstName() {
        return utilityHolderFirstName;
    }

    public String getUtilityHolderLastName() {
        return utilityHolderLastName;
    }

    public String getUtilityHolderEmail() {
        return utilityHolderEmail;
    }

    public String getStreet() {
        return street;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public String getZip() {
        return zip;
    }

    public String getHomePhone() {
        return homePhone;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public String getUtilityCompanyId() {
        return utilityCompanyId;
    }

    public String getUtilityCompanyName() {
        return utilityCompanyName;
    }

    public String getDirectSalesCloserAccountId() {
        return directSalesCloserAccountId;
    }

    public String getDirectSalesCloserAccountName() {
        return directSalesCloserAccountName;
    }

    public String getDirectSalesCloserAccountEmail() {
        return directSalesCloserAccountEmail;
    }

    public String getDirectSalesFinderAccountId() {
        return directSalesFinderAccountId;
    }

    public String getDirectSalesFinderAccountName() {
        return directSalesFinderAccountName;
    }

    public String getDirectSalesFinderAccountEmail() {
        return directSalesFinderAccountEmail;
    }

    public String getAccountManager() {
        return accountManager;
    }

    public String getAccountManagerContactInfo() {
        return accountManagerContactInfo;
    }

    public String getPsaType() {
        return psaType;
    }

    public ZonedDateTime getPsaDateTime() {
        return psaDateTime;
    }

    public String getMissingDealDocs() {
        return missingDealDocs;
    }

    public String getReasonOther() {
        return reasonOther;
    }

    public LocalDate getFinanceCompleteDate() {
        return financeCompleteDate;
    }

    public String getFinanceNotes() {
        return financeNotes;
    }

    public String getDsrNotes() {
        return dsrNotes;
    }

    public String getOperationalArea() {
        return operationalArea;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;

        if (!(o instanceof SalesforceOpportunity)) return false;

        SalesforceOpportunity that = (SalesforceOpportunity) o;

        return new EqualsBuilder()
                .append(getId(), that.getId())
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(getId())
                .toHashCode();
    }

}
