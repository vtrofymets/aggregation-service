package org.vt.aggregation.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.vt.aggregation.domain.User;
import org.vt.aggregation.providers.ParametersConverterProvider;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UsersAggregationServiceImpl implements UsersAggregationService {

    private final List<DataExtractor<User>> userDataExtractors;
    private final ParametersConverterProvider parametersConverterProvider;

    @Override
    public List<User> findUsers(UsersFilterParams params) {
        return Optional.ofNullable(params)
                .map(this::findUsersWithParams)
                .orElseGet(this::findUsers);
    }

    private List<User> findUsers() {
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

    private List<User> findUsersWithParams(UsersFilterParams params) {
        var parameters = parametersConverterProvider.convert(params);

        List<User> userList = userDataExtractors.stream()
                .map(extractor -> extractor.findWithParams(parameters))
                .flatMap(Collection::stream)
                .toList();

        log.info("userList = {}", userList);

        return userList;
    }

}
