package org.vt.aggregation.config.database;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.vt.aggregation.config.context.DataSourceContext;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Configuration
public class DataSourcesConfiguration {

    @Bean
    public Map<String, DataSourceContext> dataSourcesContext(DataSourcesSettings settings) {
        return settings.dataSources()
                .stream()
                .map(DataSourceContext::from)
                .collect(Collectors.toMap(DataSourceContext::tenant, Function.identity()));
    }

}
