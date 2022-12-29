package com.my1stle.customer.portal.service.product;

import com.my1stle.customer.portal.service.model.ProductAgreementDocument;

import java.util.Optional;

public interface ProductAgreementDocumentService {

    Optional<ProductAgreementDocument> getByProductId(Long productId);

}
