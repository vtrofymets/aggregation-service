package org.vt.aggregation.v2.service.data.handler;

import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.vt.aggregation.v2.config.properties.ContextProperties;
import org.vt.aggregation.v2.service.client.ClientStrategy;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
public class DataSourceDataHandler extends AbstractDataHandler {

    protected final JdbcTemplate jdbcTemplate;
    protected final RowMapper<Map<String, String>> rowMapper;
    protected final String selectAll;

    public DataSourceDataHandler(@NonNull String domain, @NonNull ClientStrategy<JdbcTemplate> clientStrategy,
            @NonNull ContextProperties.EntityDefinitions entityDefinitions) {
        super(domain, entityDefinitions);
        this.jdbcTemplate = clientStrategy.client();
        this.rowMapper = buildRowMapper();
        this.selectAll = buildSelect();
    }

    @Override
    public List<Map<String, String>> findAll() {
        log.info("Extract data for: {}", metadata());
        return jdbcTemplate.query(selectAll, this.rowMapper);
    }

    protected RowMapper<Map<String, String>> buildRowMapper() {
        return (rs, i) -> this.mapping.entrySet()
                .stream()
                .map(entry -> Map.entry(entry.getKey(), getString(rs, entry.getValue())))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private String buildSelect() {
        var table = metadata().entity().toUpperCase();
        return this.mapping.values()
                .stream()
                .distinct()
                .collect(Collectors.joining(", ", "SELECT ", " FROM " + table));
    }

    @SneakyThrows
    private static String getString(ResultSet rs, String field) {
        return rs.getString(field);
    }
}
