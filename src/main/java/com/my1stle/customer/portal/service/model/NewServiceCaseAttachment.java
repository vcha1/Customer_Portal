package com.my1stle.customer.portal.service.model;

import org.springframework.web.multipart.MultipartFile;

public class NewServiceCaseAttachment {

    private MultipartFile file;

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }
}
