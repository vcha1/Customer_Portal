package com.my1stle.customer.portal.service.installation.configuration.scheduling;

import com.dev1stle.scheduling.system.v1.service.appointment.IAppointmentService;
import com.dev1stle.scheduling.system.v1.service.assignee.AssigneeService;
import com.dev1stle.scheduling.system.v1.service.truck.roll.IRescheduleTruckRollService;
import com.dev1stle.scheduling.system.v1.service.truck.roll.RescheduleTruckRollService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class RescheduleTruckRollServiceConfiguration {


    @Bean
    @Primary
    @Autowired
    public IRescheduleTruckRollService rescheduleTruckRollService(IAppointmentService appointmentService, AssigneeService assigneeService) {
        return new RescheduleTruckRollService(appointmentService, assigneeService);
    }


}
