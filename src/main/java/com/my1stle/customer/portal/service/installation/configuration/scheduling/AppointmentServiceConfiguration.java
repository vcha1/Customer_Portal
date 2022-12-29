package com.my1stle.customer.portal.service.installation.configuration.scheduling;

import com.dev1stle.repository.salesforce.SalesforceObjectRepository;
import com.dev1stle.scheduling.system.v1.dao.Dao;
import com.dev1stle.scheduling.system.v1.model.Event;
import com.dev1stle.scheduling.system.v1.model.salesforce.TruckRoll;
import com.dev1stle.scheduling.system.v1.service.appointment.AppointmentService;
import com.dev1stle.scheduling.system.v1.service.appointment.IAppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class AppointmentServiceConfiguration {

    @Bean
    @Primary
    @Autowired
    public IAppointmentService appointmentService(
            SalesforceObjectRepository<TruckRoll> truckRollSalesforceObjectRepository, Dao<Long, Event> eventDao) {
        return new AppointmentService(truckRollSalesforceObjectRepository, eventDao);
    }

}
