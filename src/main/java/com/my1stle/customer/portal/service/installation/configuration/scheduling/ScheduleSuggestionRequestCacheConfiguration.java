package com.my1stle.customer.portal.service.installation.configuration.scheduling;

import com.dev1stle.scheduling.system.v1.dao.HibernateDao;
import com.dev1stle.scheduling.system.v1.model.ScheduleSuggestionRequest;
import com.dev1stle.scheduling.system.v1.service.caching.ScheduleSuggestionRequestCache;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class ScheduleSuggestionRequestCacheConfiguration {

    @Bean
    @Primary
    public ScheduleSuggestionRequestCache scheduleSuggestionRequestCache() {
        return new SuggestionRequestCache();
    }

    private class SuggestionRequestCache extends HibernateDao<Long, ScheduleSuggestionRequest> implements ScheduleSuggestionRequestCache {

        @Override
        public ScheduleSuggestionRequest findFirstByRequestOrderByCreatedDateDesc(String request) {

            String hql = "SELECT s FROM ScheduleSuggestionRequest s WHERE s.request = :cached_requested order by s.createdDate desc";
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("cached_requested", request);

            Collection<ScheduleSuggestionRequest> result = query(hql, parameters);

            if (result.isEmpty())
                return null;
            else
                return null;

        }
    }

}
