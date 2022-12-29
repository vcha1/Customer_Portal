package com.my1stle.customer.portal.web.converter;

import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.my1stle.customer.portal.service.scheduling.CustomerSelfSchedulingRole;
import com.my1stle.customer.portal.web.dto.scheduling.CustomerSelfSchedulingRequestDecodedJwt;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

class CustomerSelfSchedulingRequestDecodedJwtDecorator implements CustomerSelfSchedulingRequestDecodedJwt {

    private Long duration;
    private CustomerSelfSchedulingRole role;
    private String iss;
    private String installationId;
    private String opportunityId;
    private List<Long> skillIds = new ArrayList<>();
    private ZonedDateTime expirationDateTime;
    private ZonedDateTime issueDateTime;
    private String clientId;
    private boolean isBatteryInstall;
    private String directSalesCustomerId;

    private DecodedJWT decodedJWT;

    private CustomerSelfSchedulingRequestDecodedJwtDecorator(DecodedJWT decodedJWT) {
        this.decodedJWT = decodedJWT;
    }

    static CustomerSelfSchedulingRequestDecodedJwtDecorator from(DecodedJWT jwt) {

        CustomerSelfSchedulingRequestDecodedJwtDecorator decorator = new CustomerSelfSchedulingRequestDecodedJwtDecorator(jwt);

        Claim duration = jwt.getClaim("duration");
        Claim role = jwt.getClaim("role");
        Claim installationId = jwt.getClaim("installationId");
        Claim opportunityId = jwt.getClaim("opportunityId");
        Claim skillIds = jwt.getClaim("skillIds");
        Claim clientId = jwt.getClaim("clientId");
        Claim isBatteryInstall = jwt.getClaim("isBatteryInstall");
        Claim directSalesCustomerId = jwt.getClaim("directSalesCustomerId");

        decorator.duration = duration.isNull() ? null : duration.asString() != null ? Long.parseLong(duration.asString()) : duration.asLong();
        decorator.role = role.isNull() ? null : role.as(CustomerSelfSchedulingRole.class);
        decorator.iss = jwt.getIssuer();
        decorator.installationId = installationId.isNull() ? null : installationId.asString();
        decorator.opportunityId = opportunityId.isNull() ? null : opportunityId.asString();
        decorator.skillIds = skillIds.isNull() ? null : skillIds.asList(Long.class);
        decorator.expirationDateTime = jwt.getExpiresAt() == null ? null : ZonedDateTime.ofInstant(jwt.getExpiresAt().toInstant(), ZoneOffset.UTC);
        decorator.issueDateTime = jwt.getIssuedAt() == null ? null : ZonedDateTime.ofInstant(jwt.getIssuedAt().toInstant(), ZoneOffset.UTC);
        decorator.clientId = clientId.isNull() ? null : clientId.asString();
        decorator.isBatteryInstall = !isBatteryInstall.isNull() && Boolean.parseBoolean(isBatteryInstall.asString());
        decorator.directSalesCustomerId = directSalesCustomerId.isNull() ? null : directSalesCustomerId.asString();

        return decorator;

    }

    @Override
    public boolean isBatteryInstall() {
        return isBatteryInstall;
    }

    @Override
    public Long getDuration() {
        return duration;
    }

    @Override
    public CustomerSelfSchedulingRole getRole() {
        return role;
    }

    @Override
    public String getDirectSalesCustomerId() {
        return directSalesCustomerId;
    }

    @Override
    public String getInstallationId() {
        return installationId;
    }

    @Override
    public String getOpportunityId() {
        return opportunityId;
    }

    @Override
    public List<Long> getSkillIds() {
        return skillIds;
    }

    @Override
    public ZonedDateTime getExpirationDateTime() {
        return expirationDateTime;
    }

    @Override
    public ZonedDateTime getIssueDateTime() {
        return issueDateTime;
    }

    @Override
    public String getClientId() {
        return clientId;
    }

    @Override
    public String getToken() {
        return decodedJWT.getToken();
    }

    @Override
    public String getHeader() {
        return decodedJWT.getHeader();
    }

    @Override
    public String getPayload() {
        return decodedJWT.getPayload();
    }

    @Override
    public String getSignature() {
        return decodedJWT.getSignature();
    }

    @Override
    public String getAlgorithm() {
        return decodedJWT.getAlgorithm();
    }

    @Override
    public String getType() {
        return decodedJWT.getType();
    }

    @Override
    public String getContentType() {
        return decodedJWT.getContentType();
    }

    @Override
    public String getKeyId() {
        return decodedJWT.getKeyId();
    }

    @Override
    public Claim getHeaderClaim(String s) {
        return decodedJWT.getHeaderClaim(s);
    }

    @Override
    public String getIssuer() {
        return decodedJWT.getIssuer();
    }

    @Override
    public String getSubject() {
        return decodedJWT.getSubject();
    }

    @Override
    public List<String> getAudience() {
        return decodedJWT.getAudience();
    }

    @Override
    public Date getExpiresAt() {
        return decodedJWT.getExpiresAt();
    }

    @Override
    public Date getNotBefore() {
        return decodedJWT.getNotBefore();
    }

    @Override
    public Date getIssuedAt() {
        return decodedJWT.getIssuedAt();
    }

    @Override
    public String getId() {
        return decodedJWT.getId();
    }

    @Override
    public Claim getClaim(String s) {
        return decodedJWT.getClaim(s);
    }

    @Override
    public Map<String, Claim> getClaims() {
        return decodedJWT.getClaims();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof CustomerSelfSchedulingRequestDecodedJwtDecorator)) return false;

        CustomerSelfSchedulingRequestDecodedJwtDecorator that = (CustomerSelfSchedulingRequestDecodedJwtDecorator) o;

        return new EqualsBuilder()
                .append(isBatteryInstall, that.isBatteryInstall)
                .append(duration, that.duration)
                .append(role, that.role)
                .append(iss, that.iss)
                .append(installationId, that.installationId)
                .append(opportunityId, that.opportunityId)
                .append(skillIds, that.skillIds)
                .append(expirationDateTime, that.expirationDateTime)
                .append(issueDateTime, that.issueDateTime)
                .append(clientId, that.clientId)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(duration)
                .append(role)
                .append(iss)
                .append(installationId)
                .append(opportunityId)
                .append(skillIds)
                .append(expirationDateTime)
                .append(issueDateTime)
                .append(clientId)
                .append(isBatteryInstall)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("duration", duration)
                .append("role", role)
                .append("iss", iss)
                .append("installationId", installationId)
                .append("opportunityId", opportunityId)
                .append("expirationDateTime", expirationDateTime)
                .append("issueDateTime", issueDateTime)
                .append("clientId", clientId)
                .append("isBatteryInstall", isBatteryInstall)
                .toString();
    }
}
