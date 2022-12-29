package com.my1stle.customer.portal.service.serviceapi;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Objects;
import java.util.Set;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "firstName",
        "lastName",
        "email",
        "mobilePhone",
        "roles"
})
public class ServiceUserDto {


    @JsonProperty("firstName")
    String firstName;
    @JsonProperty("lastName")
    String lastName;
    @JsonProperty("email")
    String email;
    @JsonProperty("mobilePhone")
    String mobilePhone;
    @JsonProperty("roles")
    Set<ServiceUserRole> roles;

    ServiceUserDto() {

    }

    public ServiceUserDto(String firstName, String lastName, String email, Set<ServiceUserRole> roles) {
        Objects.requireNonNull(firstName);
        Objects.requireNonNull(lastName);
        Objects.requireNonNull(email);
        Objects.requireNonNull(roles);
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.roles = roles;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public Set<ServiceUserRole> getRoles() {
        return roles;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;

        if (!(o instanceof ExistingServiceUserDto)) return false;

        ServiceUserDto userDto = (ServiceUserDto) o;

        return new EqualsBuilder()
                .append(email, userDto.email)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(email)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.JSON_STYLE)
                .append("firstName", firstName)
                .append("lastName", lastName)
                .append("email", email)
                .append("mobilePhone", mobilePhone)
                .append("role", roles)
                .toString();
    }
}
