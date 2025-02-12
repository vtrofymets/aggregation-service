package org.vt.aggregation.v2.api;

import com.fasterxml.jackson.core.type.TypeReference;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.vt.aggregation.api.dto.ReceiveUsers2ParamsParameter;
import org.vt.aggregation.api.dto.UserResponseDto;
import org.vt.aggregation.providers.ObjectMapperProvider;
import org.vt.aggregation.v2.service.AggregationDataService;
import org.vt.aggregation.v2.service.client.ClientStrategy;
import org.vt.aggregation.v2.service.client.MongoDbClient;
import org.vt.aggregation.v2.service.client.PostgresClient;
import org.vt.aggregation.v2.service.data.handler.DataHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@Slf4j
@Hidden
public class DebugRestController {

    private final PostgresClient db1PostgresClient;
    private final PostgresClient db2PostgresClient;
    private final List<PostgresClient> clients;
    private final List<ClientStrategy<?>> clients2;
    private final List<DataHandler> dataHandlers;
    private final ObjectMapperProvider objectMapperProvider;
    private final AggregationDataService aggregationDataService;
    private final MongoDbClient db4MongoDbClient;

    @GetMapping("/debug/{group}")
    public Object debugGet(@PathVariable String group) {
        List<Map<String, String>> all = aggregationDataService.findAll(group);
        return objectMapperProvider.<UserResponseDto>convertList2(all, new TypeReference<>() {
        });
    }

    @GetMapping("/debug/create/{group}")
    public Object create(@PathVariable String group) {
        Map<String, String> object = Map.of("id", UUID.randomUUID()
                .toString(), "name", "name_" + UUID.randomUUID()
                .toString()
                .substring(0, 6), "username", "username_" + UUID.randomUUID()
                .toString()
                .substring(0, 6));
        Map<String, String> insert = db4MongoDbClient.client()
                .insert(new HashMap<>(object), group);
        log.info("Insert into db4 mongo object: {}", insert);
        return insert;
    }

    @GetMapping("/debug/get/{group}")
    public Object get(@PathVariable String group) {
        MongoTemplate client = db4MongoDbClient.client();
        var all = client.findAll(Map.class, group);
        log.info("Get object: {}", all);
        return all;
    }

    @GetMapping("/debug2/{group}")
    public Object debugGroup2(@PathVariable String group, ReceiveUsers2ParamsParameter params) {
        log.info("debug");
        List<Map<String, Object>> maps1 = db1PostgresClient.client()
                .queryForList("select * from users");
        List<Map<String, Object>> maps2 = db2PostgresClient.client()
                .queryForList("select * from user_table");
        maps1.addAll(maps2);

        var collect = dataHandlers.stream()
                .collect(Collectors.groupingBy(x -> x.metadata()
                        .domain()));
        List<Map<String, String>> list = collect.get(group)
                .stream()
                .map(DataHandler::findAll)
                .flatMap(List::stream)
                .toList();

        List<UserResponseDto> userResponseDtos = objectMapperProvider.convertList2(list, new TypeReference<>() {
        });
        return userResponseDtos;
    }

}
