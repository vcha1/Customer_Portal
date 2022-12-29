package com.my1stle.customer.portal.service.adobe.sign.client.facade;

import com.adobe.sign.api.AgreementsApi;
import com.adobe.sign.model.agreements.AgreementCreationInfo;
import com.adobe.sign.model.agreements.AgreementCreationResponse;
import com.adobe.sign.model.agreements.AgreementInfo;
import com.adobe.sign.model.agreements.DocumentUrl;
import com.adobe.sign.model.agreements.SigningUrlResponse;
import com.adobe.sign.model.agreements.SigningUrlSetInfo;
import com.adobe.sign.utils.ApiException;
import com.my1stle.customer.portal.service.adobe.sign.api.Agreements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;

import javax.ws.rs.core.MultivaluedMap;
import java.io.ByteArrayInputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

class DefaultAgreements implements Agreements {

    private static Logger LOGGER = LoggerFactory.getLogger(DefaultAgreements.class);
    private static final Integer MAX_RETRY_COUNT = 10;
    private static final Integer BACKOFF_TIME = 3000;
    static final String DOCUMENT_NOT_EXPOSED_MESSAGE = "Document is not yet exposed to this user";
    static final String AGREEMENT_NOT_CURRENTLY_WAITING_TO_SIGN_MESSAGE = "The agreement is not currently waiting for anyone to sign it";

    private static final Set<String> messagesToRetryOn = Collections.unmodifiableSet(
            new HashSet<>(Arrays.asList(DOCUMENT_NOT_EXPOSED_MESSAGE, AGREEMENT_NOT_CURRENTLY_WAITING_TO_SIGN_MESSAGE))
    );

    private final AgreementsApi agreementsApi;
    private final MultivaluedMap<String, String> headers;

    DefaultAgreements(AgreementsApi agreementsApi, MultivaluedMap<String, String> headers) {
        this.agreementsApi = agreementsApi;
        this.headers = headers;
    }

    @Override
    public AgreementCreationResponse send(AgreementCreationInfo agreementCreationInfo) {

        try {
            return this.agreementsApi.createAgreement(this.headers, agreementCreationInfo);
        } catch (ApiException e) {
            LOGGER.error(e.getMessage(), e);
            throw new RuntimeException(e.getMessage(), e);
        }

    }

    @Override
    public Resource getAgreementResource(String agreementId) {

        byte[] blob = new byte[0];

        try {
            blob = this.agreementsApi.getCombinedDocument(this.headers, agreementId, null, null, true, false);
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
    public AgreementInfo getAgreementInfo(String agreementId) {

        try {
            return this.agreementsApi.getAgreementInfo(this.headers, agreementId);
        } catch (ApiException e) {
            LOGGER.error(e.getMessage(), e);
            throw new RuntimeException("Unable to to get Agreement Info!", e);
        }

    }

    @Override
    public URL getCombinedDocumentURL(String agreementId) {
        try {

            DocumentUrl url = agreementsApi.getCombinedDocumentUrl(
                    this.headers,
                    agreementId,
                    null,
                    null,
                    false,
                    false
            );

            return new URL(url.getUrl());
        } catch (ApiException | MalformedURLException e) {
            throw new RuntimeException("Failed to get URL for contract", e);
        }
    }

    /**
     * @param agreementId
     * @return
     * @implSpec will attempt to retry retrieving of signing url set info if the api exception
     * message is "Document is not yet exposed to this user". "Document is not yet exposed to this user" exception occurs
     * when trying to access esign url set info from a recently created agreement (speaking in terms of milliseconds)
     */
    @Override
    public List<SigningUrlSetInfo> getSigningUrlSetInfos(String agreementId) {

        try {
            SigningUrlResponse signingUrl = this.agreementsApi.getSigningUrl(this.headers, agreementId);
            return signingUrl.getSigningUrlSetInfos();
        } catch (ApiException e) {

            // Retry if "Document is not yet exposed to this user"
            // "Document is not yet exposed to this user" exception occurs
            // when trying to access esign url set info from a recently created agreement
            if (messagesToRetryOn.contains(e.getMessage())) {
                LOGGER.warn("{}. Retrying...", e.getMessage());
                return retryGettingSigningUrlInfo(agreementId, 1);
            }

            LOGGER.error(e.getMessage(), e);
            throw new RuntimeException("Unable to get signing url!", e);
        }

    }

    /**
     * Retries to get SigningUrlSetInfo
     *
     * @param agreementId
     * @param retryCount
     * @return
     */
    private List<SigningUrlSetInfo> retryGettingSigningUrlInfo(String agreementId, Integer retryCount) {

        if (retryCount > MAX_RETRY_COUNT) {

            throw new RuntimeException("Maximum retry attempts exceeded! Unable to retrieve Signing Url Info");

        } else {

            try {
                Thread.sleep(BACKOFF_TIME); // TODO find a better to wait to retry
                LOGGER.info("Retry attempt {} out of {}", retryCount, MAX_RETRY_COUNT);
                SigningUrlResponse signingUrl = this.agreementsApi.getSigningUrl(this.headers, agreementId);
                return signingUrl.getSigningUrlSetInfos();
            } catch (ApiException | InterruptedException e) {

                if (messagesToRetryOn.contains(e.getMessage())) {
                    LOGGER.warn("{}. Retrying...", e.getMessage());
                    return retryGettingSigningUrlInfo(agreementId, ++retryCount);
                }

                LOGGER.error(e.getMessage(), e);
                throw new RuntimeException("Unable to get signing url!", e);

            }

        }

    }

}