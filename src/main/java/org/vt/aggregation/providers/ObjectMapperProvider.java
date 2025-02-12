package org.vt.aggregation.providers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@Slf4j
@RequiredArgsConstructor
public class ObjectMapperProvider {

    private final ObjectMapper objectMapper;

    @SuppressWarnings("unchecked")
    public <T> Map<String, String> convertToMap(T t) {
        log.debug("try to convert: {}", t);
        return objectMapper.convertValue(t, Map.class);
    }

    public <T> T convertToMap(Map<String, Object> values, Class<T> clazz) {
        log.debug("try to convert values: {}", values);
        return objectMapper.convertValue(values, clazz);
    }

    public <T> List<T> convertList(List<Map<String, Object>> values, TypeReference<List<T>> reference) {
        log.debug("try to convertList values: {}", values);
        return objectMapper.convertValue(values, reference);
    }

    public <T> List<T> convertList2(List<Map<String, String>> values, TypeReference<List<T>> reference) {
        log.debug("try to convertList2 values: {}", values);
        return objectMapper.convertValue(values, reference);
    }

    public <T> List<T> convertArray(List<Map<String, Object>> values, Class<T[]> lClass) {
        log.debug("try to convertArray values: {}", values);
        return List.of(objectMapper.convertValue(values, lClass));
    }

    @SneakyThrows
    public <T> T map(String json, Class<T> clazz) {
        log.debug("try to convert json: {}", json);
        return objectMapper.readValue(json, clazz);
    }
}
