package org.vt.aggregation.integration;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.vt.aggregation.AbstractIntegrationTest;
import org.vt.aggregation.api.dto.UserResponseDto;

import java.util.Arrays;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
public class AggregationApplicationIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void findUsersWithOutParameters_expectedResponseWithSize6() throws Exception {

        var contentAsString = mockMvc.perform(MockMvcRequestBuilders.get("/users"))
                .andExpect(status().is2xxSuccessful())
                .andReturn()
                .getResponse()
                .getContentAsString();

        var actual = List.of(objectMapper.readValue(contentAsString, UserResponseDto[].class));

        Assertions.assertThat(actual)
                .isNotEmpty()
                .hasSize(6)
                .extracting(UserResponseDto::getId)
                .doesNotContainNull();
    }

    @Test
    void getUsersWithQueryParamUsername_expectedUserResponseDto_withSize2() throws Exception {
        var contentAsString = mockMvc.perform(MockMvcRequestBuilders.get("/users?username=login1"))
                .andExpect(status().is2xxSuccessful())
                .andReturn()
                .getResponse()
                .getContentAsString();

        var actual = Arrays.asList(objectMapper.readValue(contentAsString, UserResponseDto[].class));

        Assertions.assertThat(actual)
                .isNotEmpty()
                .hasSize(2)
                .extracting(UserResponseDto::getUsername)
                .doesNotContainNull()
                .containsOnly("login1");
    }

    @Test
    void getUsersWithQueryParamName_expectedUserResponseDto_withSize1() throws Exception {
        var contentAsString = mockMvc.perform(MockMvcRequestBuilders.get("/users?name=first_name1"))
                .andExpect(status().is2xxSuccessful())
                .andReturn()
                .getResponse()
                .getContentAsString();

        var actual = Arrays.asList(objectMapper.readValue(contentAsString, UserResponseDto[].class));

        Assertions.assertThat(actual)
                .isNotEmpty()
                .hasSize(1)
                .extracting(UserResponseDto::getName)
                .doesNotContainNull()
                .containsOnly("first_name1");
    }
}
