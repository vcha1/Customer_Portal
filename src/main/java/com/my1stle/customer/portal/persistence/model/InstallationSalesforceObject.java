package com.my1stle.customer.portal.persistence.model;

import com.my1stle.customer.portal.service.model.Installation;
import com.dev1stle.repository.salesforce.model.SObjectApiName;
import com.dev1stle.repository.salesforce.model.SObjectField;
import com.dev1stle.repository.salesforce.model.SalesforceObject;

import java.time.LocalDate;
import java.time.ZonedDateTime;

@SObjectApiName(value = "Installation__c")
public class InstallationSalesforceObject extends SalesforceObject implements Installation {

    @SObjectField(api_name = "Name")
    private String name;

    @SObjectField(api_name = "Opportunity__r.AccountId")
    private String accountId;

    @SObjectField(api_name = "Opportunity__r.Account.PersonContactId")
    private String contactId;

    @SObjectField(api_name = "Opportunity__r.Name")
    private String customerName;

    @SObjectField(api_name = "Email_Address__c", read_only = true)
    private String customerEmailAddress;

    @SObjectField(api_name = "Phone_Number__c", read_only = true)
    private String customerPhoneNumber;

    @SObjectField(api_name = "System_Size__c", read_only = true)
    private Double systemSize;

    @SObjectField(api_name = "Install_Status__c")
    private String installStatus;

    @SObjectField(api_name = "Retail_Marketing_Area__c", read_only = true)
    private String operationalArea;

    @SObjectField(api_name = "Job_Site_Street_Address__c", read_only = true)
    private String address;

    @SObjectField(api_name = "Job_Site__c", read_only = true)
    private String state;

    @SObjectField(api_name = "Job_Site_City__c", read_only = true)
    private String city;

    @SObjectField(api_name = "Zip__c", read_only = true)
    private String zipCode;

    @SObjectField(api_name = "Dropbox_Link_for_Customer__c")
    private String designLink;

    @SObjectField(api_name = "Contract_Close_Date__c", read_only = true)
    private LocalDate contractSignedDate;

    @SObjectField(api_name = "PSA_Scheduled_Date__c", read_only = true)
    private ZonedDateTime psaScheduledDate;

    @SObjectField(api_name = "PSA_Completed_Date__c")
    private ZonedDateTime psaCompleteDate;

    @SObjectField(api_name = "Scheduled_Start_Date__c")
    private LocalDate scheduledStartDate;

    @SObjectField(api_name = "DH_InstallComplete_PendingPTO__c")
    private ZonedDateTime installCompletePendingPto;

    @SObjectField(api_name = "Installation_Date__c")
    private LocalDate installationDate;

    @SObjectField(api_name = "STOP_Submitted_to_Customer__c", read_only = true)
    private LocalDate stopPackageSentDate;

 //   @SObjectField(api_name = "RS_Project_No__c")
 //   private String projectId;

    @SObjectField(api_name = "Opportunity__c")
    private String opportunityId;

    @SObjectField(api_name = "Opportunity__r.Sold_Proposal__c")
    private String soldProposalId;

    @SObjectField(api_name = "Opportunity__r.Sold_Proposal__r.Predesign__c")
    private String soldProposalPredesignId;

    @SObjectField(api_name = "Opportunity__r.Sold_Proposal__r.Financing_Type_Name__c")
    private String paymentTypeName;

    @SObjectField(api_name = "Opportunity__r.Sold_Proposal__r.Provider_Type_Name__c")
    private String paymentProviderName;

    @SObjectField(api_name = "Opportunity__r.Sold_Proposal__r.Product_Type_Name__c")
    private String productTypeName;

    @SObjectField(api_name = "Opportunity__r.Sold_Proposal__r.Total_Contract_Price__c")
    private Double totalContractPrice;

    @SObjectField(api_name = "Opportunity__r.Sold_Proposal__r.SREC_Contract_Type__c")
    private String soldProposalSrecTypeName;

    @SObjectField(api_name = "Opportunity__r.Sold_Proposal__r.SREC_Upfront_Rebate__c")
    private Double soldProposalSrecUpfrontRebate;

    @SObjectField(api_name = "Opportunity__r.Sold_Proposal__r.Srec_Sell_Price__c")
    private Double soldProposalSrecSellPrice;

    @SObjectField(api_name = "Opportunity__r.Sold_Proposal__r.Srec_Sell_De_Escalator__c")
    private Double soldProposalSrecSellDeEscalator;

    @SObjectField(api_name = "Opportunity__r.Sold_Proposal__r.State_Tax_Credit__c")
    private Double soldProposalStateTaxCredit;

