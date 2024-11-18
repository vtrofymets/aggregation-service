package org.vt.aggregation.config.database;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Validated
@ConfigurationProperties("app")
public record DataSourcesSettings(List<DataSourceProperties> dataSources) {

    public record DataSourceProperties(String name, @NotNull @NotBlank String url, String user, String password, String table, MappingProperties mapping, MigrationProperties migration) {
    }

    public record MappingProperties(String id, String username, String name, String surname) {
    }

    public record MigrationProperties(boolean enabled) {
    }

}
