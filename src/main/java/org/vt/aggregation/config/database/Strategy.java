package org.vt.aggregation.config.database;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;
import org.vt.aggregation.v2.data.clients.ElasticSearchClient;
import org.vt.aggregation.v2.data.clients.MongoDbClient;
import org.vt.aggregation.v2.data.clients.PostgresClient;

@Getter
public enum Strategy {

    POSTGRES("postgres", PostgresClient.class),
    MYSQL("mysql"),
    REDIS("redis"),
    MONGO("mongodb", MongoDbClient.class),
    ELASTICSEARCH("elasticsearch", ElasticSearchClient.class);

    private final String value;
    private final Class<?> clientClass;

    Strategy(String value) {
        this(value, null);
    }

    Strategy(String value, Class<?> clientClass) {
        this.value = value;
        this.clientClass = clientClass;
    }

    @JsonCreator
    public static Strategy fromValue(String v) {
        for (Strategy strategy : Strategy.values()) {
            if (strategy.value.equals(v) || strategy.name()
                    .equalsIgnoreCase(v)) {
                return strategy;
            }
        }
        throw new IllegalArgumentException("Unsupported strategy: " + v);
    }
}