    @SObjectField(api_name = "Opportunity__r.Sold_Proposal__r.Tax_Credit_to_be_financed__c")
    private Double soldProposalFederalTaxCredit;

    @SObjectField(api_name = "Opportunity__r.Direct_Sales_Closer_Account__r.Name")
    private String directSalesCloserName;

    @SObjectField(api_name = "Opportunity__r.Direct_Sales_Closer_Account__r.Phone")
    private String directSalesCloserPhone;

    @SObjectField(api_name = "Opportunity__r.Direct_Sales_Closer_Account__r.X1st_Light_Email__c")
    private String directSalesCloserEmail;

    @SObjectField(api_name = "Account_Manager__c")
    private String accountManagerName;

    @SObjectField(api_name = "Account_Manager_Contact_Info__c")
    private String accountManagerPreformattedContactInfo;

    @SObjectField(api_name = "Opportunity__r.Sold_Proposal__r.eSign_Id__c")
    private String firstLightAgreementId;

    @SObjectField(api_name = "Opportunity__r.Sold_Proposal__r.eSign_Status__c")
    private String firstLightAgreementStatus;

    @SObjectField(api_name = "Opportunity__r.Sold_Proposal__r.Utility_eSign_Id__c")
    private String utilityAgreementId;

    @SObjectField(api_name = "Opportunity__r.Sold_Proposal__r.Utility_eSign_Status__c")
    private String utilityAgreementStatus;

    @SObjectField(api_name = "Opportunity__r.Sold_Proposal__r.Financing_eSign_Id__c")
    private String financingAgreementId;

    @SObjectField(api_name = "Opportunity__r.Sold_Proposal__r.Financing_eSign_Status__c")
    private String financingAgreementStatus;

    @SObjectField(api_name = "External_Monitoring_Id__c")
    private String monitoringExternalId;

 /*   @SObjectField(api_name = "Inverter_1_BOM_Master__r.rstk__pebom_compitem__r.rstk__peitem_item__c")
    private String inverter1Name;

    @SObjectField(api_name = "Inverter_2_BOM_Master__r.rstk__pebom_compitem__r.rstk__peitem_item__c")
    private String inverter2Name;

    @SObjectField(api_name = "Inverter_3_BOM_Master__r.rstk__pebom_compitem__r.rstk__peitem_item__c")
    private String inverter3Name;

    @SObjectField(api_name = "Inverter_4_BOM_Master__r.rstk__pebom_compitem__r.rstk__peitem_item__c")
    private String inverter4Name;

   // @SObjectField(api_name = "Inverter_5_BOM_Master__r.rstk__pebom_compitem__r.rstk__peitem_item__c")
   // private String inverter5Name;

    @SObjectField(api_name = "Inverter_6_BOM_Master__r.rstk__pebom_compitem__r.rstk__peitem_item__c")
    private String inverter6Name;

    @SObjectField(api_name = "Inverter_7_BOM_Master__r.rstk__pebom_compitem__r.rstk__peitem_item__c")
    private String inverter7Name;

    @SObjectField(api_name = "Monitor_BOM_Master__r.rstk__pebom_compitem__r.rstk__peitem_item__c")
    private String monitorName;

    @SObjectField(api_name = "BOM_Master__r.rstk__pebom_compitem__r.rstk__peitem_item__c")
    private String panelName;*/

    @SObjectField(api_name = "RS_of_Panels__c", read_only = true)
    private Integer panelCountFromItems;

