package com.my1stle.customer.portal.service.serviceapi;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "id",
        "externalId",
        "assigneeId",
        "reporterId",
        "issueType",
        "subIssueType",
        "summary",
        "description",
        "status",
        "odooId",
        "additionalDetail"
})
public class ExistingServiceCaseDto extends ServiceCaseDto {

    @JsonProperty("id")
    private long id;

    @JsonProperty("additionalDetail")
    private ExistingServiceCaseDtoAdditionalDetail additionalDetail;

    //This is the case ID that is returned
    public long getId() {
        return id;
    }

    public ExistingServiceCaseDtoAdditionalDetail getAdditionalDetail() {
        return additionalDetail;
    }
    private ExistingServiceCaseDto() {

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof ExistingServiceCaseDto)) return false;

        ExistingServiceCaseDto that = (ExistingServiceCaseDto) o;

        return new EqualsBuilder()
                .appendSuper(super.equals(o))
                .append(id, that.id)
                .append(additionalDetail, that.additionalDetail)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .appendSuper(super.hashCode())
                .append(id)
                .append(additionalDetail)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.JSON_STYLE)
                .append("id", id)
                .append("assigneeId", assigneeId)
                .append("reporterId", reporterId)
                .append("issueType", issueType)
                .append("subIssueType", subIssueType)
                .append("summary", summary)
                .append("description", description)
                .append("status", status)
                .append("odooId", odooId)
                .append("additionalDetail", additionalDetail)
                .toString();
    }
}
