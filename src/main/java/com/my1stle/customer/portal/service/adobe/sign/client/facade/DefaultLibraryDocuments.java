package com.my1stle.customer.portal.service.adobe.sign.client.facade;

import com.adobe.sign.api.LibraryDocumentsApi;
import com.adobe.sign.model.libraryDocuments.Documents;
import com.adobe.sign.model.libraryDocuments.LibraryCreationInfo;
import com.adobe.sign.model.libraryDocuments.LibraryDocumentCreationResponse;
import com.adobe.sign.model.libraryDocuments.OriginalDocument;
import com.adobe.sign.utils.ApiException;
import com.my1stle.customer.portal.service.adobe.sign.api.LibraryDocuments;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;

import javax.ws.rs.core.MultivaluedMap;
import java.io.ByteArrayInputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

class DefaultLibraryDocuments implements LibraryDocuments {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultLibraryDocuments.class);

    private static final Integer MAX_RETRY_COUNT = 10;
    private static final Integer BACKOFF_TIME = 3000;
    static final String LIBRARY_DOCUMENT_NOT_YET_AVAILABLE = "The library document is not yet available or will have no pages to view";

    private static final Set<String> messagesToRetryOn = Collections.unmodifiableSet(
            new HashSet<>(Arrays.asList(LIBRARY_DOCUMENT_NOT_YET_AVAILABLE))
    );

    private final LibraryDocumentsApi libraryDocumentsApi;
    private final MultivaluedMap<String, String> headers;

    DefaultLibraryDocuments(LibraryDocumentsApi libraryDocumentsApi, MultivaluedMap<String, String> headers) {
        this.libraryDocumentsApi = libraryDocumentsApi;
        this.headers = headers;
    }

    @Override
    public LibraryDocumentCreationResponse create(LibraryCreationInfo libraryCreationInfo) {
        try {
            return this.libraryDocumentsApi.createLibraryDocument(this.headers, libraryCreationInfo);
        } catch (ApiException e) {
            LOGGER.error(e.getMessage(), e);
            throw new RuntimeException("Unable to create library document!", e);
        }
    }

    @Override
    public Resource getDocumentResource(String libraryDocumentId, String documentId) {

        byte[] blob = new byte[0];

        try {
            blob = this.libraryDocumentsApi.getLibraryDocument(this.headers, libraryDocumentId, documentId);
        } catch (ApiException e) {
            LOGGER.error(e.getMessage(), e);
            throw new RuntimeException("Unable to get document resource!", e);
        }

        if (null == blob) {
            return null;
        }

        return new InputStreamResource(new ByteArrayInputStream(blob));

    }

    @Override
    public List<OriginalDocument> getOriginalDocuments(String libraryDocumentId) {

        Documents documents = null;
        try {
            documents = this.libraryDocumentsApi.getDocuments(this.headers, libraryDocumentId);
        } catch (ApiException e) {

            // Attempt to retry
            if (messagesToRetryOn.contains(e.getMessage())) {
                LOGGER.warn("{}. Retrying...", e.getMessage());
                return retryGettingOriginalDocuments(libraryDocumentId, 1);
            }

            LOGGER.error(e.getMessage(), e);
            throw new RuntimeException("Unable to get signing url!", e);
        }

        if (null == documents) {
            return Collections.emptyList();
        }

        return documents.getDocuments();

    }

    /**
     * Retries to get OriginalDocuments
     *
     * @param libraryDocumentId
     * @param retryCount
     * @return
     */
    private List<OriginalDocument> retryGettingOriginalDocuments(String libraryDocumentId, Integer retryCount) {

        if (retryCount > MAX_RETRY_COUNT) {
            throw new RuntimeException("Maximum retry attempts exceeded! Unable to retrieve original documents");
        } else {
            try {
                Thread.sleep(BACKOFF_TIME); // TODO find a better to wait to retry
                LOGGER.info("Retry attempt {} out of {}", retryCount, MAX_RETRY_COUNT);
                return this.libraryDocumentsApi.getDocuments(this.headers, libraryDocumentId).getDocuments();
            } catch (ApiException | InterruptedException e) {
                if (messagesToRetryOn.contains(e.getMessage())) {
                    LOGGER.warn("{}. Retrying...", e.getMessage());
                    return retryGettingOriginalDocuments(libraryDocumentId, ++retryCount);
                }
                LOGGER.error(e.getMessage(), e);
                throw new RuntimeException("Unable to get signing url!", e);
            }
        }
    }
}