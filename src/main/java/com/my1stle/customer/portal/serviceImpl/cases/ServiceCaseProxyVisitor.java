package com.my1stle.customer.portal.serviceImpl.cases;

import com.amazonaws.services.s3.AmazonS3;
import com.my1stle.customer.portal.service.InstallationService;
import com.my1stle.customer.portal.service.model.Installation;
import com.my1stle.customer.portal.service.serviceapi.ServiceApiCategory;
import com.my1stle.customer.portal.service.serviceapi.ServiceCasesApi;
import com.my1stle.customer.portal.service.serviceapi.ServiceUsersApi;
import com.my1stle.customer.portal.util.Visitor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
class ServiceCaseProxyVisitor implements Visitor<ServiceCaseProxy> {

    private final InstallationService installationService;
    private final ServiceCasesApi serviceCasesApi;
    private final ServiceUsersApi serviceUsersApi;
    private final AmazonS3 amazonS3;

    @Autowired
    ServiceCaseProxyVisitor(
            @Qualifier("publicInstallationService") InstallationService installationService,
            ServiceCasesApi serviceCasesApi,
            ServiceUsersApi serviceUsersApi,
            AmazonS3 amazonS3) {
        this.installationService = installationService;
        this.serviceCasesApi = serviceCasesApi;
        this.serviceUsersApi = serviceUsersApi;
        this.amazonS3 = amazonS3;
    }

    @Override
    public void visit(ServiceCaseProxy serviceCaseProxy) {

        serviceCaseProxy.setInstallSiteSupplier(() -> {
            Installation installationById = this.installationService.getInstallationById(serviceCaseProxy.getExternalId());
            return installationById.getAddress();
        });

        Long issueTypeId = serviceCaseProxy.getIssueTypeId();
        Long subIssueTypeId = serviceCaseProxy.getSubIssueTypeId();

        if (issueTypeId != null) {
            serviceCaseProxy.setSubject1Supplier(() -> {
                ServiceApiCategory category = ServiceApiCategory.valueOf(issueTypeId);
                return category == null ? null : category.getLabel();
            });
        }
        if (subIssueTypeId != null) {
            serviceCaseProxy.setSubject2Supplier(() -> {
                ServiceApiCategory category = ServiceApiCategory.valueOf(subIssueTypeId);
                return category == null ? null : category.getLabel();
            });
        }
        String id = serviceCaseProxy.getId();
        if (StringUtils.isNumeric(id)) {
            serviceCaseProxy.setServiceCaseAttachmentSupplier(new AWSS3ServiceCaseAttachmentSupplier(Long.valueOf(id), this.serviceCasesApi, this.amazonS3));
            serviceCaseProxy.setServiceCaseCommentSupplier(new ServiceCaseCommentSupplier(Long.valueOf(id), this.serviceCasesApi, this.serviceUsersApi));
        }


    }

}