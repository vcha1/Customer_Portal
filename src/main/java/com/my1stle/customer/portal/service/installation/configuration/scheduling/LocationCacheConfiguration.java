package com.my1stle.customer.portal.service.installation.configuration.scheduling;

import com.dev1stle.scheduling.system.v1.dao.LocationDao;
import com.dev1stle.scheduling.system.v1.service.caching.LocationCache;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class LocationCacheConfiguration {

    @Bean
    @Primary
    public LocationCache locationCache() {
        return new LocationDao();
    }

}
