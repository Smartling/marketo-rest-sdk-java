package com.smartling.marketo.sdk.domain.landingpage;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum VariableType {
    STRING("string"),
    BOOLEAN("boolean"),
    COLOR("color");

    private String key;

    VariableType(String key) {
        this.key = key;
    }

    @JsonCreator
    public static VariableType fromString(String key) {
        return key == null
                ? null
                : VariableType.valueOf(key.toUpperCase());
    }

    @JsonValue
    public String getKey() {
        return key;
    }
}
