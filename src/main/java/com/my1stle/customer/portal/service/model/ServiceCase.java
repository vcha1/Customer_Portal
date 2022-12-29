package com.my1stle.customer.portal.service.model;

import java.time.*;
import java.util.Collections;
import java.util.List;

public interface ServiceCase {

    String getId();

    String getCaseNumber();

    String getRecordTypeId();

    String getInstallSite();

    String getSubject();

    String getStatus();

    String getSubject1();

    String getSubject2();

    String getCustomerDescription();

    String getProblemType();

    String getDescription();

    LocalDate getCaseReceivedDate();

    String getCaseDepartment();

    String getBillingStatus();

    Double getCostToClient();

    Double getDepositAmount();

    LocalDate getDepositReceived();

    LocalDate getPaymentBilledDate();

    LocalDate getPaymentReceivedDate();

    LocalDateTime getScheduledServiceDate();

    default List<ServiceCaseAttachment> getServiceCaseAttachments() {
        return Collections.emptyList();
    }

    default List<ServiceCaseComment> getServiceCaseComments() {
        return Collections.emptyList();
    }

}
