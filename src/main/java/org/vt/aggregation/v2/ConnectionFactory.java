package org.vt.aggregation.v2;

import org.elasticsearch.client.RestClient;
import org.springframework.boot.actuate.data.mongo.MongoHealthIndicator;
import org.springframework.boot.actuate.elasticsearch.ElasticsearchRestClientHealthIndicator;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.actuate.jdbc.DataSourceHealthIndicator;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.vt.aggregation.config.database.Strategy;

import javax.sql.DataSource;

public class ConnectionFactory {

    public interface ConnectionCreator<T> { ;

        Strategy strategy();

        T create(ContextProperties.Connection connection);

        HealthIndicator healthIndicator();

    }

    public static class PostgresConnectionCreator implements ConnectionCreator<DataSource> {

        @Override
        public Strategy strategy() {
            return Strategy.POSTGRES;
        }

        @Override
        public DataSource create(ContextProperties.Connection connection) {
            return null;
        }


        @Override
        public HealthIndicator healthIndicator() {
            return new DataSourceHealthIndicator();
        }
    }

    public static class ElasticSearchConnectionCreator implements ConnectionCreator<RestClient> {

        @Override
        public Strategy strategy() {
            return Strategy.ELASTICSEARCH;
        }

        @Override
        public RestClient create(ContextProperties.Connection connection) {
            return null;
        }


        @Override
        public HealthIndicator healthIndicator() {
            return new ElasticsearchRestClientHealthIndicator(null);
        }
    }

    public static class MongoDbConnectionCreator implements ConnectionCreator<MongoTemplate> {

        @Override
        public Strategy strategy() {
            return Strategy.MONGO_DB;
        }

        @Override
        public MongoTemplate create(ContextProperties.Connection connection) {
            return null;
        }


        @Override
        public HealthIndicator healthIndicator() {
            return new MongoHealthIndicator(null);
        }
    }
}
