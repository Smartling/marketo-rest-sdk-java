package com.smartling.marketo.sdk.domain.token;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public class Token {
    Type type;
    String name;
    String computedUrl;
    String value;

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getComputedUrl() {
        return computedUrl;
    }

    public void setComputedUrl(String computedUrl) {
        this.computedUrl = computedUrl;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString()
    {
        return "Token{" + "type=" + type + ", name='" + name + '\'' + ", computedUrl='" + computedUrl + '\'' + ", value='" + value + '\''
                + '}';
    }

    public enum Type {
        SCRIPT_BLOCK("script block"),
        RICH_TEXT("rich text"),
        TEXT("text"),
        SCORE("score"),
        NUMBER("number"),
        DATE("date"),
        ICALENDAR("iCalendar");

        private final String name;

        Type(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }

        @JsonValue
        public String getName() {
            return this.name;
        }

        @JsonCreator
        public static Type create(String name) {
            Type[] types = Type.values();
            for (Type type : types) {
                if (type.getName().equalsIgnoreCase(name)) {
                    return type;
                }
            }
            return null;
        }
    }
}
