package com.my1stle.customer.portal.service.installation.configuration.scheduling;

import com.dev1stle.scheduling.system.v1.service.caching.LocationCache;
import com.dev1stle.scheduling.system.v1.service.routing.DistanceMatrixService;
import com.dev1stle.scheduling.system.v1.service.routing.mapquest.MapQuestRouteMatrixClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class DistanceMatrixServiceConfiguration {

    @Bean
    @Primary
    @Autowired
    public DistanceMatrixService distanceMatrixService(@Value("${mapquest.api.key}") String apiKey, LocationCache locationCache){

        return new MapQuestRouteMatrixClient(apiKey, locationCache);

    }


}
