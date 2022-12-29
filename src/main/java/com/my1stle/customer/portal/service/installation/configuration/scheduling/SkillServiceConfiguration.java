package com.my1stle.customer.portal.service.installation.configuration.scheduling;

import com.dev1stle.scheduling.system.v1.dao.SkillDao;
import com.dev1stle.scheduling.system.v1.service.skill.SkillService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class SkillServiceConfiguration {


    @Bean
    @Primary
    public SkillService skillService(){
        return new SkillDao();
    }

}
