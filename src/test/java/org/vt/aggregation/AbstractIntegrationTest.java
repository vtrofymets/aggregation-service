package org.vt.aggregation;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.MapPropertySource;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.lifecycle.Startables;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
//@ContextConfiguration(initializers = {AbstractIntegrationTest.PostgresInitializer.class})
@RequiredArgsConstructor
public abstract class AbstractIntegrationTest {

    @Autowired
    protected ObjectMapper objectMapper;

    public static class PostgresInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

        public static final String IMAGE_NAME = "postgres:16";

        private final List<PostgreSQLContainer<?>> POSTGRES_CONTAINERS = new ArrayList<>();


        @Override
        public void initialize(ConfigurableApplicationContext context) {

            var env = context.getEnvironment();

            for (int i = 0; env.containsProperty("app.data-sources[%d].name".formatted(i)); i++) {
                POSTGRES_CONTAINERS.add(new PostgreSQLContainer<>(IMAGE_NAME));
            }

            var dependencies = new MapPropertySource("dependencies", buildDependencies());

            env.getPropertySources()
                    .addFirst(dependencies);

        }

        private Map<String, Object> buildDependencies() {

            Startables.deepStart(POSTGRES_CONTAINERS).join();

            var dependencies = new LinkedHashMap<String, Object>();

            for (int i = 0; i < POSTGRES_CONTAINERS.size(); i++) {
                dependencies.put("app.data-sources[%d].url".formatted(i), POSTGRES_CONTAINERS.get(i).getJdbcUrl());
                dependencies.put("app.data-sources[%d].user".formatted(i),POSTGRES_CONTAINERS.get(i).getUsername());
                dependencies.put("app.data-sources[%d].password".formatted(i),POSTGRES_CONTAINERS.get(i).getPassword());
            }

            return dependencies;
        }
    }

}
