package com.my1stle.customer.portal.serviceImpl.servicerequest;

import com.dev1stle.repository.result.Result;
import com.dev1stle.repository.salesforce.SalesforceObjectRepository;
import com.my1stle.customer.portal.service.model.Installation;
import com.my1stle.customer.portal.service.model.PaymentDetail;
import com.my1stle.customer.portal.service.model.Product;
import com.my1stle.customer.portal.service.model.ServiceRequest;
import com.my1stle.customer.portal.service.odoo.OdooInstallationData;
import com.my1stle.customer.portal.service.pricing.ServiceRequestPricingService;
import com.my1stle.customer.portal.service.servicerequest.ServiceRequestScheduler;
import com.my1stle.customer.portal.service.util.TimeZone;
import com.my1stle.customer.portal.web.exception.InternalServerErrorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Objects;

@Service
public class SalesforceServiceCaseServiceRequestScheduler implements ServiceRequestScheduler {

    private ServiceRequestPricingService serviceRequestPricingService;
    private SalesforceObjectRepository<SalesforceServiceCase> repository;

    @Autowired
    public SalesforceServiceCaseServiceRequestScheduler(
            ServiceRequestPricingService serviceRequestPricingService,
            SalesforceObjectRepository<SalesforceServiceCase> repository) {
        this.serviceRequestPricingService = serviceRequestPricingService;
        this.repository = repository;
    }

    /**
     * Schedules a service job based on what the user has requested
     *
     * @param serviceRequest non-null serviceRequest representing what the user has request with required installation, product, and payment details
     * @throws InternalServerErrorException when unable to insert to Salesforce
     * @implNote this implementation creates a Service Case within Salesforce.
     * @see <a href="https://jira.1stlightenergy.com/browse/HD-12373">HD-12373</a>
     */
    @Override
    public void scheduleServiceRequest(ServiceRequest serviceRequest) {

        Objects.requireNonNull(serviceRequest, "Expected nonnull service request");

        //Installation installation = serviceRequest.getInstallation();
        OdooInstallationData odooInstallationData = serviceRequest.getOdooInstallationData();
        Product product = serviceRequest.getProduct();
        PaymentDetail paymentDetail = serviceRequest.getPaymentDetail();
        int quantity = serviceRequest.getQuantity();

        //Objects.requireNonNull(installation, "Expected nonnull installation");
        Objects.requireNonNull(odooInstallationData, "Expected nonnull installation");
        Objects.requireNonNull(product, "Expected nonnull product");
        Objects.requireNonNull(paymentDetail, "Expected nonnull payment detail");

        //String accountId = installation.getAccountId();
        //String contactId = installation.getContactId();
        //String opportunityId = installation.getOpportunityId();
        //String installationId = installation.getId();

        String accountId = odooInstallationData.getAccountId();
        String contactId = odooInstallationData.getContactId();
        String opportunityId = odooInstallationData.getOpportunityId();
        String installationId = odooInstallationData.getId().toString();

        LocalDate date = paymentDetail
                .getCreatedDate()
                .withZoneSameInstant(TimeZone.SALESFORCE_1ST_LIGHT_ENERGY) // Every user in 1st Light Energy Org is in
                .toLocalDate();

        final BigDecimal costToClient = this.serviceRequestPricingService.getSubTotalAmount(serviceRequest);
        final BigDecimal requiredDepositAmount = this.serviceRequestPricingService.getRequiredDepositAmount(serviceRequest);
        final BigDecimal amountPaid = paymentDetail.getAmount();
        final boolean paidInFull = amountPaid.compareTo(costToClient) >= 0;

        SalesforceServiceCase salesforceServiceCase = new SalesforceServiceCase.Builder(accountId, contactId, installationId, opportunityId)
                //.recordTypeId(SalesforceServiceCaseRecordType.getByOperationalArea(installation.getOperationalArea()))
                .recordTypeId(SalesforceServiceCaseRecordType.getByOperationalArea(odooInstallationData.getOperationalArea()))
                .subject1(SalesforceServiceCaseSubject.NEW_SERVICE)
                .subject2(product.getName())
                .status(SalesforceServiceCaseStatus.SCHEDULING)
                .problemType(SalesforceServiceCaseProblemType.BILLABLE_SERVICE)
                .billingStatus(SalesforceServiceCaseBillingStatus.BILL_CUSTOMER)
                .description(String.format("Customer purchased service on customer portal : %s \nQuantity : %s", product.getName(), quantity))
                .caseReceivedDate(date)
                .paymentBilledDate(paidInFull ? date : null) // Payment Billed: Date we collected FULL payment from Customer (same day case was made)
                .paymentReceivedDate(paidInFull ? date : null) // Payment Received: Date we collected FULL payment from Customer (same day case was made)
                .depositAmount(!paidInFull ? amountPaid.doubleValue() : null)
                .depositReceived(!paidInFull ? date : null)
                .caseDepartment(SalesforceServiceCaseDepartment.TECHNICAL_SERVICE)
                .costToClient(costToClient.doubleValue())
                .build();

        Result insert = this.repository.insert(Collections.singletonList(salesforceServiceCase));

        if (!insert.isSuccessful()) {
            throw new InternalServerErrorException(String.format("Unable to insert SF Service Case! %s", String.join("\n", insert.getErrorMessages())));
        }

    }

}
