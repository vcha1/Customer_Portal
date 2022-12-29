package com.my1stle.customer.portal.service.installation.configuration.scheduling;

import com.dev1stle.scheduling.system.v1.service.routing.DistanceCalculator;
import com.dev1stle.scheduling.system.v1.service.routing.DistanceMatrixService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class DistanceCalculatorConfiguration {


    @Bean
    @Primary
    @Autowired
    public DistanceCalculator distanceCalculator(DistanceMatrixService distanceMatrixService) {
        return new DistanceCalculator(distanceMatrixService);
    }

}
