package org.vt.aggregation.config.context;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.vt.aggregation.config.database.DataSourcesSettings;

import javax.sql.DataSource;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Builder
public record DataSourceContext(String tenant,
                                String database,
                                DataSource dataSource,
                                String table,
                                Map<String, String> mapping,
                                Map<String, Map<String, String>> tableMapping,
                                Migrate migrate,
                                HealthCheck healthCheck) {

    public static DataSourceContext from(DataSourcesSettings.DataSourceProperties dataSourceProperties) {
        return DataSourceContext.builder()
                .tenant(dataSourceProperties.name())
                .database(dataSourceProperties.strategy())
                .table(dataSourceProperties.table())
                .dataSource(buildDataSource(dataSourceProperties))
                .mapping(Collections.unmodifiableMap(dataSourceProperties.mapping()))
                .migrate(Migrate.from(dataSourceProperties.migration()))
                .healthCheck(HealthCheck.from(dataSourceProperties.health()))
                .build();
    }

    private static DataSource buildDataSource(DataSourcesSettings.DataSourceProperties dataSourceProperties) {
        return DataSourceBuilder.create()
                .url(dataSourceProperties.url())
                .username(dataSourceProperties.user())
                .password(dataSourceProperties.password())
                .build();
    }

    @RequiredArgsConstructor
    @Builder
    @Getter
    public static class Migrate {

        private final boolean enabled;

        public static Migrate from(DataSourcesSettings.MigrationProperties migrationProperties) {
            return Optional.ofNullable(migrationProperties)
                    .map(p -> Migrate.builder()
                            .enabled(p.enabled())
                            .build())
                    .orElse(null);
        }
    }

    @RequiredArgsConstructor
    @Builder
    @Getter
    public static class HealthCheck {

        private final String testQuery;

        public static HealthCheck from(DataSourcesSettings.HealthProperties healthProperties) {
            return Optional.ofNullable(healthProperties)
                    .map(p -> HealthCheck.builder()
                            .testQuery(p.testQuery())
                            .build())
                    .orElse(null);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DataSourceContext that = (DataSourceContext) o;
        return Objects.equals(tenant, that.tenant);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(tenant);
    }
}
