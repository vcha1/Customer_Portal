package com.my1stle.customer.portal.service.model;

import org.springframework.core.io.Resource;

public interface ProductAgreementDocument {

    Long getId();

    Product getProduct();

    String getExternalLibraryId();

    String getExternalDocumentId();

    Resource getAgreementDocumentResource();

}