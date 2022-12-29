package com.my1stle.customer.portal.service.serviceapi;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "posterId",
        "body",
        "internal"
})
public class CommentDto {

    @JsonProperty("posterId")
    Long posterId;

    @JsonProperty("body")
    String body;

    @JsonProperty("internal")
    boolean internal;

    CommentDto() {

    }

    public CommentDto(long posterId, String body, boolean internal) {
        this.posterId = posterId;
        this.body = body;
        this.internal = internal;
    }

    public CommentDto(String body, boolean internal) {
        this.body = body;
        this.internal = internal;
    }

    public Long getPosterId() {
        return posterId;
    }

    public String getBody() {
        return body;
    }

    public boolean isInternal() {
        return internal;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof CommentDto)) return false;

        CommentDto that = (CommentDto) o;

        return new EqualsBuilder()
                .append(posterId, that.posterId)
                .append(internal, that.internal)
                .append(body, that.body)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(posterId)
                .append(body)
                .append(internal)
                .toHashCode();
    }
}
