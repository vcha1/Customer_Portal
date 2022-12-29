package com.my1stle.customer.portal.service.installation.configuration.scheduling;

import com.dev1stle.scheduling.system.v1.service.appointment.IAppointmentService;
import com.dev1stle.scheduling.system.v1.service.truck.roll.CancelTruckRollService;
import com.dev1stle.scheduling.system.v1.service.truck.roll.ICancelTruckRollService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class CancelTruckRollServiceConfiguration {

    @Bean
    @Primary
    @Autowired
    public ICancelTruckRollService cancelTruckRollService(IAppointmentService appointmentService) {
        return new CancelTruckRollService(appointmentService);
    }

}
