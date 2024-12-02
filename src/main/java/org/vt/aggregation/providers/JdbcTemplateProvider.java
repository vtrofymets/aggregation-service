package org.vt.aggregation.providers;

import org.springframework.jdbc.core.JdbcTemplate;

public interface JdbcTemplateProvider {

    String tenant();

    JdbcTemplate jdbcTemplate();

}
