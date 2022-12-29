package com.my1stle.customer.portal.serviceImpl.servicerequest;

import com.dev1stle.repository.result.Result;
import com.dev1stle.repository.salesforce.SalesforceObjectRepository;
import com.my1stle.customer.portal.service.model.Installation;
import com.my1stle.customer.portal.service.model.PaymentDetail;
import com.my1stle.customer.portal.service.model.Product;
import com.my1stle.customer.portal.service.model.ServiceRequest;
import com.my1stle.customer.portal.service.odoo.OdooCaseDTO;
import com.my1stle.customer.portal.service.odoo.OdooInstallationData;
import com.my1stle.customer.portal.service.pricing.ServiceRequestPricingService;
import com.my1stle.customer.portal.service.servicerequest.ServiceRequestScheduler;
import com.my1stle.customer.portal.service.util.OperationalArea;
import com.my1stle.customer.portal.service.util.TimeZone;
import com.my1stle.customer.portal.web.exception.InternalServerErrorException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Unit Test for {@link SalesforceServiceCaseServiceRequestScheduler}
 */
@RunWith(MockitoJUnitRunner.class)
public class SalesforceServiceCaseServiceRequestSchedulerTest {

    private ServiceRequestScheduler serviceRequestScheduler;

    @Mock
    private SalesforceObjectRepository<SalesforceServiceCase> mockRepository;

    @Mock
    private ServiceRequestPricingService mockServiceRequestPricingService;

    @Captor
    private ArgumentCaptor<List<SalesforceServiceCase>> captor;


    @Before
    public void setUp() throws Exception {

        this.serviceRequestScheduler = new SalesforceServiceCaseServiceRequestScheduler(mockServiceRequestPricingService, mockRepository);
    }

