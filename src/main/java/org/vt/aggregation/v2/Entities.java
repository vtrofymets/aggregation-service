package org.vt.aggregation.v2;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Entities {
    USERS("users"),
    GOODS("goods");

    private final String path;

}
