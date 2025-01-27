package org.vt.aggregation.utils;

import com.zaxxer.hikari.HikariDataSource;
import lombok.experimental.UtilityClass;
import org.springframework.jdbc.core.JdbcTemplate;
import org.vt.aggregation.config.database.DataSourcesSettings;
import org.vt.aggregation.v2.ContextProperties;

import javax.sql.DataSource;

@UtilityClass
public class JdbcTemplateUtils {

    public static JdbcTemplate buildJdbcTemplate(DataSourcesSettings.DataSourceProperties dataSourceProperties) {
        var hikariDataSource = new HikariDataSource();
        hikariDataSource.setPoolName(dataSourceProperties.name() + "_pool");
        hikariDataSource.setJdbcUrl(dataSourceProperties.url());
        hikariDataSource.setUsername(dataSourceProperties.user());
        hikariDataSource.setPassword(dataSourceProperties.password());

        return new JdbcTemplate(hikariDataSource);
    }

    public static JdbcTemplate buildJdbcTemplate(ContextProperties.Connection connection) {
        var hikariDataSource = new HikariDataSource();
        hikariDataSource.setPoolName(connection.getName() + "_pool");
        hikariDataSource.setJdbcUrl(connection.getUrl());
        hikariDataSource.setUsername(connection.getUsername());
        hikariDataSource.setPassword(connection.getPassword());

        return new JdbcTemplate(hikariDataSource);
    }

    public static DataSource buildDataSource(ContextProperties.Connection connection) {
        var hikariDataSource = new HikariDataSource();
        hikariDataSource.setPoolName(connection.getName() + "_pool");
        hikariDataSource.setJdbcUrl(connection.getUrl());
        hikariDataSource.setUsername(connection.getUsername());
        hikariDataSource.setPassword(connection.getPassword());

        return hikariDataSource;
    }
}
