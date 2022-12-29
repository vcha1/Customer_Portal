package com.my1stle.customer.portal.web.dto.scheduling;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.ZonedDateTime;
import java.util.Set;

public class PublicDateTimeSelectionDto {

    @NotNull
    private String name;

    private String installationId;

    private String opportunityId;

    @NotNull
    private Long calendarId;

    @NotNull
    @DateTimeFormat(pattern = "MM/dd/yyyy h:mm a ZZZ")
    private ZonedDateTime startDateTime;

    @NotNull
    private Integer duration;

    @NotNull
    private Set<Long> skillIds;

    @Size(max = 255, message = "maximum character limited reached")
    private String notes;

    @NotNull
    private String token;

    // Constructors
    public PublicDateTimeSelectionDto() {

    }

    // Getters
    public String getName() {
        return name;
    }

    public String getInstallationId() {
        return installationId;
    }

    public String getOpportunityId() {
        return opportunityId;
    }

    public Long getCalendarId() {
        return calendarId;
    }

    public ZonedDateTime getStartDateTime() {
        return startDateTime;
    }

    public Integer getDuration() {
        return duration;
    }

    public Set<Long> getSkillIds() {
        return skillIds;
    }

    public String getNotes() {
        return notes;
    }

    public String getToken() {
        return token;
    }

    // Setters
    public void setName(String name) {
        this.name = name;
    }

    public void setInstallationId(String installationId) {
        this.installationId = installationId;
    }

    public void setOpportunityId(String opportunityId) {
        this.opportunityId = opportunityId;
    }

    public void setCalendarId(Long calendarId) {
        this.calendarId = calendarId;
    }

    public void setStartDateTime(ZonedDateTime startDateTime) {
        this.startDateTime = startDateTime;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public void setSkillIds(Set<Long> skillIds) {
        this.skillIds = skillIds;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.JSON_STYLE)
                .append("name", name)
                .append("installationId", installationId)
                .append("calendarId", calendarId)
                .append("startDateTime", startDateTime)
                .append("duration", duration)
                .append("skillIds", skillIds)
                .append("notes", notes)
                .append("token", token)
                .toString();
    }
}
