package com.my1stle.customer.portal.service.adobe.sign.info;

import com.adobe.sign.model.libraryDocuments.FileInfo;
import com.adobe.sign.model.libraryDocuments.InteractiveOptions;
import com.adobe.sign.model.libraryDocuments.LibraryCreationInfo;
import com.adobe.sign.model.libraryDocuments.LibraryDocumentCreationInfo;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Objects;

/**
 * @see <a href="https://www.adobe.com/devnet-docs/adobesign/reference/sdk/classcom_1_1adobe_1_1sign_1_1model_1_1library_documents_1_1_library_creation_info.html">LibraryCreationInfo Class Reference</a>
 */
public final class LibraryCreationInfoBuilder {

    // Required Fields
    private List<FileInfo> fileInfos;
    private String name;
    private LibraryDocumentCreationInfo.LibrarySharingModeEnum sharingMode;
    private List<LibraryDocumentCreationInfo.LibraryTemplateTypesEnum> libraryTemplateTypes;

    // Optional Fields
    private Boolean isAuthoringRequest;
    private Boolean autoLoginUser;
    private Boolean noChrome;

    public LibraryCreationInfoBuilder(
            List<FileInfo> fileInfos,
            String name,
            LibraryDocumentCreationInfo.LibrarySharingModeEnum sharingMode,
            List<LibraryDocumentCreationInfo.LibraryTemplateTypesEnum> libraryTemplateTypes) {

        Objects.requireNonNull(fileInfos);
        Objects.requireNonNull(name);
        Objects.requireNonNull(sharingMode);
        Objects.requireNonNull(libraryTemplateTypes);

        if (StringUtils.isBlank(name)) {
            throw new IllegalArgumentException("Name is required!");
        }

        if (libraryTemplateTypes.isEmpty()) {
            throw new IllegalArgumentException("Non empty list of library template types is required!");
        }

        this.fileInfos = fileInfos;
        this.name = name;
        this.sharingMode = sharingMode;
        this.libraryTemplateTypes = libraryTemplateTypes;
    }

    public LibraryCreationInfoBuilder isAuthoringRequest(Boolean isAuthoringRequest) {
        this.isAuthoringRequest = isAuthoringRequest;
        return this;
    }

    public LibraryCreationInfoBuilder autoLoginUser(Boolean autoLoginUser) {
        this.autoLoginUser = autoLoginUser;
        return this;
    }

    public LibraryCreationInfoBuilder noChrome(Boolean noChrome) {
        this.noChrome = noChrome;
        return this;
    }

    public LibraryCreationInfo build() {

        LibraryCreationInfo info = new LibraryCreationInfo();

        // Setting Required Fields
        LibraryDocumentCreationInfo creationInfo = new LibraryDocumentCreationInfo();
        creationInfo.setFileInfos(this.fileInfos);
        creationInfo.setName(this.name);
        creationInfo.setLibrarySharingMode(this.sharingMode);
        creationInfo.setLibraryTemplateTypes(this.libraryTemplateTypes);

        // Setting Optional Fields
        InteractiveOptions options = new InteractiveOptions();
        options.setAuthoringRequested(this.isAuthoringRequest == null ? false : this.isAuthoringRequest);
        options.setAutoLoginUser(this.autoLoginUser == null ? false : this.autoLoginUser);
        options.setNoChrome(this.noChrome == null ? false : this.noChrome);

        // Finalize
        info.setLibraryDocumentCreationInfo(creationInfo);
        info.setOptions(options);

        return info;

    }

}
