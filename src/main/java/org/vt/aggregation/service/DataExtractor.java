package org.vt.aggregation.service;

import lombok.NonNull;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.vt.aggregation.domain.User;
import org.vt.aggregation.providers.JdbcTemplateProvider;

import java.util.List;
import java.util.Map;

public interface DataExtractor<T> extends JdbcTemplateProvider {

    String table();

    List<User> findWithParams(@NonNull Map<String, String> parameters);

    List<T> findAll();

    NamedParameterJdbcTemplate namedParameterJdbcTemplate();

    RowMapper<T> rowMapper();

}
