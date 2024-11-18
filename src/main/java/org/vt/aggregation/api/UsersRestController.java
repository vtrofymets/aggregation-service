package org.vt.aggregation.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;
import org.vt.aggregation.api.dto.UserResponseDto;
import org.vt.aggregation.mappers.UsersMapper;
import org.vt.aggregation.service.UsersAggregationService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class UsersRestController implements UsersApi {

    private final UsersAggregationService usersAggregationService;
    private final UsersMapper usersMapper;

    @Override
    public List<UserResponseDto> receiveUsers(String id, String username, String name, String surname) {
        var users = usersAggregationService.findUsers(new UsersAggregationService.UsersFilterParams(id, username, name, surname));
        log.info("users = {}", users);
        return usersMapper.map(users);
    }
}
