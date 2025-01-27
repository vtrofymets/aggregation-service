package org.vt.aggregation.v2.config;

import org.vt.aggregation.v2.ContextProperties;
import org.vt.aggregation.v2.data.clients.ClientStrategy;
import org.vt.aggregation.v2.data.clients.MongoDbClient;
import org.vt.aggregation.v2.data.clients.PostgresClient;

public final class ClientsFactory {

    public static ClientStrategy<?> getClient(ContextProperties.Connection connection) {
        return switch (connection.getStrategy()) {
            case POSTGRES -> new PostgresClient(connection);
            case MONGO -> new MongoDbClient(connection);
            default -> throw new IllegalArgumentException("Unsupported strategy: " + connection.getStrategy());
        };
    }
}
