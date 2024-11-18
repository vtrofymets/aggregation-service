package org.vt.aggregation.service;

import org.vt.aggregation.domain.User;

import java.util.List;

public interface UsersAggregationService {

    List<User> findUsers(UsersFilterParams params);

    record UsersFilterParams(String id, String username, String name, String surname) {
        public static UsersFilterParams from(String id, String username, String name, String surname) {
            return new UsersFilterParams(id, username, name, surname);
        }
    }

}
