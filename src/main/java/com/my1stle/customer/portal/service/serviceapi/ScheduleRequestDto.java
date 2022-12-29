package com.my1stle.customer.portal.service.serviceapi;

import com.fasterxml.jackson.annotation.*;
import org.apache.commons.lang3.builder.*;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "role",
        "clientId",
        "iss",
        "isBatteryInstall",
        "installationId",
        "exp",
        "iat",
        "token",
        "duration"
})
public class ScheduleRequestDto {

    public ScheduleRequestDto(String role, String clientId, String iss, Boolean isBatteryInstall, String installationId, Long exp, Long iat, String token, Long duration) {
        this.role = role;
        this.clientId = clientId;
        this.iss = iss;
        this.isBatteryInstall = isBatteryInstall;
        this.installationId = installationId;
        this.exp = exp;
        this.iat = iat;
        this.token = token;
        this.duration = duration;
    }

    public ScheduleRequestDto(){ }

    @JsonProperty("role")
    private String role;

    @JsonProperty("clientId")
    private String clientId;

    @JsonProperty("iss")
    private String iss;

    @JsonProperty("isBatteryInstall")
    private Boolean isBatteryInstall;

    @JsonProperty("installationId")
    private String installationId;

    @JsonProperty("exp")
    private Long exp;

    @JsonProperty("iat")
    private Long iat;

    @JsonProperty("token")
    private String token;

    @JsonProperty("duration")
    private Long duration;


    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getIss() {
        return iss;
    }

    public void setIss(String iss) {
        this.iss = iss;
    }

    public Boolean getBatteryInstall() {
        return this.isBatteryInstall;
    }

    public void setBatteryInstall(Boolean isBatteryInstall) {
        this.isBatteryInstall = isBatteryInstall;
    }

    public String getInstallationId() {
        return installationId;
    }

    public void setInstallationId(String installationId) {
        this.installationId = installationId;
    }

    public Long getExp() {
        return exp;
    }

    public void setExp(Long exp) {
        this.exp = exp;
    }

    public Long getIat() {
        return iat;
    }

    public void setIat(Long iat) {
        this.iat = iat;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof ScheduleRequestDto)) return false;

        ScheduleRequestDto that = (ScheduleRequestDto) o;

        return new EqualsBuilder()
                .append(role, that.role)
                .append(clientId, that.clientId)
                .append(iss, that.iss)
                .append(isBatteryInstall, that.isBatteryInstall)
                .append(installationId, that.installationId)
                .append(exp, that.exp)
                .append(iat, that.iat)
                .append(token, that.token)
                .append(duration, that.duration)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(role)
                .append(clientId)
                .append(iss)
                .append(isBatteryInstall)
                .append(installationId)
                .append(exp)
                .append(iat)
                .append(token)
                .append(duration)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.JSON_STYLE)
                .append("role", role)
                .append("clientId", clientId)
                .append("iss", iss)
                .append("isBatteryInstall", isBatteryInstall)
                .append("installationId", installationId)
                .append("exp", exp)
                .append("iat", iat)
                .append("token", token)
                .append("duration", duration)
                .toString();
    }
    static public class Builder{

        public Builder(){}

        private String role;
        private String clientId;
        private String iss;
        private Boolean isBatteryInstall;
        private String installationId;
        private Long exp;
        private Long iat;
        private String token;
        private Long duration;

        public Builder role(String role) {
            this.role = role;
            return this;
        }

        public Builder clientId(String clientId) {
            this.clientId = clientId;
            return this;
        }

        public Builder iss(String iss) {
            this.iss = iss;
            return this;
        }

        public Builder isBatteryInstall(Boolean isBatteryInstall) {
            this.isBatteryInstall = isBatteryInstall;
            return this;
        }

        public Builder installationId(String installationId) {
            this.installationId = installationId;
            return this;
        }

        public Builder exp(Long exp) {
            this.exp = exp;
            return this;
        }

        public Builder iat(Long iat) {
            this.iat = iat;
            return this;
        }

        public Builder token(String token) {
            this.token = token;
            return this;
        }

        public Builder duration(Long duration) {
            this.duration = duration;
            return this;
        }


        public ScheduleRequestDto build(){
            return new ScheduleRequestDto(
                    this.role,
                    this.clientId,
                    this.iss,
                    this.isBatteryInstall,
                    this.installationId,
                    this.exp,
                    this.iat,
                    this.token,
                    this.duration
            );
        }
    }
}
