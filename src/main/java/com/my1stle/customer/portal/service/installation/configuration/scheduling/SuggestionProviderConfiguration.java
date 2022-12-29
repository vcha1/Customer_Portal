package com.my1stle.customer.portal.service.installation.configuration.scheduling;

import com.dev1stle.scheduling.system.v1.service.assignee.AssigneeService;
import com.dev1stle.scheduling.system.v1.service.availability.AvailabilityService;
import com.dev1stle.scheduling.system.v1.service.availability.DefaultAvailabilityService;
import com.dev1stle.scheduling.system.v1.service.routing.SchedulingService;
import com.dev1stle.scheduling.system.v1.service.suggestion.provider.ExistingAppointmentService;
import com.dev1stle.scheduling.system.v1.service.suggestion.provider.ISuggestionProvider;
import com.dev1stle.scheduling.system.v1.service.suggestion.provider.SuggestionProvider;
import com.dev1stle.scheduling.system.v1.suggestion.strategy.MergeSuggestionStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class SuggestionProviderConfiguration {

    @Bean
    @Primary
    @Autowired
    public ISuggestionProvider suggestionProvider(
            SchedulingService schedulingService,
            ExistingAppointmentService existingAppointmentService,
            MergeSuggestionStrategy mergeSuggestionStrategy) {

        return new SuggestionProvider(schedulingService, existingAppointmentService, mergeSuggestionStrategy);

    }

    @Bean
    @Primary
    @Autowired
    public AvailabilityService availabilityService(ExistingAppointmentService existingAppointmentService) {
        return new DefaultAvailabilityService(existingAppointmentService);
    }

}