package com.my1stle.customer.portal.service.installation.configuration.geocoding;

import com.dev1stle.scheduling.system.v1.service.caching.LocationCache;
import com.dev1stle.scheduling.system.v1.service.geocoding.GeocodingService;
import com.dev1stle.scheduling.system.v1.service.geocoding.MapQuestCachedGeocodingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class GeocodingServiceConfiguration {


    @Bean
    @Primary
    @Autowired
    public GeocodingService geocodingService(
            @Value("${mapquest.api.key}") String mapQuestApiKey,
            LocationCache locationCache) {

        return new MapQuestCachedGeocodingService(mapQuestApiKey, locationCache);

    }

}