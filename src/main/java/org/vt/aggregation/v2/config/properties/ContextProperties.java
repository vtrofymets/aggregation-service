package org.vt.aggregation.v2.config.properties;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;
import org.vt.aggregation.config.database.DataSourcesSettings;
import org.vt.aggregation.config.database.Strategy;

import java.util.List;
import java.util.Map;
import java.util.Properties;

@Validated
@ConfigurationProperties("context")
@RequiredArgsConstructor
@Getter
public class ContextProperties {

    @NotNull
    @Valid
    private final List<Connection> connections;
    private final Map<Group, List<EntityDefinitions>> domains;

    @Validated
    public record Group(@NotNull @NotBlank @Pattern(regexp = "^[a-z][a-z0-9]*$") String group) {
    }

    @Validated
    @RequiredArgsConstructor
    @Getter
    public static class Connection {
        @NotNull
        @NotBlank
        @Pattern(regexp = "^[a-zA-Z][a-zA-Z0-9_-]*$")
        private final String name;
        @NotNull
        private final Strategy strategy;
        @NotNull
        @NotBlank
        private final String url;
        private final String username;
        private final String password;

        private final DataSourcesSettings.HealthProperties health;

        private final Properties properties;

        private final Postgres postgres;
        private final MongoDb mongoDb;
        private final ElasticSearch elasticSearch;
    }

    @Validated
    @RequiredArgsConstructor
    @Getter
    public static class EntityDefinitions {
        @NotNull
        @NotBlank
        private final String connectionName;
        @NotNull
        @NotBlank
        private final String entity;
        @NotNull
        private final Map<String, String> mapping;
        private final Map<String, Object> mapping2;
        private final DataSourcesSettings.MigrationProperties migration;
    }


    interface StrategyDefinition {
        Strategy strategy();
    }

    public static class Postgres implements StrategyDefinition {
        @Override
        public Strategy strategy() {
            return Strategy.POSTGRES;
        }
    }

    public static class MongoDb implements StrategyDefinition {
        @Override
        public Strategy strategy() {
            return Strategy.MONGO_DB;
        }
    }

    public static class ElasticSearch implements StrategyDefinition {
        @Override
        public Strategy strategy() {
            return Strategy.ELASTICSEARCH;
        }
    }

}
