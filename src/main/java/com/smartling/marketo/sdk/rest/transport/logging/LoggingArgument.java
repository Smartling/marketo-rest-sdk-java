package com.smartling.marketo.sdk.rest.transport.logging;

public class LoggingArgument {
    private final String name;
    private final Object value;

    public LoggingArgument(String name, Object value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public Object getValue() {
        return value;
    }
}
