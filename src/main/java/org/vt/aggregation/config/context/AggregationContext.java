package org.vt.aggregation.config.context;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public final class AggregationContext {

    private final Map<String, DataSourceContext> dataSourcesContext;

    public DataSourceContext getDataSourceContextByTenant(String tenant) {
        return dataSourcesContext.get(tenant);
    }

    public DataSourceContext getDataSourceContextByTable(String table) {
        return dataSourcesContext.values()
                .stream()
                .collect(Collectors.toMap(DataSourceContext::table, Function.identity()))
                .get(table);
    }

    public List<DataSourceContext> dataSourcesContexts() {
        return List.copyOf(dataSourcesContext.values());
    }

}
