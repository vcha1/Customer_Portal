package com.my1stle.customer.portal.serviceImpl.product;

import com.my1stle.customer.portal.persistence.model.ProductAgreementDocumentEntity;
import com.my1stle.customer.portal.persistence.repository.ProductAgreementDocumentRepository;
import com.my1stle.customer.portal.service.adobe.sign.client.facade.AdobeSignFacadeClient;
import com.my1stle.customer.portal.service.model.Product;
import com.my1stle.customer.portal.service.model.ProductAgreementDocument;
import com.my1stle.customer.portal.service.product.ProductAgreementDocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Optional;
import java.util.function.Supplier;

@Service
public class DefaultAgreementDocumentService implements ProductAgreementDocumentService {

    private ProductAgreementDocumentRepository productAgreementDocumentRepository;
    private AdobeSignFacadeClient adobeSignFacadeClient;

    private static ProductAgreementDocumentService instance;


    @Autowired
    private DefaultAgreementDocumentService(
            ProductAgreementDocumentRepository productAgreementDocumentRepository,
            AdobeSignFacadeClient adobeSignFacadeClient) {
        this.productAgreementDocumentRepository = productAgreementDocumentRepository;
        this.adobeSignFacadeClient = adobeSignFacadeClient;

        instance = this;
    }

    @Override
    public Optional<ProductAgreementDocument> getByProductId(Long productId) {
        return this.productAgreementDocumentRepository
                .findByProductId(productId)
                .map(ProductAgreementDocumentProxy::new);
    }

    public static ProductAgreementDocumentService getInstance() {
       return instance;
    }

    private class ProductAgreementDocumentProxy implements ProductAgreementDocument {

        private Long id;
        private Product product;
        private String externalLibraryId;
        private String externalDocumentId;

        // Cache
        private Resource agreementDocumentResource;

        // Lazy loaders
        private Supplier<Resource> agreementDocumentResourceSupplier;

        ProductAgreementDocumentProxy(ProductAgreementDocumentEntity entity) {

            this.id = entity.getId();
            this.product = entity.getProduct();
            this.externalLibraryId = entity.getLibraryId();
            this.externalDocumentId = entity.getDocumentId();

            this.agreementDocumentResourceSupplier = () -> {
                return adobeSignFacadeClient.libraryDocuments().getDocumentResource(this.externalLibraryId, this.externalDocumentId);
            };

        }

        @Override
        public Long getId() {
            return this.id;
        }

        @Override
        public Product getProduct() {
            return this.product;
        }

        @Override
        public String getExternalLibraryId() {
            return this.externalLibraryId;
        }

        @Override
        public String getExternalDocumentId() {
            return this.externalDocumentId;
        }

        @Override
        public Resource getAgreementDocumentResource() {

            if (null == this.agreementDocumentResource) {
                agreementDocumentResource = agreementDocumentResourceSupplier.get();
            }

            return this.agreementDocumentResource;
        }

    }

}
