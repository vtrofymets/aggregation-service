package org.vt.aggregation.v2;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.client.MongoClients;
import org.elasticsearch.client.RestClient;
import org.springframework.boot.actuate.data.mongo.MongoHealthIndicator;
import org.springframework.boot.actuate.elasticsearch.ElasticsearchRestClientHealthIndicator;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.actuate.jdbc.DataSourceHealthIndicator;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.vt.aggregation.config.database.Strategy;
import org.vt.aggregation.utils.JdbcTemplateUtils;

import java.util.Objects;

public final class ClientFactory {
    //HttpClient builder
    public static ClientStrategy<?> getClient(ContextProperties.Connection connection) {
        return switch (connection.getStrategy()) {
            case POSTGRES -> new PostgresClient(connection);
            case MONGO_DB -> new MongoDbClient(connection);
            default -> throw new IllegalArgumentException("Unsupported strategy: " + connection.getStrategy());
        };
    }

    public interface ClientStrategy<T> {

        String name();

        Strategy strategy();

        T client();

        HealthIndicator healthIndicator();

    }

    public static class PostgresClient implements ClientStrategy<JdbcTemplate> {

        private final JdbcTemplate JdbcTemplate;
        private final String name;
        private final HealthIndicator healthIndicator;

        PostgresClient(ContextProperties.Connection connection) {
            this.JdbcTemplate = JdbcTemplateUtils.buildJdbcTemplate(connection);
            this.name = connection.getName();
            this.healthIndicator = new DataSourceHealthIndicator(client().getDataSource());
        }

        @Override
        public String name() {
            return this.name;
        }

        @Override
        public Strategy strategy() {
            return Strategy.POSTGRES;
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

    public static class ElasticSearchClient implements ClientStrategy<RestClient> {

        @Override
        public String name() {
            return "";
        }

        @Override
        public Strategy strategy() {
            return Strategy.ELASTICSEARCH;
        }

        @Override
        public RestClient client() {
            return null;
        }


        @Override
        public HealthIndicator healthIndicator() {
            return new ElasticsearchRestClientHealthIndicator(client());
        }
    }

    public static class MongoDbClient implements ClientStrategy<MongoTemplate> {

        private final MongoTemplate mongoTemplate;

        MongoDbClient(ContextProperties.Connection connection) {
            var username = Objects.requireNonNull(connection.getUsername(), "Mongo username is required!");
            var password = Objects.requireNonNull(connection.getPassword(), "Mongo password is required!");

            var credential = MongoCredential.createCredential(username, connection.getName(), password.toCharArray());

            var connectionString = new ConnectionString(connection.getUrl());
            var mongoClientSettings = MongoClientSettings.builder()
                    .applyConnectionString(connectionString)
                    .credential(credential)
                    .build();

            var mongoClient = MongoClients.create(mongoClientSettings);

            this.mongoTemplate = new MongoTemplate(mongoClient, connection.getName());
        }

        @Override
        public String name() {
            return "";
        }

        @Override
        public Strategy strategy() {
            return Strategy.MONGO_DB;
        }

        @Override
        public MongoTemplate client() {
            return this.mongoTemplate;
        }

        @Override
        public HealthIndicator healthIndicator() {
            return new MongoHealthIndicator(client());
        }
    }
}
