package org.vt.aggregation.config.database;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Strategy {

    POSTGRES("postgres"),
    MYSQL("mysql"),
    REDIS("redis"),
    MONGO_DB("mongodb"),
    ELASTICSEARCH("elasticsearch");

    private final String value;

    @JsonCreator
    public static Strategy fromValue(String v) {
        for (Strategy strategy : Strategy.values()) {
            if (strategy.value.equals(v) || strategy.name().equalsIgnoreCase(v)) {
                return strategy;
            }
        }
        throw new IllegalArgumentException("Invalid strategy: " + v);
    }
}
