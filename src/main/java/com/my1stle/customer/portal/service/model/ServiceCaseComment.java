package com.my1stle.customer.portal.service.model;

import java.time.ZonedDateTime;
import java.util.Objects;

public class ServiceCaseComment {

    private String posterName;
    private String body;
    private ZonedDateTime createdDateTime;
    private ZonedDateTime lastModifiedDateTime;

    public ServiceCaseComment(String posterName, String body, ZonedDateTime createdDateTime, ZonedDateTime lastModifiedDateTime) {
        Objects.requireNonNull(posterName);
        Objects.requireNonNull(body);
        Objects.requireNonNull(createdDateTime);
        Objects.requireNonNull(lastModifiedDateTime);
        this.posterName = posterName;
        this.body = body;
        this.createdDateTime = createdDateTime;
        this.lastModifiedDateTime = lastModifiedDateTime;
    }

    public String getPosterName() {
        return posterName;
    }

    public String getBody() {
        return body;
    }

    public ZonedDateTime getCreatedDateTime() {
        return createdDateTime;
    }

    public ZonedDateTime getLastModifiedDateTime() {
        return lastModifiedDateTime;
    }


}
