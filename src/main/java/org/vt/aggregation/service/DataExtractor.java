package org.vt.aggregation.service;

import lombok.NonNull;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.vt.aggregation.domain.User;

import java.util.List;
import java.util.Map;

public interface DataExtractor<T> {

    String tenant();

    String table();

    List<User> findWithParams(@NonNull Map<String, String> parameters);

    List<T> findAll();

    JdbcTemplate jdbcTemplate();

    NamedParameterJdbcTemplate namedParameterJdbcTemplate();

    RowMapper<T> rowMapper();

}
