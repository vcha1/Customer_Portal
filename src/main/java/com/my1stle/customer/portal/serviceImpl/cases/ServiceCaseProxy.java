package com.my1stle.customer.portal.serviceImpl.cases;

import com.my1stle.customer.portal.service.model.ServiceCase;
import com.my1stle.customer.portal.service.model.ServiceCaseAttachment;
import com.my1stle.customer.portal.service.model.ServiceCaseComment;
import com.my1stle.customer.portal.service.serviceapi.ExistingServiceCaseDto;
import com.my1stle.customer.portal.util.Visitor;

import java.time.*;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * TODO
 */
class ServiceCaseProxy implements ServiceCase, Consumer<Visitor<ServiceCaseProxy>> {

    private String id;
    private String externalId;
    private String caseNumber;
    private String status;
    private String subject1;
    private String subject2;
    private String customerDescription;
    private String installSite;
    private LocalDateTime scheduledServiceDate;

    private String odooId;

    // TODO
    private String subject;
    private String problemType;
    private LocalDate caseReceivedDate;
    private String recordTypeId;
    private String caseDepartment;
    private String billingStatus;
    private Double costToClient;
    private Double depositAmount;
    private LocalDate depositReceived;
    private LocalDate paymentBilledDate;
    private LocalDate paymentReceivedDate;

    // Lazy loaders
    private Supplier<String> installSiteSupplier;
    private Supplier<String> subject1Supplier;
    private Supplier<String> subject2Supplier;

    private Supplier<List<ServiceCaseAttachment>> serviceCaseAttachmentSupplier;
    private Supplier<List<ServiceCaseComment>> serviceCaseCommentSupplier;

    private Long issueTypeId;
    private Long subIssueTypeId;

    private ServiceCaseProxy() {

    }

    static ServiceCaseProxy from(ExistingServiceCaseDto existingServiceCaseDto) {
        ServiceCaseProxy proxy = new ServiceCaseProxy();
        proxy.id = String.valueOf(existingServiceCaseDto.getId());
        proxy.externalId = existingServiceCaseDto.getExternalId();
        proxy.caseNumber = String.valueOf(existingServiceCaseDto.getId());
        proxy.status = existingServiceCaseDto.getStatus().toValue();
        proxy.customerDescription = existingServiceCaseDto.getDescription();

        proxy.issueTypeId = existingServiceCaseDto.getIssueType();
        proxy.subIssueTypeId = existingServiceCaseDto.getSubIssueType();
        proxy.scheduledServiceDate = existingServiceCaseDto.getAdditionalDetail().getScheduledServiceDate();
        proxy.odooId = existingServiceCaseDto.getOdooId();
        return proxy;

    }

    String getExternalId() {
        return externalId;
    }

    String getOdooId(){
        return odooId;
    }

    Long getIssueTypeId() {
        return issueTypeId;
    }

    Long getSubIssueTypeId() {
        return subIssueTypeId;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getCaseNumber() {
        return caseNumber;
    }

    @Override
    public String getRecordTypeId() {
        return recordTypeId;
    }

    @Override
    public String getInstallSite() {
        if (null == installSite && installSiteSupplier != null) {
            installSite = installSiteSupplier.get();
        }
        return installSite;

    }

    @Override
    public String getSubject() {
        return subject;
    }

    @Override
    public String getStatus() {
        return status;
    }

    @Override
    public String getSubject1() {

        if (null == subject1 && subject1Supplier != null) {
            subject1 = subject1Supplier.get();
        }

        return subject1;
    }

    @Override
    public String getSubject2() {

        if (null == subject2 && subject2Supplier != null) {
            subject2 = subject2Supplier.get();
        }

        return subject2;
    }

    @Override
    public String getCustomerDescription() {
        return customerDescription;
    }

    @Override
    public String getProblemType() {
        return problemType;
    }

    @Override
    public String getDescription() {
        return customerDescription;
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
        return scheduledServiceDate;
    }

    @Override
    public List<ServiceCaseAttachment> getServiceCaseAttachments() {

        if (serviceCaseAttachmentSupplier != null) {
            return serviceCaseAttachmentSupplier.get();
        }

        return Collections.emptyList();

    }

    @Override
    public List<ServiceCaseComment> getServiceCaseComments() {

        if (serviceCaseCommentSupplier != null) {
            return serviceCaseCommentSupplier.get();
        }

        return Collections.emptyList();
    }

    /**
     * Performs this operation on the given argument.
     *
     * @param serviceCaseProxyVisitor the input argument
     */
    @Override
    public void accept(Visitor<ServiceCaseProxy> serviceCaseProxyVisitor) {
        serviceCaseProxyVisitor.visit(this);
    }

    void setInstallSiteSupplier(Supplier<String> installSiteSupplier) {
        this.installSiteSupplier = installSiteSupplier;
    }

    void setSubject1Supplier(Supplier<String> subject1Supplier) {
        this.subject1Supplier = subject1Supplier;
    }

    void setSubject2Supplier(Supplier<String> subject2Supplier) {
        this.subject2Supplier = subject2Supplier;
    }

    void setServiceCaseAttachmentSupplier(Supplier<List<ServiceCaseAttachment>> serviceCaseAttachmentSupplier) {
        this.serviceCaseAttachmentSupplier = serviceCaseAttachmentSupplier;
    }

    void setServiceCaseCommentSupplier(Supplier<List<ServiceCaseComment>> serviceCaseCommentSupplier) {
        this.serviceCaseCommentSupplier = serviceCaseCommentSupplier;
    }
}