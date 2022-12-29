
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
        "firstName",
        "lastName",
        "email",
        "mobilePhone",
        "roles"
})
public class ExistingServiceUserDto extends ServiceUserDto {

    @JsonProperty("id")
    private long id;

    private ExistingServiceUserDto() {

    }

    public long getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof ExistingServiceUserDto)) return false;

        ExistingServiceUserDto userDto = (ExistingServiceUserDto) o;

        return new EqualsBuilder()
                .append(id, userDto.id)
                .append(email, userDto.email)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(id)
                .append(email)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.JSON_STYLE)
                .append("id", id)
                .append("firstName", firstName)
                .append("lastName", lastName)
                .append("email", email)
                .append("mobilePhone", mobilePhone)
                .append("role", roles)
                .toString();
    }

}
