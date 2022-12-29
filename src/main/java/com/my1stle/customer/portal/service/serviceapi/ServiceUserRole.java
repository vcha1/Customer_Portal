package com.my1stle.customer.portal.service.serviceapi;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.WordUtils;

public enum ServiceUserRole {

    UNKNOWN, EMPLOYEE, CUSTOMER;

    @JsonCreator
    public static ServiceUserRole forValue(String value) {
        if (StringUtils.isBlank(value)) {
            return UNKNOWN;
        }
        try {
            return ServiceUserRole.valueOf(StringUtils.upperCase(value.replaceAll(" ", "_")));
        } catch (IllegalArgumentException e) {
            return UNKNOWN;
        }
    }

    @JsonValue
    public String toValue() {
        return WordUtils.capitalizeFully(this.name().replaceAll("_", " "));
    }

}