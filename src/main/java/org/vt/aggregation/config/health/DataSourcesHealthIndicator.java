package org.vt.aggregation.config.health;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.actuate.health.Status;
import org.springframework.stereotype.Component;
import org.vt.aggregation.config.context.AggregationContext;
import org.vt.aggregation.providers.JdbcTemplateProvider;
import org.vt.aggregation.service.DataExtractor;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@Slf4j
@RequiredArgsConstructor
public class DataSourcesHealthIndicator implements HealthIndicator {

    private final AggregationContext aggregationContext;
    private final List<DataExtractor<?>> dataExtractors;

    @Override
    public Health health() {
        var details = dataExtractors.stream()
                .filter(jdbcTemplateProvider -> aggregationContext.getDataSourceContextByTenant(
                                jdbcTemplateProvider.tenant())
                        .healthCheck() != null)
                .map(this::buildDetails)
                .collect(Collectors.toMap(k -> k.tenant, Function.identity()));

        log.debug("DataSources health check details: {}", details);

        return Health.up()
                .withDetails(details)
                .build();
    }

    private TenantDetails buildDetails(JdbcTemplateProvider jdbcTemplateProvider) {
        var testQuery = aggregationContext.getDataSourceContextByTenant(jdbcTemplateProvider.tenant())
                .healthCheck()
                .getTestQuery();
        try {
            jdbcTemplateProvider.jdbcTemplate()
                    .execute(testQuery);
            return new TenantDetails(jdbcTemplateProvider.tenant(), testQuery, Status.UP);
        } catch (Exception e) {
            log.warn("Get health error for tenant {}", jdbcTemplateProvider.tenant(), e);
            return new TenantDetails(jdbcTemplateProvider.tenant(), testQuery, new Status(Status.DOWN.getCode(), e.getMessage()));
        }
    }

    private record TenantDetails(String tenant, String query, Status status) {
    }
}
