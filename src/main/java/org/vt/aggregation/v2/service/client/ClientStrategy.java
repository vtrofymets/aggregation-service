package org.vt.aggregation.v2.service.client;

import org.springframework.boot.actuate.health.HealthIndicator;
import org.vt.aggregation.config.database.Strategy;

public interface ClientStrategy<T> {

    String name();

    Strategy strategy();

    T client();

    HealthIndicator healthIndicator();

}
