package org.vt.aggregation.utils;

import com.zaxxer.hikari.HikariDataSource;
import lombok.experimental.UtilityClass;
import org.springframework.jdbc.core.JdbcTemplate;
import org.vt.aggregation.config.database.DataSourcesSettings;

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
}
