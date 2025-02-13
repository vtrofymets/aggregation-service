package org.vt.aggregation.v2.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.vt.aggregation.v2.service.data.handler.DataHandler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AggregationDataServiceImpl implements AggregationDataService {

    private final Map<String, List<DataHandler>> dataHandlerMap;

    public AggregationDataServiceImpl(List<DataHandler> dataHandlerMap) {
        this.dataHandlerMap = dataHandlerMap.stream()
                .collect(Collectors.groupingBy(x -> x.metadata()
                        .domain()));
    }

    @Override
    public List<Map<String, String>> findAll(String domain) {
        log.info("findAll for domain: {}", domain);
        List<DataHandler> dataHandlers = dataHandlerMap.getOrDefault(domain, Collections.emptyList());
        log.info("Fo domain: {} get dataHandlers: {}", domain, dataHandlers.size());
        List<CompletableFuture<List<Map<String, String>>>> futures = dataHandlers.stream()
                .map(dh -> CompletableFuture.supplyAsync(dh::findAll).handle(AggregationDataServiceImpl::handleException))
                .toList();
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
        return futures.stream()
                .map(CompletableFuture::join)
                .filter(Objects::nonNull)
                .reduce(new ArrayList<>(), (l1, l2) -> {
                    l1.addAll(l2);
                    return l1;
                });
    }

    private static List<Map<String, String>> handleException(List<Map<String, String>> data, Throwable ex) {
        if (ex != null) {
            log.warn("While trying receive data, get error: [{}]", ex.getMessage(), ex);
            return null;
        }
        return data;
    }
}
