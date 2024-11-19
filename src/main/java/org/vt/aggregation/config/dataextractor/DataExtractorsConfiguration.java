package org.vt.aggregation.config.dataextractor;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.vt.aggregation.config.database.DataSourcesSettings;
import org.vt.aggregation.domain.User;
import org.vt.aggregation.service.DataExtractor;
import org.vt.aggregation.service.UserDataExtractorImpl;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class DataExtractorsConfiguration {

    private final DataSourcesSettings dataSourcesSettings;

    @Bean
    public List<DataExtractor<User>> userDataExtractors() {
        return dataSourcesSettings.dataSources()
                .stream()
                .map(UserDataExtractorImpl::create)
                .toList();
    }

}
