package org.vt.aggregation.mappers;

import org.mapstruct.Mapper;
import org.vt.aggregation.api.dto.UserResponseDto;
import org.vt.aggregation.domain.User;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UsersMapper {

    List<UserResponseDto> map(List<User> users);

}
