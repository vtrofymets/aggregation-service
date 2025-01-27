package org.vt.aggregation.v2.health;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;
import org.vt.aggregation.v2.data.clients.ClientStrategy;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Slf4j
@RequiredArgsConstructor
public class ClientsHealthIndicator implements HealthIndicator {

    private final List<ClientStrategy<?>> clients;

    @Override
    public Health health() {
        var healthMap = clients.stream()
                .map(client -> Map.entry(client.name(), client.healthIndicator().health()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        return Health.up()
                .withDetails(healthMap)
                .build();
    }

}
