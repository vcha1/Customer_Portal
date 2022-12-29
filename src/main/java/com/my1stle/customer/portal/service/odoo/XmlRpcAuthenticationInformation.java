package com.my1stle.customer.portal.service.odoo;

import java.net.URI;

public interface XmlRpcAuthenticationInformation {
    URI getServerUri();

    String getDatabase();

    String getUsername();

    int getUserId();

    String getPassword();
}
