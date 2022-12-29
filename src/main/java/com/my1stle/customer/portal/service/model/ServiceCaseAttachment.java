package com.my1stle.customer.portal.service.model;

import com.my1stle.customer.portal.service.serviceapi.ExistingAttachmentDto;
import org.springframework.core.io.Resource;

import java.util.Objects;
import java.util.function.Supplier;

public class ServiceCaseAttachment {

    private String id;
    private String name;
    private String url;
    private Resource resource;


    // Lazy loaders
    private Supplier<Resource> resourceSupplier;

    private ServiceCaseAttachment(String id, String name, String url) {
        this.url = url;
        Objects.requireNonNull(id);
        Objects.requireNonNull(name);
        this.id = id;
        this.name = name;
    }

    public static ServiceCaseAttachment from(ExistingAttachmentDto attachmentDto) {
        return new ServiceCaseAttachment(String.valueOf(attachmentDto.getId()), attachmentDto.getName(), attachmentDto.getUrl());
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public Resource getResource() {
        if (null == resource && resourceSupplier != null) {
            resource = resourceSupplier.get();
        }
        return resource;
    }

    public void setResourceSupplier(Supplier<Resource> resourceSupplier) {
        this.resourceSupplier = resourceSupplier;
    }
}
