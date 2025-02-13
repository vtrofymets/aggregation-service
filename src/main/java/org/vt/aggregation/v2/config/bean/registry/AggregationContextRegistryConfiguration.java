package org.vt.aggregation.v2.config.bean.registry;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.ConfigurableEnvironment;

@Configuration
public class AggregationContextRegistryConfiguration {

    @Bean
    public static AggregationContextBeanDefinitionRegistry clientsBeanDefinitionConfiguration(ConfigurableEnvironment environment) {
        return new AggregationContextBeanDefinitionRegistry(environment);
    }

}
