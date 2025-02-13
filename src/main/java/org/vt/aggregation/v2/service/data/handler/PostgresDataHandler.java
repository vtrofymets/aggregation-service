package org.vt.aggregation.v2.service.data.handler;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.vt.aggregation.v2.config.properties.ContextProperties;
import org.vt.aggregation.v2.service.client.ClientStrategy;
import org.vt.aggregation.v2.service.client.PostgresClient;

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
