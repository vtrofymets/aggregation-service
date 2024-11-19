package org.vt.aggregation.domain;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class User {

    String id;
    String username;
    String name;
    String surname;

}
