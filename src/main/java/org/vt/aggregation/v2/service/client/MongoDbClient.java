package org.vt.aggregation.v2.service.client;

import com.mongodb.client.MongoClients;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.actuate.data.mongo.MongoHealthIndicator;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.vt.aggregation.v2.config.properties.ContextProperties;

import java.util.Objects;

@Slf4j
public class MongoDbClient extends AbstractClientStrategy<MongoTemplate> {

    private final MongoTemplate mongoTemplate;
    private final HealthIndicator healthIndicator;

    public MongoDbClient(ContextProperties.Connection connection) {
        super(connection);

        var username = Objects.requireNonNull(connection.getUsername(), "Mongo username is required!");
        var password = Objects.requireNonNull(connection.getPassword(), "Mongo password is required!");


        String[] split = connection.getUrl().split("mongodb://", 2);

        var connectionUrl = "mongodb://" + username + ":" + password + "@" + split[split.length - 1] + "?authSource=admin";

                log.debug("Mongo connection url: {}", connectionUrl);

        var mongoClient = MongoClients.create(connectionUrl);

        this.mongoTemplate = new MongoTemplate(mongoClient, connection.getName());
        this.healthIndicator = new MongoHealthIndicator(mongoTemplate);
    }

    @Override
    public MongoTemplate client() {
        return this.mongoTemplate;
    }

    @Override
    public HealthIndicator healthIndicator() {
        return this.healthIndicator;
    }
}
