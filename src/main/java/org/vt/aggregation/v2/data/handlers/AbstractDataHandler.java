package org.vt.aggregation.v2.data.handlers;

import org.vt.aggregation.v2.ContextProperties;

import java.util.Map;

public abstract class AbstractDataHandler implements DataHandler {

    private final Metadata metadata;
    protected final Map<String, String> mapping;

    public AbstractDataHandler(String domain, ContextProperties.EntityDefinitions entityDefinitions) {
        this.metadata = new Metadata(domain, entityDefinitions.getConnectionName(), entityDefinitions.getEntity());
        this.mapping = entityDefinitions.getMapping();
    }

    @Override
    public Metadata metadata() {
        return this.metadata;
    }

}
