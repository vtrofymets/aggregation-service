package org.vt.aggregation.v2.data.clients;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.client.MongoClients;
import org.springframework.boot.actuate.data.mongo.MongoHealthIndicator;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.vt.aggregation.v2.ContextProperties;

import java.util.Objects;

public class MongoDbClient extends AbstractClientStrategy<MongoTemplate> {

    private final MongoTemplate mongoTemplate;
    private final HealthIndicator healthIndicator;

    public MongoDbClient(ContextProperties.Connection connection) {
        super(connection);

        var username = Objects.requireNonNull(connection.getUsername(), "Mongo username is required!");
        var password = Objects.requireNonNull(connection.getPassword(), "Mongo password is required!");

        var credential = MongoCredential.createCredential(username, connection.getName(), password.toCharArray());

        var connectionString = new ConnectionString(connection.getUrl());
        var mongoClientSettings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .credential(credential)
                .build();

        var mongoClient = MongoClients.create(mongoClientSettings);

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
