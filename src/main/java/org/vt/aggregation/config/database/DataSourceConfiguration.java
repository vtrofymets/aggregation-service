package org.vt.aggregation.config.database;

import com.zaxxer.hikari.HikariDataSource;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.vt.aggregation.domain.User;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Configuration
@RequiredArgsConstructor
public class DataSourceConfiguration {

    private final DataSourcesSettings dataSourcesSettings;

    @Bean
    public Map<String, JdbcTemplate> jdbcTemplateMap() {
        return dataSourcesSettings.dataSources()
                .stream()
                .map(dataSourceProperties -> Map.entry(dataSourceProperties.name(),
                        buildJdbcTemplate(dataSourceProperties)))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private static JdbcTemplate buildJdbcTemplate(DataSourcesSettings.DataSourceProperties dataSourceProperties) {
        var hikariDataSource = new HikariDataSource();
        hikariDataSource.setPoolName(dataSourceProperties.name().concat("_pool"));
        hikariDataSource.setJdbcUrl(dataSourceProperties.url());
        hikariDataSource.setUsername(dataSourceProperties.user());
        hikariDataSource.setPassword(dataSourceProperties.password());

        return new JdbcTemplate(hikariDataSource);
    }

    @Bean
    public List<DataExtractor<User>> userDataExtractors() {
        return dataSourcesSettings.dataSources()
                .stream()
                .map(DataExtractorImpl::create)
                .toList();
    }

    public interface DataExtractor<T> {

        List<T> findAll();

        String tenant();

        String table();

        JdbcTemplate repository();

        RowMapper<T> rowMapper();

    }

    @Slf4j
    public static class DataExtractorImpl implements DataExtractor<User> {

        private final String tenant;
        private final String table;
        private final JdbcTemplate jdbcTemplate;
        private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
        private final RowMapper<User> rowMapper;
        private final Set<String> columns;
        private final String selectAll;

        private DataExtractorImpl(DataSourcesSettings.DataSourceProperties dataSourceProperties) {
            this.tenant = dataSourceProperties.name();
            this.table = dataSourceProperties.table();
            this.jdbcTemplate = buildJdbcTemplate(dataSourceProperties);
            this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
            var mapping = dataSourceProperties.mapping();
            this.rowMapper = buildRowMapper(mapping);
            this.columns = Stream.of(mapping.id(), mapping.username(), mapping.name(), mapping.surname()).collect(Collectors.toSet());
            this.selectAll = columns.stream().collect(Collectors.joining(", ", "SELECT ", " FROM " + table));
        }

        @Override
        public List<User> findAll() {
            return this.jdbcTemplate.query(selectAll, this.rowMapper);
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
        public JdbcTemplate repository() {
            return this.jdbcTemplate;
        }

        @Override
        public RowMapper<User> rowMapper() {
            return this.rowMapper;
        }

        public static DataExtractor<User> create(@NonNull DataSourcesSettings.DataSourceProperties dataSourceProperties) {
            return new DataExtractorImpl(dataSourceProperties);
        }

        private JdbcTemplate buildJdbcTemplate(DataSourcesSettings.DataSourceProperties dataSourceProperties) {
            var hikariDataSource = new HikariDataSource();
            hikariDataSource.setPoolName(dataSourceProperties.name()
                    .concat("_pool"));
            hikariDataSource.setJdbcUrl(dataSourceProperties.url());
            hikariDataSource.setUsername(dataSourceProperties.user());
            hikariDataSource.setPassword(dataSourceProperties.password());

            return new JdbcTemplate(hikariDataSource);
        }

        private RowMapper<User> buildRowMapper(DataSourcesSettings.MappingProperties mapping) {
            return (rs, i) -> User.builder()
                    .id(rs.getString(mapping.id()))
                    .username(rs.getString(mapping.username()))
                    .name(rs.getString(mapping.name()))
                    .surname(rs.getString(mapping.surname()))
                    .build();
        }

    }
}
