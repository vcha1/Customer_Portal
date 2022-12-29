package com.my1stle.customer.portal.web.dto.scheduling;

import com.auth0.jwt.interfaces.DecodedJWT;

import java.time.ZonedDateTime;

public interface CustomerSelfSchedulingRequestDecodedJwt extends CustomerSelfSchedulingRequest, DecodedJWT {

    ZonedDateTime getExpirationDateTime();

    ZonedDateTime getIssueDateTime();

}
