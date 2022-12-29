package com.my1stle.customer.portal.persistence.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "product_agreement_document")
public class ProductAgreementDocumentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @OneToOne
    @JoinColumn(name = "product_id", nullable = false)
    private ProductEntity product;

    @Column(name = "library_id", nullable = false)
    private String libraryId;

    @Column(name = "document_id", nullable = false)
    private String documentId;

    // Constructors
    protected ProductAgreementDocumentEntity() {

    }

    // Getters
    public Long getId() {
        return id;
    }

    public ProductEntity getProduct() {
        return product;
    }

    public String getLibraryId() {
        return libraryId;
    }

    public String getDocumentId() {
        return documentId;
    }

}
