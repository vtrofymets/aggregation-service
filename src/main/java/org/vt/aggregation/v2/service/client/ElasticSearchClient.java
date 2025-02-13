package org.vt.aggregation.v2.service.client;

import org.elasticsearch.client.RestClient;
import org.springframework.boot.actuate.elasticsearch.ElasticsearchRestClientHealthIndicator;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.vt.aggregation.v2.config.properties.ContextProperties;

public class ElasticSearchClient extends AbstractClientStrategy<RestClient> {

    public ElasticSearchClient(ContextProperties.Connection connection) {
        super(connection);
    }

    @Override
    public RestClient client() {
        return null;
    }


    @Override
    public HealthIndicator healthIndicator() {
        return new ElasticsearchRestClientHealthIndicator(client());
    }
}
