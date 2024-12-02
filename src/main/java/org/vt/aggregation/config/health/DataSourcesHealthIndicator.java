package org.vt.aggregation.config.health;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.actuate.health.Status;
import org.springframework.stereotype.Component;
import org.vt.aggregation.config.context.AggregationContext;
import org.vt.aggregation.providers.JdbcTemplateProvider;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Slf4j
@RequiredArgsConstructor
public class DataSourcesHealthIndicator implements HealthIndicator {

    private final List<JdbcTemplateProvider> jdbcTemplateProviders;
    private final AggregationContext aggregationContext;

    @Override
    public Health health() {
        var details = jdbcTemplateProviders.stream()
                .filter(jdbcTemplateProvider -> aggregationContext.getDataSourceContextByTenant(
                                jdbcTemplateProvider.tenant())
                        .healthCheck() != null)
                .map(jdbcTemplateProvider -> Map.entry(jdbcTemplateProvider.tenant(), buildDetails(jdbcTemplateProvider)))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        return Health.up()
                .withDetails(details)
                .build();
    }

    private TenantDetails buildDetails(JdbcTemplateProvider jdbcTemplateProvider) {
        try {
            var testQuery = aggregationContext.getDataSourceContextByTenant(jdbcTemplateProvider.tenant())
                    .healthCheck()
                    .getTestQuery();
            jdbcTemplateProvider.jdbcTemplate()
                    .execute(testQuery);
            return new TenantDetails(jdbcTemplateProvider.tenant(), Status.UP, null);
        } catch (Exception e) {
            log.warn("Get health error for tenant {}", jdbcTemplateProvider.tenant(), e);
            return new TenantDetails(jdbcTemplateProvider.tenant(), Status.DOWN, e.getMessage());
        }
    }

    private record TenantDetails(String tenant, Status status, String errorMessage) {
    }
}
