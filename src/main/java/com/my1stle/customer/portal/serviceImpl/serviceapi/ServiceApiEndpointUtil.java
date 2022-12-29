package com.my1stle.customer.portal.serviceImpl.serviceapi;

import com.my1stle.customer.portal.service.serviceapi.ServiceApiCredentials;
import org.apache.http.client.utils.URIBuilder;

import java.net.MalformedURLException;
import java.net.URISyntaxException;

class ServiceApiEndpointUtil {

    private ServiceApiEndpointUtil() {

    }

    static String endpoint(ServiceApiCredentials credentials, String resourcePath) {
        try {
            return new URIBuilder()
                    .setScheme("https")
                    .setHost(credentials.getHost())
                    .setPath(credentials.getEnvironment().concat("/").concat(resourcePath))
                    .build()
                    .toURL()
                    .toString();
        } catch (MalformedURLException | URISyntaxException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

}
