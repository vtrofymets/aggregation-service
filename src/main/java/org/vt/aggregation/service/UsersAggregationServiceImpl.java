package org.vt.aggregation.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.vt.aggregation.domain.User;
import org.vt.aggregation.providers.ParametersConverterProvider;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.concurrent.CompletableFuture.allOf;
import static java.util.concurrent.CompletableFuture.supplyAsync;

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
        return receiveUsers(extractor -> supplyAsync(extractor::findAll));
    }

    private List<User> findUsersWithParams(UsersFilterParams params) {
        var parameters = parametersConverterProvider.convert(params);
        return receiveUsers(extractor -> supplyAsync(() -> extractor.findWithParams(parameters)));
    }

    private List<User> receiveUsers(Function<DataExtractor<User>, CompletableFuture<List<User>>> function) {
        var futures = userDataExtractors.stream()
                .map(function)
                .map(future -> future.handle(UsersAggregationServiceImpl::handleException))
                .toList();

        allOf(futures.toArray(new CompletableFuture[0])).join();

        var users = futures.stream()
                .map(CompletableFuture::join)
                .filter(Objects::nonNull)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());

        log.info("users = {}", users);

        return users;
    }

    private static List<User> handleException(List<User> users, Throwable ex) {
        if (ex != null) {
            log.warn("While trying receive users get error: [{}]", ex.getMessage(), ex);
            return null;
        }
        return users;
    }

}
