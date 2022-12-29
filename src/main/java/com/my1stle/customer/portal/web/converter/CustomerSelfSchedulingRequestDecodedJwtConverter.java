package com.my1stle.customer.portal.web.converter;

import com.auth0.jwt.interfaces.JWTVerifier;
import com.my1stle.customer.portal.service.util.LRUCache;
import com.my1stle.customer.portal.web.dto.scheduling.CustomerSelfSchedulingRequestDecodedJwt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;

@Service
public class CustomerSelfSchedulingRequestDecodedJwtConverter implements Converter<String, CustomerSelfSchedulingRequestDecodedJwt> {

    private JWTVerifier jwtVerifier;

    private Map<String, CustomerSelfSchedulingRequestDecodedJwt> decodedJWTCache = Collections.synchronizedMap(
            new LRUCache<>(100)
    );

    @Autowired
    public CustomerSelfSchedulingRequestDecodedJwtConverter(JWTVerifier jwtVerifier) {
        this.jwtVerifier = jwtVerifier;
    }

    @Override
    public CustomerSelfSchedulingRequestDecodedJwt convert(String s) {
        return decodedJWTCache.computeIfAbsent(s, token -> {
            return CustomerSelfSchedulingRequestDecodedJwtDecorator.from(jwtVerifier.verify(token));
        });
    }

}
