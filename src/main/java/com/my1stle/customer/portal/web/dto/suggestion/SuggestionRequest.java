
package com.my1stle.customer.portal.web.dto.suggestion;

import com.dev1stle.scheduling.system.v1.model.Calendar;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "resources",
        "start",
        "end",
        "timezone",
        "appointment",
        "blockDuration",
        "tolerance"
})
public class SuggestionRequest {

    @JsonProperty("resources")
    private List<Calendar> resources = new ArrayList<Calendar>();
    @JsonProperty("start")
    private ZonedDateTime start;
    @JsonProperty("end")
    private ZonedDateTime end;
    @JsonProperty("timezone")
    private ZoneId timezone;
    @JsonProperty("appointment")
    private Appointment appointment;
    @JsonProperty("blockDuration")
    private Long blockDuration;
    @JsonProperty("tolerance")
    private Long tolerance;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * TODO : should not be serializing/deserializing internal entities
     *
     * @return
     */
    @Deprecated
    @JsonProperty("resources")
    public List<Calendar> getResources() {
        return resources;
    }

    /**
     * TODO : should not be serializing/deserializing internal entities
     *
     * @param resources
     */
    @JsonProperty("resources")
    public void setResources(List<Calendar> resources) {
        this.resources = resources;
    }

    @JsonProperty("start")
    public ZonedDateTime getStart() {
        return start;
    }

    @JsonProperty("start")
    public void setStart(ZonedDateTime start) {
        this.start = start;
    }

    @JsonProperty("end")
    public ZonedDateTime getEnd() {
        return end;
    }

    @JsonProperty("end")
    public void setEnd(ZonedDateTime end) {
        this.end = end;
    }

    @JsonProperty("timezone")
    public ZoneId getTimezone() {
        return timezone;
    }

    @JsonProperty("timezone")
    public void setTimezone(ZoneId timezone) {
        this.timezone = timezone;
    }

    @JsonProperty("appointment")
    public Appointment getAppointment() {
        return appointment;
    }

    @JsonProperty("appointment")
    public void setAppointment(Appointment appointment) {
        this.appointment = appointment;
    }

    @JsonProperty("blockDuration")
    public Long getBlockDuration() {
        return blockDuration;
    }

    @JsonProperty("blockDuration")
    public void setBlockDuration(Long blockDuration) {
        this.blockDuration = blockDuration;
    }

    @JsonProperty("tolerance")
    public Long getTolerance() {
        return tolerance;
    }

    @JsonProperty("tolerance")
    public void setTolerance(Long tolerance) {
        this.tolerance = tolerance;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("resources", resources).append("start", start).append("end", end).append("timezone", timezone).append("appointment", appointment).append("blockDuration", blockDuration).append("tolerance", tolerance).append("additionalProperties", additionalProperties).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(resources).append(timezone).append(start).append(additionalProperties).append(tolerance).append(blockDuration).append(end).append(appointment).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof SuggestionRequest) == false) {
            return false;
        }
        SuggestionRequest rhs = ((SuggestionRequest) other);
        return new EqualsBuilder().append(resources, rhs.resources).append(timezone, rhs.timezone).append(start, rhs.start).append(additionalProperties, rhs.additionalProperties).append(tolerance, rhs.tolerance).append(blockDuration, rhs.blockDuration).append(end, rhs.end).append(appointment, rhs.appointment).isEquals();
    }

}
