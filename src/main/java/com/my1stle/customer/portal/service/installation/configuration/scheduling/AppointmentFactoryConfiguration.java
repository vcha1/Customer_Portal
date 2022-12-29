package com.my1stle.customer.portal.service.installation.configuration.scheduling;

import com.dev1stle.scheduling.system.v1.factory.AppointmentFactory;
import com.dev1stle.scheduling.system.v1.factory.IAppointmentFactory;
import com.dev1stle.scheduling.system.v1.factory.IEventFactory;
import com.dev1stle.scheduling.system.v1.factory.ITruckRollFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class AppointmentFactoryConfiguration {

    @Bean
    @Primary
    @Autowired
    public IAppointmentFactory appointmentFactory(
            IEventFactory eventFactory,
            ITruckRollFactory truckRollFactory
    ) {

        return new AppointmentFactory(eventFactory, truckRollFactory);

    }
}
