package org.vt.aggregation.utils;

import lombok.experimental.UtilityClass;
import org.vt.aggregation.domain.User;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@UtilityClass
public class DataUtils {

    public static List<User> createUsers(long limit) {
        return Stream.generate(DataUtils::createDefaultUser)
                .limit(limit)
                .collect(Collectors.toList());
    }

    public static User createDefaultUser() {
        return User.builder()
                .id(UUID.randomUUID().toString())
                .username(UUID.randomUUID().toString())
                .name(UUID.randomUUID().toString())
                .surname(UUID.randomUUID().toString())
                .build();
    }
}
