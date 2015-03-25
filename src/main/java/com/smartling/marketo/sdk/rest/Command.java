package com.smartling.marketo.sdk.rest;

import java.lang.reflect.Type;
import java.util.Map;

public interface Command<T> {
    String getPath();

    String getMethod();

    Type getResultType();

    Map<String, Object> getParameters();
}
