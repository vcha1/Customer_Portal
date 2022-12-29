package com.my1stle.customer.portal.service.installation.configuration.scheduling;

import com.dev1stle.scheduling.system.v1.suggestion.strategy.MergeSuggestionStrategy;
import com.dev1stle.scheduling.system.v1.suggestion.strategy.MinimizeNumberOfResourcesSuggestionStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class MergeSuggestionStrategyConfiguration {

    @Bean
    @Primary
    public MergeSuggestionStrategy mergeSuggestionStrategy() {
        return new MinimizeNumberOfResourcesSuggestionStrategy();
    }

}
