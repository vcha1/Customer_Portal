package com.my1stle.customer.portal.serviceImpl.cases;

import com.amazonaws.services.s3.AmazonS3;
import com.my1stle.customer.portal.service.model.ServiceCaseAttachment;
import com.my1stle.customer.portal.service.serviceapi.ServiceApiException;
import com.my1stle.customer.portal.service.serviceapi.ServiceCasesApi;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

class AWSS3ServiceCaseAttachmentSupplier implements Supplier<List<ServiceCaseAttachment>> {

    private long id;
    private ServiceCasesApi serviceCasesApi;
    private AmazonS3 amazonS3;

    AWSS3ServiceCaseAttachmentSupplier(long id, ServiceCasesApi serviceCasesApi, AmazonS3 amazonS3) {
        this.id = id;
        this.serviceCasesApi = serviceCasesApi;
        this.amazonS3 = amazonS3;
    }

    /**
     * Gets a result.
     *
     * @return a result
     */
    @Override
    public List<ServiceCaseAttachment> get() {
        try {
            List<ServiceCaseAttachment> attachments = serviceCasesApi.getAttachments(id)
                    .stream()
                    .map(ServiceCaseAttachment::from)
                    .collect(Collectors.toList());
            attachments.forEach(this::initializeResourceSupplier);
            return attachments;
        } catch (ServiceApiException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    private void initializeResourceSupplier(ServiceCaseAttachment attachment) {
        try {
            attachment.setResourceSupplier(new AWSS3ResourceSupplier(this.amazonS3, new URI(attachment.getUrl())));
        } catch (URISyntaxException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}
