package org.vt.aggregation.v2.service.client;

import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.actuate.jdbc.DataSourceHealthIndicator;
import org.springframework.jdbc.core.JdbcTemplate;
import org.vt.aggregation.config.database.DataSourcesSettings;
import org.vt.aggregation.utils.JdbcTemplateUtils;
import org.vt.aggregation.v2.config.properties.ContextProperties;

import java.util.Optional;

public class PostgresClient extends AbstractClientStrategy<JdbcTemplate> {

    private final JdbcTemplate JdbcTemplate;
    private final HealthIndicator healthIndicator;

    public PostgresClient(ContextProperties.Connection connection) {
        super(connection);
        //NamedParameterJdbcTemplate ???
        this.JdbcTemplate = JdbcTemplateUtils.buildJdbcTemplate(connection);
        var query = Optional.ofNullable(connection.getHealth())
                .map(DataSourcesSettings.HealthProperties::testQuery)
                .orElse(null);
        this.healthIndicator = new DataSourceHealthIndicator(this.JdbcTemplate.getDataSource(), query);
    }

    @Override
    public JdbcTemplate client() {
        return this.JdbcTemplate;
    }

    @Override
    public HealthIndicator healthIndicator() {
        return healthIndicator;
    }

}
