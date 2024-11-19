package org.vt.aggregation.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.vt.aggregation.api.dto.UserResponseDto;
import org.vt.aggregation.domain.User;
import org.vt.aggregation.mappers.UsersMapperImpl;
import org.vt.aggregation.service.UsersAggregationService;
import org.vt.aggregation.utils.DataUtils;

import java.util.Arrays;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UsersRestController.class)
@Import(UsersMapperImpl.class)
class UsersRestControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UsersAggregationService usersAggregationService;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getUsers_expectedUserResponseDto_withSize10AndContainsExpectedValues() throws Exception {
        var expected = DataUtils.createUsers(10);

        Mockito.when(usersAggregationService.findUsers(Mockito.any()))
                .thenReturn(expected);

        var contentAsString = mockMvc.perform(MockMvcRequestBuilders.get("/users"))
                .andExpect(status().is2xxSuccessful())
                .andReturn()
                .getResponse()
                .getContentAsString();

        var actual = Arrays.asList(objectMapper.readValue(contentAsString, UserResponseDto[].class));

        Assertions.assertThat(actual)
                .isNotEmpty()
                .hasSize(10)
                .extracting(UserResponseDto::getId)
                .doesNotContainNull()
                .containsAll(expected.stream()
                        .map(User::getId)
                        .toList());
    }
}