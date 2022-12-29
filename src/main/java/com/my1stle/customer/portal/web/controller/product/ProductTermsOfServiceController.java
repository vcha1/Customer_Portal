package com.my1stle.customer.portal.web.controller.product;

import com.my1stle.customer.portal.service.model.ProductAgreementDocument;
import com.my1stle.customer.portal.service.product.ProductAgreementDocumentService;
import com.my1stle.customer.portal.service.util.MediaTypeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.Optional;

@Controller
public class ProductTermsOfServiceController {

    private ProductAgreementDocumentService productAgreementDocumentService;

    @Autowired
    public ProductTermsOfServiceController(
            ProductAgreementDocumentService productAgreementDocumentService) {
        this.productAgreementDocumentService = productAgreementDocumentService;
    }

    @GetMapping(value = "/product/{product_id}/terms_of_service")
    public ResponseEntity<InputStreamResource> viewProductTermsOfService(@PathVariable("product_id") Long productId) throws IOException {

        Optional<ProductAgreementDocument> agreementDocument = this.productAgreementDocumentService.getByProductId(productId);

        if (!agreementDocument.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        ProductAgreementDocument productAgreementDocument = agreementDocument.get();
        Resource resource = productAgreementDocument.getAgreementDocumentResource();
        InputStream is = resource.getInputStream();
        MediaType mediaType = MediaTypeUtil.getMediaType(is);
        String extension = MediaTypeUtil.getExtension(mediaType);
        String productName = productAgreementDocument.getProduct().getName();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, String.format("inline;filename=%s", URLEncoder.encode(String.format("Terms of Service for %s", productName), "UTF-8")))
                .contentType(mediaType)
                .body(new InputStreamResource(is));


    }

}
