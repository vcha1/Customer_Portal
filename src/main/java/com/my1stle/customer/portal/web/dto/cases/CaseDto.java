package com.my1stle.customer.portal.web.dto.cases;

import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

public class CaseDto {

    private String installationId;

    private String addressChoiceId;

    private Boolean isInstallOperational;

    @Size(max = 255, message = "Exceeded message limit size of 255 characters")
    private String preInstallDescription;

    @Size(max = 255, message = "Exceeded message limit size of 255 characters")
    private String description;

    private String category;

    private String preInstallIssue;

    private Boolean interiorDamage;

    private Boolean inverterDisplayScreen;

    private List<MultipartFile> attachments = new ArrayList<>();

    public CaseDto(){}


    public String getInstallationId() {
        return installationId;
    }

    public void setInstallationId(String installationId) {
        this.installationId = installationId;
    }

    public String getAddressChoiceId() {
        return addressChoiceId;
    }

    public void setAddressChoiceId(String addressChoiceId) {
        this.addressChoiceId = addressChoiceId;
    }

    public Boolean getIsInstallOperational() {
        return isInstallOperational;
    }

    public void setIsInstallOperational(Boolean isInstallOperational) {
        this.isInstallOperational = isInstallOperational;
    }

    public String getPreInstallDescription() {
        return preInstallDescription;
    }

    public void setPreInstallDescription(String preInstallDescription) {
        this.preInstallDescription = preInstallDescription;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return this.category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Boolean getInteriorDamage() {
        return interiorDamage;
    }

    public void setInteriorDamage(Boolean interiorDamage) {
        this.interiorDamage = interiorDamage;
    }

    public Boolean getInverterDisplayScreen() {
        return inverterDisplayScreen;
    }

    public void setInverterDisplayScreen(Boolean inverterDisplayScreen) {
        this.inverterDisplayScreen = inverterDisplayScreen;
    }

    public String getPreInstallIssue() {
        return preInstallIssue;
    }

    public void setPreInstallIssue(String preInstallIssue) {
        this.preInstallIssue = preInstallIssue;
    }

    public List<MultipartFile> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<MultipartFile> attachments) {
        this.attachments = attachments;
    }
}
