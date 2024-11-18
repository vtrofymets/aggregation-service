package org.vt.aggregation.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.vt.aggregation.config.database.DataSourceConfiguration;
import org.vt.aggregation.domain.User;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UsersAggregationServiceImpl implements UsersAggregationService {

    private final List<DataSourceConfiguration.DataExtractor<User>> userDataExtractors;

    @Override
    public List<User> findUsers(UsersFilterParams params) {
        var completableFutures = userDataExtractors.stream()
                .map(extractor -> CompletableFuture.supplyAsync(extractor::findAll))
                .toList();

        CompletableFuture.allOf(completableFutures.toArray(new CompletableFuture[0]))
                .join();

        return completableFutures.stream()
                .map(CompletableFuture::join)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

}
