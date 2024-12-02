package org.vt.aggregation.config.flyway;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.MigrationVersion;
import org.springframework.context.annotation.Configuration;
import org.vt.aggregation.config.context.AggregationContext;
import org.vt.aggregation.config.context.DataSourceContext;

import java.util.Optional;

@Configuration
@RequiredArgsConstructor
public class FlywayConfiguration {

    private final AggregationContext aggregationContext;

    @PostConstruct
    public void init() {
        aggregationContext.dataSourcesContexts()
                .stream()
                .filter(dataSourceProperties -> Optional.ofNullable(dataSourceProperties.migrate())
                        .filter(DataSourceContext.Migrate::isEnabled)
                        .isPresent())
                .map(dataSourceProperties -> Flyway.configure()
                        .dataSource(dataSourceProperties.dataSource())
                        .locations("classpath:db/migration/".concat(dataSourceProperties.database()).concat("/").concat(dataSourceProperties.tenant()))
                        .target(MigrationVersion.LATEST)
                        .baselineOnMigrate(true)
                        .cleanDisabled(false)
                        .load())
                .forEach(flyway -> {
                    flyway.clean();
                    flyway.migrate();
                });
    }
}
