package org.vt.aggregation.service;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.vt.aggregation.config.context.DataSourceContext;
import org.vt.aggregation.domain.User;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
public class UserDataExtractorImpl implements DataExtractor<User> {

    private final String tenant;
    private final String table;
    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final RowMapper<User> rowMapper;
    private final Map<String, String> mapping;
    private final String selectAll;

    private UserDataExtractorImpl(DataSourceContext dataSourceContext) {
        this.tenant = dataSourceContext.tenant();
        this.table = dataSourceContext.table();
        this.jdbcTemplate = new JdbcTemplate(dataSourceContext.dataSource());
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
        this.mapping = dataSourceContext.mapping();
        this.rowMapper = buildRowMapper(mapping);
        this.selectAll = new LinkedHashSet<>(mapping.values()).stream()
                .collect(Collectors.joining(", ", "SELECT ", " FROM " + table));
    }

    public static DataExtractor<User> create(@NonNull DataSourceContext dataSourceContexts) {
        return new UserDataExtractorImpl(dataSourceContexts);
    }

    @Override
    public List<User> findWithParams(@NonNull Map<String, String> parameters) {
        log.info("tenant: {}, parameters: {}", tenant, parameters);

        if (!parameters.isEmpty()) {
            var queryParameters = parameters.entrySet()
                    .stream()
                    .filter(entry -> mapping.containsKey(entry.getKey()))
                    .map(entry -> mapping.get(entry.getKey())
                            .concat(" = '")
                            .concat(entry.getValue())
                            .concat("'"))
                    .collect(Collectors.joining(" AND "));

            queryParameters = StringUtils.isNotBlank(queryParameters) ? " WHERE " + queryParameters : StringUtils.EMPTY;

            var query = selectAll + queryParameters;

            log.info("tenant: {}, query: {}", tenant, query);

            return jdbcTemplate.query(query, rowMapper());
        }

        return findAll();
    }

    @Override
    public List<User> findAll() {
        return this.jdbcTemplate.query(selectAll, rowMapper());
    }

    @Override
    public String tenant() {
        return this.tenant;
    }

    @Override
    public String table() {
        return this.table;
    }

    @Override
    public JdbcTemplate jdbcTemplate() {
        return this.jdbcTemplate;
    }

    @Override
    public NamedParameterJdbcTemplate namedParameterJdbcTemplate() {
        return this.namedParameterJdbcTemplate;
    }

    @Override
    public RowMapper<User> rowMapper() {
        return this.rowMapper;
    }

    private static RowMapper<User> buildRowMapper(@NonNull Map<String, String> mapping) {
        return (rs, i) -> User.builder()
                .id(rs.getString(mapping.get("id")))
                .username(rs.getString(mapping.get("username")))
                .name(rs.getString(mapping.get("name")))
                .surname(rs.getString(mapping.get("surname")))
                .build();
    }

}
