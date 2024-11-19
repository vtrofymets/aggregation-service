package org.vt.aggregation.service;

import org.apache.commons.lang3.ObjectUtils;
import org.vt.aggregation.domain.User;

import java.util.List;

public interface UsersAggregationService {

    List<User> findUsers(UsersFilterParams params);

    record UsersFilterParams(String id, String username, String name, String surname) {
        public static UsersFilterParams of(String id, String username, String name, String surname) {
            if (ObjectUtils.allNull(id, username, name, surname)) {
                return null;
            }
            return new UsersFilterParams(id, username, name, surname);
        }
    }

}
