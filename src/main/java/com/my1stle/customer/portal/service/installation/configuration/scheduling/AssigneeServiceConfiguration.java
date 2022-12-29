package com.my1stle.customer.portal.service.installation.configuration.scheduling;

import com.dev1stle.scheduling.system.v1.dao.CalendarDao;
import com.dev1stle.scheduling.system.v1.service.assignee.AssigneeService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class AssigneeServiceConfiguration {

    @Bean
    @Primary
    public AssigneeService assigneeService() {

        return new CalendarDao();

    }

}
