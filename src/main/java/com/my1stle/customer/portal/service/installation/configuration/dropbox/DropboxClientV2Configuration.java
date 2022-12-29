package com.my1stle.customer.portal.service.installation.configuration.dropbox;

import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.http.StandardHttpRequestor;
import com.dropbox.core.v2.DbxClientV2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class DropboxClientV2Configuration {

    @Bean
    @Primary
    public DbxClientV2 dbxClientV2(@Value("${1le.dropbox.token}") String token) {

        DbxRequestConfig requestConfig = DbxRequestConfig.newBuilder("dropbox/1le_customer_portal")
                .withHttpRequestor(new StandardHttpRequestor(StandardHttpRequestor.Config.DEFAULT_INSTANCE))
                .build();

        return new DbxClientV2(requestConfig, token);

    }


}
