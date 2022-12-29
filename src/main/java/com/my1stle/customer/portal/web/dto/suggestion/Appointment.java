package com.my1stle.customer.portal.web.dto.suggestion;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "duration",
        "address"
})
public class Appointment {

    public Appointment() {

    }

    public Appointment(Long duration, String address) {
        this.duration = duration;
        this.address = address;
    }

    @JsonProperty("duration")
    private Long duration;
    @JsonProperty("address")
    private String address;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("duration")
    public Long getDuration() {
        return duration;
    }

    @JsonProperty("duration")
    public void setDuration(Long duration) {
        this.duration = duration;
    }

    @JsonProperty("address")
    public String getAddress() {
        return address;
    }

    @JsonProperty("address")
    public void setAddress(String address) {
        this.address = address;
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
        return new ToStringBuilder(this).append("duration", duration).append("address", address).append("additionalProperties", additionalProperties).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(duration).append(address).append(additionalProperties).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Appointment) == false) {
            return false;
        }
        Appointment rhs = ((Appointment) other);
        return new EqualsBuilder().append(duration, rhs.duration).append(address, rhs.address).append(additionalProperties, rhs.additionalProperties).isEquals();
    }

}