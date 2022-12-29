package com.my1stle.customer.portal.web.dto.scheduling;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.ZonedDateTime;

public class DateTimeSelectionDto {

    @NotNull
    private String installationId;

    @NotNull
    private Long productId;

    @NotNull
    private Long calendarId;

    @NotNull
    @DateTimeFormat(pattern = "MM/dd/yyyy h:mm a ZZZ")
    private ZonedDateTime startDateTime;

    @Size(max = 255, message = "maximum character limited reached")
    private String notes;

    // Constructors

    public DateTimeSelectionDto() {

    }

    // Getters

    public String getInstallationId() {
        return installationId;
    }

    public Long getProductId() {
        return productId;
    }

    public Long getCalendarId() {
        return calendarId;
    }

    public ZonedDateTime getStartDateTime() {
        return startDateTime;
    }

    public String getNotes() {
        return notes;
    }

    // Setters
    public void setInstallationId(String installationId) {
        this.installationId = installationId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public void setCalendarId(Long calendarId) {
        this.calendarId = calendarId;
    }

    public void setStartDateTime(ZonedDateTime startDateTime) {
        this.startDateTime = startDateTime;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("installationId", installationId)
                .append("productId", productId)
                .append("calendarId", calendarId)
                .append("startDateTime", startDateTime)
                .append("notes", notes)
                .toString();
    }

}
