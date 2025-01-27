package org.vt.aggregation.v2.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.ConfigurableEnvironment;

@Configuration
public class ClientsConfiguration {

    @Bean
    public static ClientsBeanDefinitionRegistry clientsBeanDefinitionConfiguration(ConfigurableEnvironment environment) {
        return new ClientsBeanDefinitionRegistry(environment);
    }

}
