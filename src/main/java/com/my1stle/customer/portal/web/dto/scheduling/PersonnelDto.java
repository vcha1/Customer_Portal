package com.my1stle.customer.portal.web.dto.scheduling;

import com.dev1stle.scheduling.system.v1.service.truck.roll.api.model.CalendarDetail;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.time.ZoneId;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "id"
})
public class PersonnelDto {

    @JsonProperty("id")
    private long id;

    @JsonProperty("timeZone")
    private String timezone;

    public PersonnelDto() {
    }

    public PersonnelDto(long id) {
        this.id = id;
    }

    public static PersonnelDto from(CalendarDetail calendarDetail) {
        PersonnelDto dto = new PersonnelDto();
        dto.id = calendarDetail.getId();
        dto.timezone = calendarDetail.getTimeZone().getId();
        return dto;
    }

    public long getId() {
        return id;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof PersonnelDto)) return false;

        PersonnelDto that = (PersonnelDto) o;

        return new EqualsBuilder()
                .append(id, that.id)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(id)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .toString();
    }
}
