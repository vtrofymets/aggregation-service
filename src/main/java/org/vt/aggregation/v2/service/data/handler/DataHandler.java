package org.vt.aggregation.v2.service.data.handler;

import lombok.NonNull;

import java.util.List;
import java.util.Map;

public interface DataHandler {

    Metadata metadata();

    List<Map<String, String>> findAll();

    record Metadata(@NonNull String domain, @NonNull String connectionName, @NonNull String entity) {
    }

}