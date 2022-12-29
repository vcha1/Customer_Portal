package com.my1stle.customer.portal.serviceImpl.subscription;

import com.adobe.sign.model.agreements.AgreementCreationInfo;
import com.adobe.sign.model.agreements.DocumentCreationInfo;
import com.adobe.sign.model.agreements.FileInfo;
import com.adobe.sign.model.agreements.MergefieldInfo;
import com.adobe.sign.model.agreements.PostSignOptions;
import com.adobe.sign.model.agreements.RecipientInfo;
import com.adobe.sign.model.agreements.RecipientSetInfo;
import com.my1stle.customer.portal.persistence.model.ProductEntity;
import com.my1stle.customer.portal.persistence.model.SubscriptionEntity;
import com.my1stle.customer.portal.service.InstallationService;
import com.my1stle.customer.portal.service.adobe.sign.info.AgreementCreationInfoBuilder;
import com.my1stle.customer.portal.service.adobe.sign.info.MergeFieldInfoBuilder;
import com.my1stle.customer.portal.service.model.Installation;
import com.my1stle.customer.portal.service.model.ProductAgreementDocument;
import com.my1stle.customer.portal.service.product.ProductAgreementDocumentService;
import com.my1stle.customer.portal.web.exception.ResourceNotFoundException;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.utils.URIBuilder;
import org.baeldung.persistence.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.InetAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
class SubscriptionAgreementCreationInfoFactory {

    private ProductAgreementDocumentService productAgreementDocumentService;
    private InstallationService installationService;

    private static final String LOCAL_CANONCIAL_HOST_NAME = "1stlightenergy.local"; // the name when application context is brought up
    private static final String REDIRECT_LOCAL_DEVELOPMENT_HOST = "mshome.net";

    @Value("${server.port}")
    private Integer serverPort;

    @Autowired
    public SubscriptionAgreementCreationInfoFactory(ProductAgreementDocumentService productAgreementDocumentService, InstallationService installationService) {
        this.productAgreementDocumentService = productAgreementDocumentService;
        this.installationService = installationService;
    }

    AgreementCreationInfo create(SubscriptionEntity subscriptionEntity) {

        User owner = subscriptionEntity.getOwner();
        ProductEntity product = subscriptionEntity.getProduct();
        Installation installation = this.installationService.getInstallationById(subscriptionEntity.getInstallationId());

        if (null == installation) {
            throw new ResourceNotFoundException("Installation Not Found!");
        }

        FileInfo fileInfo = new FileInfo();

        Optional<ProductAgreementDocument> agreementDocument = productAgreementDocumentService.getByProductId(product.getId());

        if (!agreementDocument.isPresent()) {
            throw new ResourceNotFoundException(String.format("Product %s does not have an agreement document!", product.getName()));
        }

        fileInfo.setLibraryDocumentId(agreementDocument.get().getExternalLibraryId());

        RecipientSetInfo recipientSetInfo = new RecipientSetInfo();
        recipientSetInfo.setRecipientSetRole(RecipientSetInfo.RecipientSetRoleEnum.SIGNER);

        RecipientInfo recipient = new RecipientInfo();
        recipient.setEmail(owner.getEmail());

        recipientSetInfo.setRecipientSetMemberInfos(Collections.singletonList(recipient));

        List<FileInfo> fileInfos = Collections.singletonList(fileInfo);
        String name = String.format("%s for %s", product.getName(), installation.getName());

        List<RecipientSetInfo> recipientSetInfos = Collections.singletonList(recipientSetInfo);
        DocumentCreationInfo.SignatureTypeEnum signatureType = DocumentCreationInfo.SignatureTypeEnum.ESIGN;

        AgreementCreationInfo agreementCreationInfo = new AgreementCreationInfoBuilder(fileInfos, name, recipientSetInfos, signatureType)
                .signatureFlow(DocumentCreationInfo.SignatureFlowEnum.SENDER_SIGNATURE_NOT_REQUIRED)
                .build();

        URIBuilder builder = new URIBuilder();

        try {
            builder.setScheme("https")
                    .setHost(InetAddress.getLocalHost().getCanonicalHostName())
                    .setPath(String.format("/subscription/%s/agreement_complete", subscriptionEntity.getId()));

        } catch (UnknownHostException e) {
            throw new RuntimeException(e.getMessage(), e);
        }

        try {
            URI uri = builder.build();

            // for locally testing
            // Adobe Sign will throw an API exception if the redirect URL ends with .local
            if (StringUtils.endsWithIgnoreCase(uri.getHost(), LOCAL_CANONCIAL_HOST_NAME)) {
                builder.setPort(serverPort);
                builder.setScheme("http");
                builder.setHost(StringUtils.replace(builder.getHost(), LOCAL_CANONCIAL_HOST_NAME, REDIRECT_LOCAL_DEVELOPMENT_HOST));
            }

            PostSignOptions postSignOptions = new PostSignOptions();
            postSignOptions.setRedirectUrl(builder.build().toString());
            agreementCreationInfo.getDocumentCreationInfo().setPostSignOptions(postSignOptions);

        } catch (URISyntaxException e) {
            throw new RuntimeException(e.getMessage(), e);
        }

        agreementCreationInfo.getDocumentCreationInfo().setMergeFieldInfo(generateMergeFieldsInfo(installation));

        return agreementCreationInfo;

    }

    void setServerPort(Integer serverPort) {
        this.serverPort = serverPort;
    }

    /**
     * @param installation
     * @return a list of {@link MergefieldInfo}
     * @implSpec The following are merge fields info that are generated  :
     * <p>
     * customer_name
     * customer_address
     * customer_reported_system_size
     * <p>
     * primary_contact_name
     * primary_contact_information
     * secondary_contact_name
     * secondary_contact_information
     * <p>
     * misc_provision_customer_name
     * misc_provision_customer_street_address
     * misc_provision_customer_state_and_zip_code
     * misc_provision_customer_email
     */
    private List<MergefieldInfo> generateMergeFieldsInfo(Installation installation) {

        Map<String, String> fieldNameValues = new HashMap<>();

        fieldNameValues.put("customer_name", installation.getCustomerName());
        fieldNameValues.put("customer_address", String.format("%s\n%s, %s %s", installation.getAddress(), installation.getCity(), installation.getState(), installation.getZipCode()));
        fieldNameValues.put("customer_reported_system_size", String.format("%.2f", installation.getSystemSize()));

        fieldNameValues.put("primary_contact_name", installation.getCustomerName());
        fieldNameValues.put("primary_contact_information", String.format("Phone : %s\n Email : %s\n", installation.getCustomerPhoneNumber(), installation.getCustomerEmail()));
        fieldNameValues.put("secondary_contact_name", "");
        fieldNameValues.put("secondary_contact_information", "");

        fieldNameValues.put("misc_provision_customer_name", installation.getCustomerName());
        fieldNameValues.put("misc_provision_customer_street_address", installation.getAddress());
        fieldNameValues.put("misc_provision_customer_state_and_zip_code", String.format("%s, %s", installation.getState(), installation.getZipCode()));
        fieldNameValues.put("misc_provision_customer_email", installation.getCustomerEmail());

        return new MergeFieldInfoBuilder(fieldNameValues).build();

    }

}