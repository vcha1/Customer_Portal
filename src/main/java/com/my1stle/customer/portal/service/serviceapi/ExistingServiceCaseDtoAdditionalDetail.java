package com.my1stle.customer.portal.service.serviceapi;

import com.fasterxml.jackson.annotation.*;

import java.time.*;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "scheduledServiceDate"
})

public class ExistingServiceCaseDtoAdditionalDetail {

    @JsonProperty("scheduledServiceDate")
    LocalDateTime scheduledServiceDate;

    public LocalDateTime getScheduledServiceDate() {
        return scheduledServiceDate;
    }

    public void setScheduledServiceDate(LocalDateTime scheduledServiceDate) {
        this.scheduledServiceDate = scheduledServiceDate;
    }
}
