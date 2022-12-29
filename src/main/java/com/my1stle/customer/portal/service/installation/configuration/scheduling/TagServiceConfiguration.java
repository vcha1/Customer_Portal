package com.my1stle.customer.portal.service.installation.configuration.scheduling;

import com.dev1stle.scheduling.system.v1.dao.TagDao;
import com.dev1stle.scheduling.system.v1.service.tag.TagService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class TagServiceConfiguration {

    @Bean
    @Primary
    public TagService tagService() {
        return new TagDao();
    }

}
