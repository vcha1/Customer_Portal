package com.my1stle.customer.portal.service.installation.configuration.scheduling;

import com.dev1stle.scheduling.system.v1.factory.ITruckRollFactory;
import com.dev1stle.scheduling.system.v1.factory.TruckRollFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class TruckRollFactoryConfiguration {

    @Bean
    @Primary
    public ITruckRollFactory truckRollFactory() {
        return new TruckRollFactory();
    }


}
