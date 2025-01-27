package org.vt.aggregation.v2.data.clients;

import org.vt.aggregation.config.database.Strategy;
import org.vt.aggregation.v2.ContextProperties;

public abstract class AbstractClientStrategy<T> implements ClientStrategy<T> {

    private final String name;
    private final Strategy strategy;

    public AbstractClientStrategy(ContextProperties.Connection connection) {
        this(connection.getName(), connection.getStrategy());
    }

    public AbstractClientStrategy(String name, Strategy strategy) {
        this.name = name;
        this.strategy = strategy;
    }

    @Override
    public String name() {
        return this.name;
    }

    @Override
    public Strategy strategy() {
        return this.strategy;
    }

}
