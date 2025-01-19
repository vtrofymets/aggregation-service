package org.vt.aggregation.v2;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;

import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@Configuration
@RequiredArgsConstructor
public class ConnectionsContextConfig {

    private final ContextProperties contextProperties;

    @PostConstruct
    public void init() {
        Map<String, ContextProperties.Connection> connectionMap = contextProperties.getConnections()
                .stream()
                .collect(Collectors.toMap(ContextProperties.Connection::getName, Function.identity()));

        Map<String, ClientFactory.ClientStrategy<?>> clients = connectionMap.entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getKey, v -> ClientFactory.getClient(v.getValue())));


        Map<String, Map<ClientFactory.ClientStrategy<?>, ContextProperties.EntityDetails>> groupsMap = contextProperties.getEntities()
                .entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue()
                        .stream()
                        .map(details -> Map.entry(Objects.requireNonNull(clients.get(details.getConnectionName()), "For group: '%s', connection with name: '%s' must be present in connections!".formatted(entry.getKey(), details.getConnectionName())), details))
                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue))));


        
    }

}
