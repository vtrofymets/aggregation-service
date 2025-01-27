package org.vt.aggregation.v2.api;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.vt.aggregation.api.dto.UserResponseDto;
import org.vt.aggregation.v2.data.clients.ClientStrategy;
import org.vt.aggregation.v2.data.clients.PostgresClient;
import org.vt.aggregation.v2.data.handlers.DataHandler;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@Slf4j
public class DebugRestController {

    private final PostgresClient db1PostgresClient;
    private final PostgresClient db2PostgresClient;
    private final List<PostgresClient> clients;
    private final List<ClientStrategy<?>> clients2;
    private final List<DataHandler> dataHandlers;
    private final ObjectMapper objectMapper;

    @GetMapping("/debug/{group}")
    public Object debugGroup(@PathVariable String group) {
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

        List<UserResponseDto> userResponseDtos = objectMapper.convertValue(list, new TypeReference<>() {
        });
        System.out.println(userResponseDtos);
        return userResponseDtos;
    }

}
