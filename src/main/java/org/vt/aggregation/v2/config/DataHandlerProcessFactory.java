package org.vt.aggregation.v2.config;

import org.vt.aggregation.v2.ContextProperties;
import org.vt.aggregation.v2.data.clients.ClientStrategy;
import org.vt.aggregation.v2.data.handlers.DataHandler;
import org.vt.aggregation.v2.data.handlers.PostgresDataHandler;

final class DataHandlerProcessFactory {

    public static DataHandler getDataHandlerProcess(ContextProperties.Group group, ClientStrategy<?> clientStrategy, ContextProperties.EntityDefinitions entityDefinitions) {
        return switch (clientStrategy.strategy()) {
            case POSTGRES -> new PostgresDataHandler(group.groupName(), clientStrategy, entityDefinitions);
            default -> throw new IllegalArgumentException("Unsupported strategy: " + clientStrategy.strategy());
        };
    }
}
