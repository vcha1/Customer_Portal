package com.my1stle.customer.portal.service.odoo;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesUtility {
    public PropertiesUtility() {
    }

    public static Properties loadApplicationProperties() throws IOException {
        Properties applicationProperties = new Properties();
        InputStream resourceAsStream = PropertiesUtility.class.getResourceAsStream("/application.properties");
        applicationProperties.load(resourceAsStream);
        return applicationProperties;
    }
}
