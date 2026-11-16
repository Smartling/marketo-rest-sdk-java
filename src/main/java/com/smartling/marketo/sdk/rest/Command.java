package com.smartling.marketo.sdk.rest;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.Map;

public interface Command<T> {
    String getPath();

    String getMethod();

    Type getResultType();

    Map<String, Object> getParameters();

    default Map<String, String> getHeaders() {
        return Collections.emptyMap();
    }

    /**
     * Body of a {@code JSON_POST} command, serialized as JSON; {@code null} for every other command.
     */
    default Object getBody() {
        return null;
    }
}
