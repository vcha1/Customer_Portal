package org.baeldung.spring;

import com.maxmind.db.Reader;
import com.maxmind.geoip2.DatabaseReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.ResourceUtils;
import ua_parser.Parser;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

@Configuration
public class LoginNotificationConfig {

    @Bean
    public Parser uaParser() throws IOException {
        return new Parser();
    }
/*
    @Bean
    public DatabaseReader databaseReader(ResourceLoader resourceLoader) throws IOException {

        Resource resource = resourceLoader.getResource("classpath:maxmind/GeoLite2-City.mmdb") ;
        InputStream dbAsStream = resource.getInputStream();

        DatabaseReader reader = new DatabaseReader
                .Builder(dbAsStream)
                .fileMode(Reader.FileMode.MEMORY)
                .build() ;

        return reader ;

    }
    */

}
