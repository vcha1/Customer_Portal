package com.my1stle.customer.portal.service.installation.configuration.scheduling;

import com.dev1stle.scheduling.system.v1.factory.EventFactory;
import com.dev1stle.scheduling.system.v1.factory.IEventFactory;
import com.dev1stle.scheduling.system.v1.service.tag.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class EventFactoryConfiguration {

    @Bean
    @Primary
    @Autowired
    public IEventFactory eventFactory(TagService tagService) {
        return new EventFactory(tagService);
    }

}
