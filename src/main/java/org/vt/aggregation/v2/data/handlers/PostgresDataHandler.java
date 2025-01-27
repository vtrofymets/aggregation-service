package org.vt.aggregation.v2.data.handlers;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.vt.aggregation.v2.ContextProperties;
import org.vt.aggregation.v2.data.clients.ClientStrategy;
import org.vt.aggregation.v2.data.clients.PostgresClient;

@Slf4j
public class PostgresDataHandler extends DataSourceDataHandler {

    public PostgresDataHandler(@NonNull String group, @NonNull PostgresClient postgresClient,
            ContextProperties.@NonNull EntityDefinitions entityDefinitions) {
        super(group, postgresClient, entityDefinitions);
    }

    public PostgresDataHandler(@NonNull String group, @NonNull ClientStrategy<?> postgresClient,
            ContextProperties.@NonNull EntityDefinitions entityDefinitions) {
        super(group, (PostgresClient) postgresClient, entityDefinitions);
    }
}
