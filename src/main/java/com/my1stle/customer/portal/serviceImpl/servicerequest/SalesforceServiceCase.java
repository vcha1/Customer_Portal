package com.my1stle.customer.portal.serviceImpl.servicerequest;

import com.dev1stle.repository.salesforce.model.SObjectApiName;
import com.dev1stle.repository.salesforce.model.SObjectField;
import com.dev1stle.repository.salesforce.model.SalesforceObject;
import com.my1stle.customer.portal.service.model.ServiceCase;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.time.*;

@SObjectApiName(value = "Case")
public class SalesforceServiceCase extends SalesforceObject implements ServiceCase {

    @SObjectField(api_name = "CaseNumber", read_only = true)
    private String caseNumber;

    @SObjectField(api_name = "RecordTypeId")
    private String recordTypeId;

    @SObjectField(api_name = "AccountId")
    private String accountId;

    @SObjectField(api_name = "ContactId")
    private String contactId;

    @SObjectField(api_name = "Installation__c")
    private String installationId;

    @SObjectField(api_name = "Installation__r.Job_Site_Street_Address__c")
    private String installSite;

    @SObjectField(api_name = "Opportunity__c")
    private String opportunityId;

    @SObjectField(api_name = "Subject")
    private String subject;

    @SObjectField(api_name = "Subject__c")
    private String subject1;

    @SObjectField(api_name = "Subject2__c")
    private String subject2;

    @SObjectField(api_name = "Status")
    private String status;

    @SObjectField(api_name = "Problem_Type__c")
    private String problemType;

    @SObjectField(api_name = "Description")
    private String description;

    @SObjectField(api_name = "Case_Received__c")
    private LocalDate caseReceivedDate;

    @SObjectField(api_name = "Case_Department__c")
    private String caseDepartment;

    @SObjectField(api_name = "Billing_Status__c")
    private String billingStatus;

    @SObjectField(api_name = "Cost_to_Client__c")
    private Double costToClient;

    @SObjectField(api_name = "Payment_Billed__c")
    private LocalDate paymentBilledDate;

    @SObjectField(api_name = "Payment_Received__c")
    private LocalDate paymentReceivedDate;

    @SObjectField(api_name = "Deposit_Amount__c")
    private Double depositAmount;

    @SObjectField(api_name = "Deposit_Received__c")
    private LocalDate depositReceived;

    @SObjectField(api_name = "Pre_Installation_Issue__c")
    private String preInstallationIssue;

    @SObjectField(api_name = "Customer_Description__c")
    private String customerDescription;

    public static class Builder {

        private String accountId;
        private String contactId;
        private String installationId;
        private String opportunityId;
        private String recordTypeId;
        private String subject;
        private String subject1;
        private String subject2;
        private String status;
        private String problemType;
        private String description;
        private LocalDate caseReceivedDate;
        private String caseDepartment;
        private String billingStatus;
        private Double costToClient;
        private LocalDate paymentBilledDate;
        private LocalDate paymentReceivedDate;
        private Double depositAmount;
        private LocalDate depositReceived;
        private String customerDescription;

        public Builder(String accountId, String contactId, String installationId, String opportunityId) {
            this.accountId = accountId;
            this.contactId = contactId;
            this.installationId = installationId;
            this.opportunityId = opportunityId;
        }

        public Builder recordTypeId(String recordTypeId) {
            this.recordTypeId = recordTypeId;
            return this;
        }

        public Builder subject(String subject) {
            this.subject = subject;
            return this;
        }

        public Builder subject1(String subject1) {
            this.subject1 = subject1;
            return this;
        }

        public Builder subject2(String subject2) {
            this.subject2 = subject2;
            return this;
        }

        public Builder status(String status) {
            this.status = status;
            return this;
        }

        public Builder problemType(String problemType) {
            this.problemType = problemType;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder caseReceivedDate(LocalDate caseReceivedDate) {
            this.caseReceivedDate = caseReceivedDate;
            return this;
        }

        public Builder caseDepartment(String caseDepartment) {
            this.caseDepartment = caseDepartment;
            return this;
        }

        public Builder billingStatus(String billingStatus) {
            this.billingStatus = billingStatus;
            return this;
        }

        public Builder costToClient(Double costToClient) {
            this.costToClient = costToClient;
            return this;
        }

        public Builder paymentBilledDate(LocalDate paymentBilledDate) {
            this.paymentBilledDate = paymentBilledDate;
            return this;
        }

        public Builder paymentReceivedDate(LocalDate paymentReceivedDate) {
            this.paymentReceivedDate = paymentReceivedDate;
            return this;
        }

        public Builder depositAmount(Double depositAmount) {
            this.depositAmount = depositAmount;
            return this;
        }

        public Builder depositReceived(LocalDate depositReceived) {
            this.depositReceived = depositReceived;
            return this;
        }

        public Builder customerDescription(String customerDescription) {
            this.customerDescription = customerDescription;
            return this;
        }


        public SalesforceServiceCase build() {
            return new SalesforceServiceCase(this);
        }

    }

