package org.vt.aggregation.config.dataextractor;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.vt.aggregation.config.context.AggregationContext;
import org.vt.aggregation.domain.User;
import org.vt.aggregation.service.DataExtractor;
import org.vt.aggregation.service.UserDataExtractorImpl;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class DataExtractorsConfiguration {

    @Bean
    public List<DataExtractor<User>> userDataExtractors(AggregationContext AggregationContext) {
        return AggregationContext.dataSourcesContexts()
                .stream()
                .map(UserDataExtractorImpl::create)
                .toList();
    }

}
