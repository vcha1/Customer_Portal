package com.my1stle.customer.portal.service.adobe.sign.info;

import com.adobe.sign.model.agreements.AgreementCreationInfo;
import com.adobe.sign.model.agreements.DocumentCreationInfo;
import com.adobe.sign.model.agreements.ExternalId;
import com.adobe.sign.model.agreements.FileInfo;
import com.adobe.sign.model.agreements.InteractiveOptions;
import com.adobe.sign.model.agreements.MergefieldInfo;
import com.adobe.sign.model.agreements.PostSignOptions;
import com.adobe.sign.model.agreements.RecipientSetInfo;
import com.adobe.sign.model.agreements.RequestFormField;
import com.adobe.sign.model.agreements.SecurityOption;
import com.adobe.sign.model.agreements.SendThroughWebOptions;
import com.adobe.sign.model.agreements.VaultingInfo;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Objects;

/**
 * @see <a href="https://www.adobe.com/devnet-docs/adobesign/reference/sdk/classcom_1_1adobe_1_1sign_1_1model_1_1agreements_1_1_agreement_creation_info.html">AgreementCreationInfo Class Reference</a>
 */
public final class AgreementCreationInfoBuilder {

    // Required Fields
    private List<FileInfo> fileInfos;
    private String name;
    private List<RecipientSetInfo> recipientSetInfos;
    private DocumentCreationInfo.SignatureTypeEnum signatureType;

    // Optional Fields
    private String callback;
    private List<String> ccs;
    private Integer daysUntilSigningDeadline;
    private ExternalId externalId;
    private List<FileInfo> formFieldLayerTemplates;
    private List<RequestFormField> requestFormFields;
    private List<MergefieldInfo> mergefieldInfos;
    private String message;
    private PostSignOptions postSignOptions;
    private DocumentCreationInfo.ReminderFrequencyEnum reminderFrequencyEnum;
    private SecurityOption securityOption;
    private DocumentCreationInfo.SignatureFlowEnum signatureFlow;
    private VaultingInfo vaultingInfo;
    private Boolean isAuthoringRequested;
    private Boolean autoLoginUser;
    private String locale;
    private Boolean noChrome;
    private Boolean sendThroughWeb;
    private SendThroughWebOptions sendThroughWebOptions;

    public AgreementCreationInfoBuilder(
            List<FileInfo> fileInfos,
            String name,
            List<RecipientSetInfo> recipientSetInfos,
            DocumentCreationInfo.SignatureTypeEnum signatureType) {

        Objects.requireNonNull(fileInfos);
        Objects.requireNonNull(name);
        Objects.requireNonNull(recipientSetInfos);
        Objects.requireNonNull(signatureType);

        if (fileInfos.isEmpty()) {
            throw new IllegalArgumentException("List of file infos is required!");
        }

        if (StringUtils.isBlank(name)) {
            throw new IllegalArgumentException("Name is required!");
        }

        if (recipientSetInfos.isEmpty()) {
            throw new IllegalArgumentException(("List of recipient set infos is required!"));
        }

        this.fileInfos = fileInfos;
        this.name = name;
        this.recipientSetInfos = recipientSetInfos;
        this.signatureType = signatureType;
    }