    // Constructors
    private SalesforceServiceCase() {

    }

    private SalesforceServiceCase(Builder builder) {

        this.accountId = builder.accountId;
        this.contactId = builder.contactId;
        this.opportunityId = builder.opportunityId;
        this.installationId = builder.installationId;
        this.recordTypeId = builder.recordTypeId;
        this.subject = builder.subject;
        this.subject1 = builder.subject1;
        this.subject2 = builder.subject2;
        this.status = builder.status;
        this.problemType = builder.problemType;
        this.description = builder.description;
        this.caseReceivedDate = builder.caseReceivedDate;
        this.billingStatus = builder.billingStatus;
        this.caseDepartment = builder.caseDepartment;
        this.costToClient = builder.costToClient;
        this.paymentReceivedDate = builder.paymentReceivedDate;
        this.paymentBilledDate = builder.paymentBilledDate;
        this.depositAmount = builder.depositAmount;
        this.depositReceived = builder.depositReceived;
        this.customerDescription = builder.customerDescription;

    }

    // Getters
    @Override
    public String getCaseNumber() {
        return caseNumber;
    }

    @Override
    public String getRecordTypeId() {
        return recordTypeId;
    }

    public String getAccountId() {
        return accountId;
    }

    public String getContactId() {
        return contactId;
    }

    public String getInstallationId() {
        return installationId;
    }

    public String getOpportunityId() {
        return opportunityId;
    }

    public String getCaseSubject() {
        return subject;
    }

    @Override
    public String getSubject() {
        return String.format("%s : %s", subject1, subject2);
    }

    @Override
    public String getSubject1() {
        return subject1;
    }

    @Override
    public String getSubject2() {
        return subject2;
    }

    @Override
    public String getInstallSite() {
        return installSite;
    }

    @Override
    public String getStatus() {
        return status;
    }

    @Override
    public String getProblemType() {
        return problemType;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public LocalDate getCaseReceivedDate() {
        return caseReceivedDate;
    }

    @Override
    public String getCaseDepartment() {
        return caseDepartment;
    }

    @Override
    public String getBillingStatus() {
        return billingStatus;
    }

    @Override
    public Double getCostToClient() {
        return costToClient;
    }

    @Override
    public Double getDepositAmount() {
        return depositAmount;
    }

    @Override
    public LocalDate getDepositReceived() {
        return depositReceived;
    }

    @Override
    public LocalDate getPaymentBilledDate() {
        return paymentBilledDate;
    }

    @Override
    public LocalDate getPaymentReceivedDate() {
        return paymentReceivedDate;
    }

    @Override
    public LocalDateTime getScheduledServiceDate() {
        return null;
    }

    @Override
    public String getCustomerDescription() {
        return customerDescription;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof SalesforceServiceCase)) return false;

        SalesforceServiceCase that = (SalesforceServiceCase) o;

        return new EqualsBuilder()
                .append(this.getId(), that.getId())
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(caseNumber)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.JSON_STYLE)
                .append("id", this.getId())
                .append("createdDate", this.getCreatedDate())
                .append("lastModifiedDate", this.getLastModifiedDate())
                .append("caseNumber", caseNumber)
                .append("recordTypeId", recordTypeId)
                .append("accountId", accountId)
                .append("installationId", installationId)
                .append("opportunityId", opportunityId)
                .append("subject", subject)
                .append("subject1", subject1)
                .append("subject2", subject2)
                .append("status", status)
                .append("problemType", problemType)
                .append("description", description)
                .append("caseReceivedDate", caseReceivedDate)
                .append("caseDepartment", caseDepartment)
                .append("billingStatus", billingStatus)
                .append("costToClient", costToClient)
                .append("paymentBilledDate", paymentBilledDate)
                .append("paymentReceivedDate", paymentReceivedDate)
                .append("customerDescription", customerDescription)
                .toString();
    }
}