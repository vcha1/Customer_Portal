package com.my1stle.customer.portal.service.installation.configuration.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.JWTVerifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class JWTVerifierConfiguration {

    @Bean
    @Primary
    public JWTVerifier jwtVerifier(@Value("${self.schedule.secret.key}") String secret) {

        return JWT.require(Algorithm.HMAC256(secret)).build();

    }

}
