package org.vt.aggregation.v2.config.bean.registry;

import org.vt.aggregation.v2.config.properties.ContextProperties;
import org.vt.aggregation.v2.service.client.ClientStrategy;
import org.vt.aggregation.v2.service.data.handler.DataHandler;
import org.vt.aggregation.v2.service.data.handler.PostgresDataHandler;

final class DataHandlerProcessFactory {

    public static DataHandler createDataHandler(ContextProperties.Group group, ClientStrategy<?> clientStrategy, ContextProperties.EntityDefinitions entityDefinitions) {
        return switch (clientStrategy.strategy()) {
            case POSTGRES -> new PostgresDataHandler(group.group(), clientStrategy, entityDefinitions);
            default -> throw new IllegalArgumentException("Unsupported strategy: " + clientStrategy.strategy());
        };
    }
}
