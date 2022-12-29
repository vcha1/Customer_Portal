package com.my1stle.customer.portal.service.adobe.sign.api;

import com.adobe.sign.model.agreements.AgreementCreationInfo;
import com.adobe.sign.model.agreements.AgreementCreationResponse;
import com.adobe.sign.model.agreements.AgreementInfo;
import com.adobe.sign.model.agreements.SigningUrlSetInfo;
import org.springframework.core.io.Resource;

import java.net.URL;
import java.util.List;

public interface Agreements {

    AgreementCreationResponse send(AgreementCreationInfo agreementCreationInfo);

    Resource getAgreementResource(String agreementId);

    AgreementInfo getAgreementInfo(String agreementId);

    URL getCombinedDocumentURL(String agreementId);

    List<SigningUrlSetInfo> getSigningUrlSetInfos(String agreementId);

}