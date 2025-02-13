package org.vt.aggregation.v2.service;

import java.util.List;
import java.util.Map;

public interface AggregationDataService {
    List<Map<String, String>> findAll(final String domain);
}
