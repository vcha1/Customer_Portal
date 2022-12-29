package com.my1stle.customer.portal.service.installation.configuration.scheduling;

import com.dev1stle.scheduling.system.v1.service.appointment.IAppointmentService;
import com.dev1stle.scheduling.system.v1.service.truck.roll.EditTruckRollDetailsService;
import com.dev1stle.scheduling.system.v1.service.truck.roll.IEditTruckRollDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class TruckRollEditDetailServiceConfiguration {

    @Bean
    @Primary
    public IEditTruckRollDetailsService editTruckRollDetailsService(IAppointmentService appointmentService) {
        return new EditTruckRollDetailsService(appointmentService);
    }

}