   // @SObjectField(api_name = "RS_Project_No__r.rstk__pjproj_div__c")
    //private String rootProjectMasterDivisionMasterId;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getAccountId() {
        return accountId;
    }

    @Override
    public String getContactId() {
        return contactId;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getCustomerName() {
        return customerName;
    }

    @Override
    public String getCustomerEmail() {
        return this.customerEmailAddress;
    }

    @Override
    public String getCustomerPhoneNumber() {
        return this.customerPhoneNumber;
    }

    @Override
    public Double getSystemSize() {
        return systemSize;
    }

    @Override
    public String getInstallStatus() {
        return installStatus;
    }

    @Override
    public String getOperationalArea() {
        return this.operationalArea;
    }

    @Override
    public String getAddress() {
        return this.address;
    }

    @Override
    public String getState() {
        return state;
    }

    @Override
    public String getCity() {
        return city;
    }

    @Override
    public String getZipCode() {
        return zipCode;
    }

    @Override
    public String getDesignLink() {
        return designLink;
        //return "https://www.dropbox.com/sh/19uqctmlwa7qqzn/AACZ76EWHdxS2b7W0frf_I78a?dl=0";
    }

    @Override
    public LocalDate getContractSignedDate() {
        return contractSignedDate;
    }

    @Override
    public ZonedDateTime getPsaScheduledDate() {
        return psaScheduledDate;
    }

    @Override
    public ZonedDateTime getPsaCompleteDate() {
        return psaCompleteDate;
    }

    @Override
    public LocalDate getScheduledStartDate() {
        return scheduledStartDate;
    }

    @Override
    public ZonedDateTime getInstallCompletePendingPto() {
        return installCompletePendingPto;
    }

    @Override
    public String getOpportunityId() {
        return opportunityId;
    }

    @Override
    public LocalDate getInstallationDate() {
        return installationDate;
    }

    @Override
    public LocalDate getStopPackageSentDate() {
        return stopPackageSentDate;
    }

    /*@Override
    public String getProjectId() {
        return projectId;
    }*/

    @Override
    public String getSoldProposalId() {
        return soldProposalId;
    }

    @Override
    public String getSoldProposalPredesignId() {
        return soldProposalPredesignId;
    }

    @Override
    public String getPaymentTypeName() {
        return paymentTypeName;
    }

    @Override
    public String getPaymentProviderName() {
        return paymentProviderName;
    }

    @Override
    public String getProductTypeName() {
        return productTypeName;
    }

    @Override
    public String getDirectSalesCloserName() {
        return directSalesCloserName;
    }

    @Override
    public String getDirectSalesCloserPhone() {
        return directSalesCloserPhone;
    }

    @Override
    public String getDirectSalesCloserEmail() {
        return directSalesCloserEmail;
    }

    @Override
    public String getAccountManagerName() {
        return accountManagerName;
    }

    @Override
    public String getAccountManagerPreformattedContactInfo() {
        return accountManagerPreformattedContactInfo;
    }

    @Override
    public Double getTotalContractPrice() {
        return totalContractPrice;
    }

    @Override
    public String getSoldProposalSrecTypeName() {
        return soldProposalSrecTypeName;
    }

    @Override
    public Double getSoldProposalSrecUpfrontRebate() {
        return soldProposalSrecUpfrontRebate;
    }

    @Override
    public Double getSoldProposalSrecSellPrice() {
        return soldProposalSrecSellPrice;
    }

    @Override
    public Double getSoldProposalSrecSellDeEscalator() {
        return soldProposalSrecSellDeEscalator;
    }

    @Override
    public Double getSoldProposalStateTaxCredit() {
        return soldProposalStateTaxCredit;
    }

    @Override
    public Double getSoldProposalFederalTaxCredit() {
        return soldProposalFederalTaxCredit;
    }

    @Override
    public String getFirstLightAgreementId() {
        return firstLightAgreementId;
        //return "3AAABLblqZhC6aMPOz6bzGkpei9m-jpdXojzj7JlWOILvRxY8XFtBds4xMr92Bb-t0ic8tle7fNvrFas_cJ-CqI15XHGLRXvQ";
    }

    @Override
    public String getFirstLightAgreementStatus() {
        return firstLightAgreementStatus;
        //return "SIGNED";
    }

    @Override
    public String getUtilityAgreementId() {
        return utilityAgreementId;
    }

    @Override
    public String getUtilityAgreementStatus() {
        return utilityAgreementStatus;
    }

    @Override
    public String getFinancingAgreementId() {
        return financingAgreementId;
    }

    @Override
    public String getFinancingAgreementStatus() {
        return financingAgreementStatus;
    }

    @Override
    public String getMonitoringExternalId() {
        return monitoringExternalId;
    }

  /*  public String getInverter1Name() {
        return inverter1Name;
    }

    @Override
    public String getInverter2Name() {
        return inverter2Name;
    }

    @Override
    public String getInverter3Name() {
        return inverter3Name;
    }

    @Override
    public String getInverter4Name() {
        return inverter4Name;
    }

 //   @Override
 //   public String getInverter5Name() {
  //      return inverter5Name;
  //  }

    @Override
    public String getInverter6Name() {
        return inverter6Name;
    }

    @Override
    public String getInverter7Name() {
        return inverter7Name;
    }

    @Override
    public String getMonitorName() {
        return monitorName;
    }

    @Override
    public String getPanelName() {
        return panelName;
    }*/

    @Override
    public Integer getPanelCountFromItems() {
        return panelCountFromItems;
    }

    /*@Override
    public String getRootstockProjectMasterDivisionMasterId() {
        return rootProjectMasterDivisionMasterId;
    }*/
}
