package com.my1stle.customer.portal.service.adobe.sign.api;

import com.adobe.sign.model.libraryDocuments.LibraryCreationInfo;
import com.adobe.sign.model.libraryDocuments.LibraryDocumentCreationResponse;
import com.adobe.sign.model.libraryDocuments.OriginalDocument;
import com.adobe.sign.utils.ApiException;
import org.springframework.core.io.Resource;

import java.util.List;

public interface LibraryDocuments {

    /**
     * Creates a template that is placed in the library of the user for reuse.
     *
     * @param libraryCreationInfo
     * @return
     * @throws ApiException
     */
    LibraryDocumentCreationResponse create(LibraryCreationInfo libraryCreationInfo);

    /**
     * Returns the library document as a resource
     *
     * @param libraryDocumentId
     * @return resource representing the library document
     * @throws ApiException
     */
    Resource getDocumentResource(String libraryDocumentId, String documentId);

    /**
     * Returns the Original Documents associated with the library id
     *
     * @param libraryDocumentId
     * @return
     * @throws ApiException
     */
    List<OriginalDocument> getOriginalDocuments(String libraryDocumentId);

}