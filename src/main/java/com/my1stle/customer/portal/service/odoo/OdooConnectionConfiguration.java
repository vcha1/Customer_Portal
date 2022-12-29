
package com.my1stle.customer.portal.service.odoo;


import com.my1stle.customer.portal.service.odoo.PropertiesUtility;
import com.my1stle.customer.portal.service.odoo.XmlRpcAuthenticationInformation;
import com.my1stle.customer.portal.service.odoo.XmlRpcClientFactory;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.Properties;
import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfig;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class OdooConnectionConfiguration {
    private static final String ODOO_URL_PROPERTY = "odoo.1le.url";
    private static final String ODOO_DATABASE_PROPERTY = "odoo.1le.database";
    private static final String ODOO_USERNAME_PROPERTY = "odoo.1le.username";
    private static final String ODOO_PASSWORD_PROPERTY = "odoo.1le.password";

    public OdooConnectionConfiguration() {
    }

    @Bean
    @Primary
    public XmlRpcAuthenticationInformation xmlRpcAuthenticationInformation(@Value("${odoo.1le.url}") String databaseUrl, @Value("${odoo.1le.database}") String databaseName, @Value("${odoo.1le.username}") String username, @Value("${odoo.1le.password}") String password) {
        try {
            return authenticateXmlRpcConnection(databaseUrl, databaseName, username, password);
        } catch (RuntimeException var6) {
            throw new BeanInitializationException(var6.getMessage(), var6);
        }
    }

    public static XmlRpcAuthenticationInformation generateDefaultXmlRpcAuthenticationInformation() {
        Properties applicationProperties;
        try {
            applicationProperties = PropertiesUtility.loadApplicationProperties();
        } catch (IOException var5) {
            throw new RuntimeException(var5.getMessage(), var5);
        }

        String databaseUrl = applicationProperties.getProperty("odoo.1le.url");
        String databaseName = applicationProperties.getProperty("odoo.1le.database");
        String username = applicationProperties.getProperty("odoo.1le.username");
        String password = applicationProperties.getProperty("odoo.1le.password");
        return authenticateXmlRpcConnection(databaseUrl, databaseName, username, password);
    }

    private static XmlRpcAuthenticationInformation authenticateXmlRpcConnection(String databaseUrl, String database, String username, String password) {
        URI odooServerUri;
        try {
            odooServerUri = new URI(databaseUrl);
        } catch (URISyntaxException var11) {
            throw new RuntimeException(var11.getMessage(), var11);
        }

        URL commonEndpoint;
        try {
            commonEndpoint = odooServerUri.resolve("/xmlrpc/2/common").toURL();
        } catch (MalformedURLException var10) {
            throw new RuntimeException(var10.getMessage(), var10);
        }

        XmlRpcClientConfigImpl loginConfig = new XmlRpcClientConfigImpl();
        loginConfig.setServerURL(commonEndpoint);
        XmlRpcClient loginClient = new XmlRpcClient();
        loginClient.setConfig(loginConfig);

        try {
            int userId = (Integer)loginClient.execute("authenticate", Arrays.asList(database, username, password, Collections.emptyMap()));
            return new AuthenticationInformation(odooServerUri, database, username, userId, password);
        } catch (XmlRpcException var9) {
            throw new RuntimeException(var9.getMessage(), var9);
        }
    }

    @Bean
    @Primary
    public XmlRpcClientFactory xmlRpcClientFactory() {
        return createDefaultXmlRpcClientFactory();
    }

    public static XmlRpcClientFactory createDefaultXmlRpcClientFactory() {
        return new DefaultXmlRpcClientFactory();
    }

    private static class DefaultXmlRpcClientFactory implements XmlRpcClientFactory {
        private DefaultXmlRpcClientFactory() {
        }

        public XmlRpcClient createClient(XmlRpcClientConfig config) {
            XmlRpcClient client = new XmlRpcClient();
            client.setConfig(config);
            return client;
        }
    }

    private static class AuthenticationInformation implements XmlRpcAuthenticationInformation {
        private final URI uri;
        private final String db;
        private final String username;
        private final int userId;
        private final String pw;

        AuthenticationInformation(URI uri, String db, String username, int userId, String pw) {
            this.uri = uri;
            this.db = db;
            this.username = username;
            this.userId = userId;
            this.pw = pw;
        }

        public URI getServerUri() {
            return this.uri;
        }

        public String getDatabase() {
            return this.db;
        }

        public String getUsername() {
            return this.username;
        }

        public int getUserId() {
            return this.userId;
        }

        public String getPassword() {
            return this.pw;
        }
    }
}
