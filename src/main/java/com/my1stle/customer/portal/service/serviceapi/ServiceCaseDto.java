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
        "externalId",
        "assigneeId",
        "reporterId",
        "issueType",
        "subIssueType",
        "summary",
        "description",
        "status",
        "odooId"
})
public class ServiceCaseDto {

    @JsonProperty("assigneeId")
    Long assigneeId;

    @JsonProperty("reporterId")
    Long reporterId;

    @JsonProperty("issueType")
    Long issueType;

    @JsonProperty("subIssueType")
    Long subIssueType;

    @JsonProperty("summary")
    String summary;

    @JsonProperty("description")
    String description;

    @JsonProperty("status")
    ServiceCaseStatus status;

    @JsonProperty("externalId")
    String externalId;

    @JsonProperty("groupId")
    Long groupId;

    @JsonProperty("odooId")
    String odooId;

    ServiceCaseDto() {

    }

    private ServiceCaseDto(Builder builder) {
        this.assigneeId = builder.assigneeId;
        this.subIssueType = builder.subIssueType;
        this.description = builder.description;
        this.status = builder.status == null ? ServiceCaseStatus.NEW : builder.status;
        this.reporterId = builder.reporterId;
        this.summary = builder.summary;
        this.issueType = builder.issueType;
        this.externalId = builder.externalId;
        this.groupId = builder.groupId;
        this.odooId = builder.odooId;
    }

    public Long getAssigneeId() {
        return assigneeId;
    }

    public Long getReporterId() {
        return reporterId;
    }

    public Long getIssueType() {
        return issueType;
    }

    public Long getSubIssueType() {
        return subIssueType;
    }

    public String getSummary() {
        return summary;
    }

    public String getDescription() {
        return description;
    }

    public ServiceCaseStatus getStatus() {
        return status;
    }

    public String getExternalId() {
        return externalId;
    }

    public Long getGroupId() {
        return groupId;
    }

    public String getOdooId() {
        return odooId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof ServiceCaseDto)) return false;

        ServiceCaseDto caseDto = (ServiceCaseDto) o;

        return new EqualsBuilder()
                .append(assigneeId, caseDto.assigneeId)
                .append(reporterId, caseDto.reporterId)
                .append(issueType, caseDto.issueType)
                .append(subIssueType, caseDto.subIssueType)
                .append(summary, caseDto.summary)
                .append(description, caseDto.description)
                .append(status, caseDto.status)
                .append(odooId, caseDto.odooId)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(assigneeId)
                .append(reporterId)
                .append(issueType)
                .append(subIssueType)
                .append(summary)
                .append(description)
                .append(status)
                .append(odooId)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.JSON_STYLE)
                .append("assigneeId", assigneeId)
                .append("reporterId", reporterId)
                .append("issueType", issueType)
                .append("subIssueType", subIssueType)
                .append("summary", summary)
                .append("description", description)
                .append("status", status)
                .append("odooId", odooId)
                .toString();
    }

    public static final class Builder {


        private Long assigneeId;
        private long reporterId;
        private long issueType;
        private Long subIssueType;
        private String summary;
        private String description;
        private ServiceCaseStatus status;
        private String externalId;
        private Long groupId;
        private String odooId;

        public Builder(long reporterId, long issueType, String summary, String description, ServiceCaseStatus status) {
            this.reporterId = reporterId;
            this.issueType = issueType;
            this.summary = summary;
            this.description = description;
            this.status = status;
        }

        public Builder assigneeId(Long assigneeId) {
            this.assigneeId = assigneeId;
            return this;
        }

        public Builder subIssueType(Long subIssueType) {
            this.subIssueType = subIssueType;
            return this;
        }

        public Builder odooId(String odooId) {
            this.odooId = odooId;
            return this;
        }

        public Builder externalId(String externalId) {
            this.externalId = externalId;
            return this;
        }

        public Builder groupId(Long groupId) {
            this.groupId = groupId;
            return this;
        }

        public ServiceCaseDto build() {
            return new ServiceCaseDto(this);
        }
    }
}