    public AgreementCreationInfo build() {

        AgreementCreationInfo info = new AgreementCreationInfo();

        // Setting Required Fields
        DocumentCreationInfo documentCreationInfo = new DocumentCreationInfo();
        documentCreationInfo.setFileInfos(this.fileInfos);
        documentCreationInfo.setName(this.name);
        documentCreationInfo.setRecipientSetInfos(this.recipientSetInfos);
        documentCreationInfo.setSignatureType(this.signatureType);

        // Setting Optional Fields
        documentCreationInfo.setCallbackInfo(this.callback);
        documentCreationInfo.setCcs(this.ccs);
        documentCreationInfo.setDaysUntilSigningDeadline(this.daysUntilSigningDeadline);
        documentCreationInfo.setExternalId(this.externalId);
        documentCreationInfo.setFormFieldLayerTemplates(this.formFieldLayerTemplates);
        documentCreationInfo.setFormFields(this.requestFormFields);
        documentCreationInfo.setMergeFieldInfo(this.mergefieldInfos);
        documentCreationInfo.setMessage(this.message);
        documentCreationInfo.setPostSignOptions(this.postSignOptions);
        documentCreationInfo.setReminderFrequency(this.reminderFrequencyEnum);
        documentCreationInfo.setSecurityOptions(this.securityOption);
        documentCreationInfo.setSignatureFlow(this.signatureFlow);
        documentCreationInfo.setVaultingInfo(this.vaultingInfo);

        InteractiveOptions interactiveOptions = new InteractiveOptions();
        interactiveOptions.setAuthoringRequested(this.isAuthoringRequested == null ? false : this.isAuthoringRequested);
        interactiveOptions.setAutoLoginUser(this.autoLoginUser == null ? false : this.autoLoginUser);
        interactiveOptions.setLocale(this.locale);
        interactiveOptions.setNoChrome(this.noChrome == null ? false : this.noChrome);
        interactiveOptions.setSendThroughWeb(this.sendThroughWeb == null ? false : this.sendThroughWeb);
        interactiveOptions.setSendThroughWebOptions(this.sendThroughWebOptions);

        info.setDocumentCreationInfo(documentCreationInfo);
        info.setOptions(interactiveOptions);

        return info;

    }

    public AgreementCreationInfoBuilder callback(String callback) {
        this.callback = callback;
        return this;
    }

    public AgreementCreationInfoBuilder ccs(List<String> ccs) {
        this.ccs = ccs;
        return this;
    }

    public AgreementCreationInfoBuilder daysUntilSigningDeadline(Integer daysUntilSigningDeadline) {
        this.daysUntilSigningDeadline = daysUntilSigningDeadline;
        return this;
    }

    public AgreementCreationInfoBuilder externalId(ExternalId externalId) {
        this.externalId = externalId;
        return this;
    }

    public AgreementCreationInfoBuilder formFieldLayerTemplates(List<FileInfo> formFieldLayerTemplates) {
        this.formFieldLayerTemplates = formFieldLayerTemplates;
        return this;
    }

    public AgreementCreationInfoBuilder requestFormFields(List<RequestFormField> requestFormFields) {
        this.requestFormFields = requestFormFields;
        return this;
    }

    public AgreementCreationInfoBuilder mergefieldInfos(List<MergefieldInfo> mergefieldInfos) {
        this.mergefieldInfos = mergefieldInfos;
        return this;
    }

    public AgreementCreationInfoBuilder message(String message) {
        this.message = message;
        return this;
    }

    public AgreementCreationInfoBuilder postSignOptions(PostSignOptions postSignOptions) {
        this.postSignOptions = postSignOptions;
        return this;
    }

    public AgreementCreationInfoBuilder reminderFrequencyEnum(DocumentCreationInfo.ReminderFrequencyEnum reminderFrequencyEnum) {
        this.reminderFrequencyEnum = reminderFrequencyEnum;
        return this;
    }

    public AgreementCreationInfoBuilder securityOption(SecurityOption securityOption) {
        this.securityOption = securityOption;
        return this;
    }

    public AgreementCreationInfoBuilder signatureFlow(DocumentCreationInfo.SignatureFlowEnum signatureFlow) {
        this.signatureFlow = signatureFlow;
        return this;
    }

    public AgreementCreationInfoBuilder vaultingInfo(VaultingInfo vaultingInfo) {
        this.vaultingInfo = vaultingInfo;
        return this;
    }

    public AgreementCreationInfoBuilder authoringRequested(Boolean authoringRequested) {
        this.isAuthoringRequested = authoringRequested;
        return this;
    }

    public AgreementCreationInfoBuilder autoLoginUser(Boolean autoLoginUser) {
        this.autoLoginUser = autoLoginUser;
        return this;
    }

    public AgreementCreationInfoBuilder locale(String locale) {
        this.locale = locale;
        return this;
    }

    public AgreementCreationInfoBuilder noChrome(Boolean noChrome) {
        this.noChrome = noChrome;
        return this;
    }

    public AgreementCreationInfoBuilder sendThroughWeb(Boolean sendThroughWeb) {
        this.sendThroughWeb = sendThroughWeb;
        return this;
    }

    public AgreementCreationInfoBuilder sendThroughWebOptions(SendThroughWebOptions sendThroughWebOptions) {
        this.sendThroughWebOptions = sendThroughWebOptions;
        return this;
    }

}
