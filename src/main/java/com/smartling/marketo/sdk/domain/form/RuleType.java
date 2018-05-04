package com.smartling.marketo.sdk.domain.form;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum RuleType
{
    ALWAYSSHOW("alwaysShow"),
    SHOW("show"),
    HIDE("HIDE");

    private String key;

    RuleType(String key) {
        this.key = key;
    }

    @JsonCreator
    public static RuleType fromString(String key) {
        return key == null
                ? null
                : RuleType.valueOf(key.toUpperCase());
    }

    @JsonValue
    public String getKey() {
        return key;
    }
}