    @Test
    public void shouldCreateSalesforceServiceRequestBasedOnPaidServiceRequest() {

        String accountId = "some account id";
        String contactId = "some contact id";
        String opportunityId = "some opportunity id";
        String installationId = "some installation id";
        String productName = "Acme Product";
        BigDecimal costToClient = BigDecimal.valueOf(100.00);
        ZonedDateTime paymentDate = LocalDateTime.of(2019, 11, 19, 0, 0, 0)
                .atZone(TimeZone.SALESFORCE_1ST_LIGHT_ENERGY);

        ServiceRequest serviceRequest = mock(ServiceRequest.class);
        //Installation stubbedInstallation = mock(Installation.class);
        OdooInstallationData stubbedInstallation = mock(OdooInstallationData.class);
        Product stubbedProduct = mock(Product.class);
        PaymentDetail stubbedPaymentDetail = mock(PaymentDetail.class);
        Result stubbedResult = mock(Result.class);

        // Stubbing
        when(serviceRequest.getPaymentDetail()).thenReturn(stubbedPaymentDetail);
        //when(serviceRequest.getInstallation()).thenReturn(stubbedInstallation);
        when(serviceRequest.getOdooInstallationData()).thenReturn(stubbedInstallation);
        when(serviceRequest.getProduct()).thenReturn(stubbedProduct);
        when(serviceRequest.getQuantity()).thenReturn(1);
        when(stubbedPaymentDetail.getCreatedDate()).thenReturn(paymentDate);
        when(stubbedPaymentDetail.getAmount()).thenReturn(costToClient);
        when(mockServiceRequestPricingService.getSubTotalAmount(any(ServiceRequest.class))).thenReturn(costToClient);
        when(mockServiceRequestPricingService.getRequiredDepositAmount(any(ServiceRequest.class))).thenReturn(BigDecimal.ZERO);
        when(stubbedInstallation.getAccountId()).thenReturn(accountId);
        when(stubbedInstallation.getContactId()).thenReturn(contactId);
        when(stubbedInstallation.getOpportunityId()).thenReturn(opportunityId);
        when(stubbedInstallation.getId()).thenReturn(installationId);
        when(stubbedInstallation.getOperationalArea()).thenReturn(OperationalArea.CAILFORNIA_MANTECA);
        when(stubbedProduct.getName()).thenReturn(productName);
        when(mockRepository.insert(anyList())).thenReturn(stubbedResult);
        when(stubbedResult.isSuccessful()).thenReturn(true);

        // When
        this.serviceRequestScheduler.scheduleServiceRequest(serviceRequest);

        // Then
        verify(mockServiceRequestPricingService).getSubTotalAmount(any(ServiceRequest.class));
        verify(mockServiceRequestPricingService).getRequiredDepositAmount(any(ServiceRequest.class));
        verify(mockRepository).insert(captor.capture());

        List<SalesforceServiceCase> value = captor.getValue();

        assertEquals(1, value.size()); // only 1 Service Case inserted

        SalesforceServiceCase salesforceServiceCase = value.get(0);

        assertEquals(accountId, salesforceServiceCase.getAccountId());
        assertEquals(contactId, salesforceServiceCase.getContactId());
        assertEquals(opportunityId, salesforceServiceCase.getOpportunityId());
        assertEquals(installationId, salesforceServiceCase.getInstallationId());
        assertEquals(SalesforceServiceCaseRecordType.WESTERN_SERVICE_AREA, salesforceServiceCase.getRecordTypeId());
        assertEquals(SalesforceServiceCaseSubject.NEW_SERVICE, salesforceServiceCase.getSubject1());
        assertEquals(productName, salesforceServiceCase.getSubject2());
        assertEquals(SalesforceServiceCaseStatus.SCHEDULING, salesforceServiceCase.getStatus());
        assertEquals(SalesforceServiceCaseProblemType.BILLABLE_SERVICE, salesforceServiceCase.getProblemType());
        assertEquals(SalesforceServiceCaseBillingStatus.BILL_CUSTOMER, salesforceServiceCase.getBillingStatus());
        assertEquals(String.format("Customer purchased service on customer portal : %s \nQuantity : %s", productName, 1), salesforceServiceCase.getDescription());
        assertEquals(paymentDate.toLocalDate(), salesforceServiceCase.getCaseReceivedDate());
        assertEquals(paymentDate.toLocalDate(), salesforceServiceCase.getPaymentBilledDate());
        assertEquals(paymentDate.toLocalDate(), salesforceServiceCase.getPaymentReceivedDate());
        assertEquals(SalesforceServiceCaseDepartment.TECHNICAL_SERVICE, salesforceServiceCase.getCaseDepartment());
        assertEquals(Double.valueOf(costToClient.doubleValue()), salesforceServiceCase.getCostToClient());
        assertNull(salesforceServiceCase.getDepositAmount());
        assertNull(salesforceServiceCase.getDepositReceived());

    }

