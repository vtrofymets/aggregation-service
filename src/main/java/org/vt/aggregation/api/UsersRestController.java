package org.vt.aggregation.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.vt.aggregation.api.dto.PaginationResponseDto;
import org.vt.aggregation.api.dto.ReceiveUsers2ParamsParameter;
import org.vt.aggregation.api.dto.UserResponseDto;
import org.vt.aggregation.mappers.UsersMapper;
import org.vt.aggregation.service.UsersAggregationService;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Slf4j
public class UsersRestController implements UsersApi {

    private final UsersAggregationService usersAggregationService;
    private final UsersMapper usersMapper;

    @ResponseStatus(HttpStatus.OK)
    @Override
    public List<UserResponseDto> receiveUsers(String id, String username, String name, String surname) {
        var users = usersAggregationService.findUsers(
                UsersAggregationService.UsersFilterParams.of(id, username, name, surname));
        log.info("users = {}", users);
        return usersMapper.map(users);
    }

    @Override
    public PaginationResponseDto receiveUsers2(ReceiveUsers2ParamsParameter params, Map<String, String> mapping,
            Map<String, List<String>> mappings) {
        return null;
    }

}
