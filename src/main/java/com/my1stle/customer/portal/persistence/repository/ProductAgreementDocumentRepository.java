package com.my1stle.customer.portal.persistence.repository;

import com.my1stle.customer.portal.persistence.model.ProductAgreementDocumentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductAgreementDocumentRepository extends JpaRepository<ProductAgreementDocumentEntity, Long> {

    Optional<ProductAgreementDocumentEntity> findByProductId(Long productId);

}