    @Test
    public void shouldThrowInternalServiceErrorExceptionWhenUnableToInsertIntoSalesforce() {

        String accountId = "some account id";
        String contactId = "some contact id";
        String opportunityId = "some opportunity id";
        String installationId = "some installation id";
        String productName = "Acme Product";
        BigDecimal costToClient = BigDecimal.valueOf(100.00);
        ZonedDateTime paymentDate = LocalDateTime.of(2019, 11, 19, 0, 0, 0)
                .atZone(TimeZone.SALESFORCE_1ST_LIGHT_ENERGY);

        ServiceRequest serviceRequest = mock(ServiceRequest.class);
        //Installation stubbedInstallation = mock(Installation.class);
        OdooInstallationData stubbedInstallation = mock(OdooInstallationData.class);
        Product stubbedProduct = mock(Product.class);
        PaymentDetail stubbedPaymentDetail = mock(PaymentDetail.class);
        Result stubbedResult = mock(Result.class);

        // Stubbing
        when(serviceRequest.getPaymentDetail()).thenReturn(stubbedPaymentDetail);
        when(serviceRequest.getOdooInstallationData()).thenReturn(stubbedInstallation);
        when(serviceRequest.getProduct()).thenReturn(stubbedProduct);
        when(serviceRequest.getQuantity()).thenReturn(1);
        when(stubbedPaymentDetail.getCreatedDate()).thenReturn(paymentDate);
        when(stubbedPaymentDetail.getAmount()).thenReturn(costToClient);
        when(mockServiceRequestPricingService.getSubTotalAmount(any(ServiceRequest.class))).thenReturn(costToClient);
        when(mockServiceRequestPricingService.getRequiredDepositAmount(any(ServiceRequest.class))).thenReturn(BigDecimal.ZERO);
        when(stubbedInstallation.getAccountId()).thenReturn(accountId);
        when(stubbedInstallation.getContactId()).thenReturn(contactId);
        when(stubbedInstallation.getOpportunityId()).thenReturn(opportunityId);
        when(stubbedInstallation.getId()).thenReturn(installationId);
        when(stubbedInstallation.getOperationalArea()).thenReturn(OperationalArea.CAILFORNIA_MANTECA);
        when(stubbedProduct.getName()).thenReturn(productName);
        when(mockRepository.insert(anyList())).thenReturn(stubbedResult);
        when(stubbedResult.isSuccessful()).thenReturn(false);
        when(stubbedResult.getErrorMessages()).thenReturn(Collections.singletonList("Stubbed Salesforce Error! Please Ignore..."));

        // When
        try {

            this.serviceRequestScheduler.scheduleServiceRequest(serviceRequest);

        } catch (InternalServerErrorException e) {

            // Then
            verify(mockServiceRequestPricingService).getSubTotalAmount(any(ServiceRequest.class));
            verify(mockServiceRequestPricingService).getRequiredDepositAmount(any(ServiceRequest.class));
            verify(mockRepository).insert(captor.capture());

            List<SalesforceServiceCase> value = captor.getValue();

            assertEquals(1, value.size()); // only 1 Service Case inserted

            SalesforceServiceCase salesforceServiceCase = value.get(0);

            assertEquals(accountId, salesforceServiceCase.getAccountId());
            assertEquals(contactId, salesforceServiceCase.getContactId());
            assertEquals(opportunityId, salesforceServiceCase.getOpportunityId());
            assertEquals(installationId, salesforceServiceCase.getInstallationId());
            assertEquals(SalesforceServiceCaseRecordType.WESTERN_SERVICE_AREA, salesforceServiceCase.getRecordTypeId());
            assertEquals(SalesforceServiceCaseSubject.NEW_SERVICE, salesforceServiceCase.getSubject1());
            assertEquals(productName, salesforceServiceCase.getSubject2());
            assertEquals(SalesforceServiceCaseStatus.SCHEDULING, salesforceServiceCase.getStatus());
            assertEquals(SalesforceServiceCaseProblemType.BILLABLE_SERVICE, salesforceServiceCase.getProblemType());
            assertEquals(SalesforceServiceCaseBillingStatus.BILL_CUSTOMER, salesforceServiceCase.getBillingStatus());
            assertEquals(String.format("Customer purchased service on customer portal : %s \nQuantity : %s", productName, 1), salesforceServiceCase.getDescription());
            assertEquals(paymentDate.toLocalDate(), salesforceServiceCase.getCaseReceivedDate());
            assertEquals(paymentDate.toLocalDate(), salesforceServiceCase.getPaymentBilledDate());
            assertEquals(paymentDate.toLocalDate(), salesforceServiceCase.getPaymentReceivedDate());
            assertEquals(SalesforceServiceCaseDepartment.TECHNICAL_SERVICE, salesforceServiceCase.getCaseDepartment());
            assertEquals(Double.valueOf(costToClient.doubleValue()), salesforceServiceCase.getCostToClient());
            assertNull(salesforceServiceCase.getDepositAmount());
            assertNull(salesforceServiceCase.getDepositReceived());

            return;

        }

        fail(String.format("%s was expected to be thrown!", InternalServerErrorException.class.getSimpleName()));


    }
}