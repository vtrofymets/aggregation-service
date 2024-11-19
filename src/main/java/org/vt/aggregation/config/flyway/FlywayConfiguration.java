package org.vt.aggregation.config.flyway;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.MigrationVersion;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Configuration;
import org.vt.aggregation.config.database.DataSourcesSettings;

import java.util.Optional;

@Configuration
@RequiredArgsConstructor
public class FlywayConfiguration {

    private final DataSourcesSettings dataSourcesSettings;

    @PostConstruct
    public void init() {
        dataSourcesSettings.dataSources()
                .stream()
                .filter(dataSourceProperties -> Optional.ofNullable(dataSourceProperties.migration())
                        .filter(DataSourcesSettings.MigrationProperties::enabled)
                        .isPresent())
                .map(dataSourceProperties -> Flyway.configure()
                        .dataSource(DataSourceBuilder.create()
                                .url(dataSourceProperties.url())
                                .username(dataSourceProperties.user())
                                .password(dataSourceProperties.password())
                                .build())
                        .locations("classpath:db/migration/postgres/" + dataSourceProperties.name())
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
