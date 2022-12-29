package com.my1stle.customer.portal.service.installation.configuration.scheduling;

import com.dev1stle.scheduling.system.v1.service.caching.ScheduleSuggestionRequestCache;
import com.dev1stle.scheduling.system.v1.service.routing.DistanceCalculator;
import com.dev1stle.scheduling.system.v1.service.routing.SchedulingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class SchedulingServiceConfiguration {

    @Bean
    @Primary
    @Autowired
    public SchedulingService schedulingService(
            ScheduleSuggestionRequestCache scheduleSuggestionRequestCache,
            DistanceCalculator distanceCalculator) {

        return new SchedulingService(scheduleSuggestionRequestCache, distanceCalculator);

    }

}
