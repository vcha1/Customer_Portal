package com.my1stle.customer.portal.service.installation.configuration.scheduling;

import com.dev1stle.scheduling.system.v1.dao.EventDao;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class EventDaoConfiguration {

    @Bean
    @Primary
    public EventDao eventDao() {
       return new EventDao();
    }
}
