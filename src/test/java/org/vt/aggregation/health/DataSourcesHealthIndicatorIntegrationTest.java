package org.vt.aggregation.health;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.Status;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.vt.aggregation.AbstractIntegrationTest;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
public class DataSourcesHealthIndicatorIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void checkDataSourceHealth() throws Exception {
        var contentAsString = mockMvc.perform(MockMvcRequestBuilders.get("/actuator/health/dataSources"))
                .andExpect(status().is2xxSuccessful())
                .andReturn()
                .getResponse()
                .getContentAsString();

        var actual = objectMapper.readValue(contentAsString, Health.Builder.class);

        Assertions.assertThat(actual.build())
                .isNotNull()
                .matches(x -> x.getStatus().equals(Status.UP))
                .matches(x -> x.getDetails().size() == 2);
    }
}
