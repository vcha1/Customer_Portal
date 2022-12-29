package com.my1stle.customer.portal.service.serviceapi;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.time.ZonedDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "id",
        "posterId",
        "body",
        "internal",
})
public class ExistingCommentDto extends CommentDto {

    @JsonProperty("id")
    private long id;

    @JsonProperty("createdDateTime")
    private ZonedDateTime createdDateTime;

    @JsonProperty("lastModifiedDateTime")
    private ZonedDateTime lastModifiedDateTime;

    private ExistingCommentDto() {

    }

    public long getId() {
        return id;
    }

    public ZonedDateTime getCreatedDateTime() {
        return createdDateTime;
    }

    public ZonedDateTime getLastModifiedDateTime() {
        return lastModifiedDateTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof ExistingCommentDto)) return false;

        ExistingCommentDto that = (ExistingCommentDto) o;

        return new EqualsBuilder()
                .appendSuper(super.equals(o))
                .append(id, that.id)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .appendSuper(super.hashCode())
                .append(id)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("posterId", posterId)
                .append("body", body)
                .append("internal", internal)
                .append("createdDateTime", createdDateTime)
                .append("lastModifiedDateTime", lastModifiedDateTime)
                .toString();
    }
}
