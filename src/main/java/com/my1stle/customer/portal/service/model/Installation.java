package com.my1stle.customer.portal.service.model;

import java.time.LocalDate;
import java.time.ZonedDateTime;

public interface Installation {

    String getName() ;
    String getAccountId();
    String getContactId();
    String getCustomerName();
    String getId() ;
    String getAddress();
    String getState();
    String getCity();
    String getZipCode();
    String getOperationalArea();
    String getCustomerEmail() ;
    String getCustomerPhoneNumber();
    Double getSystemSize();
    String getInstallStatus();
    String getDesignLink();
    ZonedDateTime getPsaScheduledDate();
    LocalDate getInstallationDate();
    LocalDate getStopPackageSentDate();
//    String getProjectId();
    String getFirstLightAgreementId();
    String getFirstLightAgreementStatus();
    String getUtilityAgreementId();
    String getUtilityAgreementStatus();
    String getFinancingAgreementId();
    String getFinancingAgreementStatus();
    String getMonitoringExternalId() ;
    LocalDate getContractSignedDate();
    ZonedDateTime getPsaCompleteDate();
    LocalDate getScheduledStartDate();
    ZonedDateTime getInstallCompletePendingPto();
    String getOpportunityId();
    String getSoldProposalId();
    String getSoldProposalPredesignId();
    String getPaymentTypeName();
    String getPaymentProviderName();
    String getProductTypeName();
    String getDirectSalesCloserName();
    String getDirectSalesCloserPhone();
    String getDirectSalesCloserEmail();
    String getAccountManagerName();
    String getAccountManagerPreformattedContactInfo();
    Double getTotalContractPrice();
    String getSoldProposalSrecTypeName();
    Double getSoldProposalSrecUpfrontRebate();
    Double getSoldProposalSrecSellPrice();
    Double getSoldProposalSrecSellDeEscalator();
    Double getSoldProposalStateTaxCredit();
    Double getSoldProposalFederalTaxCredit();
    /*String getInverter1Name();
    String getInverter2Name();
    String getInverter3Name();
    String getInverter4Name();
    String getInverter5Name();
    String getInverter6Name();
    String getInverter7Name();
    String getMonitorName();
    String getPanelName();*/
    Integer getPanelCountFromItems();
    //String getRootstockProjectMasterDivisionMasterId();

}
