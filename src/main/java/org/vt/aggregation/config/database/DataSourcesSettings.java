package org.vt.aggregation.config.database;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Map;

@Validated
@ConfigurationProperties("app")
public record DataSourcesSettings(List<DataSourceProperties> dataSources) {

    public record DataSourceProperties(@NotNull @NotBlank String name,
                                       @NotNull @NotBlank String strategy,
                                       @NotNull @NotBlank String url,
                                       @NotNull @NotBlank String user,
                                       @NotNull @NotBlank String password,
                                       @NotNull @NotBlank String table,
                                       @NotNull Map<String, String> mapping,
                                       HealthProperties health,
                                       MigrationProperties migration) {
    }

    //    public record MappingProperties(String id, String username, String name, String surname) {
    //    }

    public record MigrationProperties(boolean enabled) {
    }

    public record HealthProperties(@NotNull @NotBlank String testQuery) {
    }

}
