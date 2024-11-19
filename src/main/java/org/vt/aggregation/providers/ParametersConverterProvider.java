package org.vt.aggregation.providers;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Slf4j
@RequiredArgsConstructor
public class ParametersConverterProvider {

    private final ObjectMapper objectMapper;

    @SuppressWarnings("unchecked")
    public <T> Map<String, String> convert(T t) {
        log.info("try to convert = {}", t);
        return objectMapper.convertValue(t, Map.class);
    }
}
