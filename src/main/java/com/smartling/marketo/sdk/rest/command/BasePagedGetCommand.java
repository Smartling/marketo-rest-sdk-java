package com.smartling.marketo.sdk.rest.command;

import java.util.HashMap;
import java.util.Map;

abstract public class BasePagedGetCommand<T> extends BaseGetCommand<T>
{
    private final Map<String, Object> parameters = new HashMap<>();

    protected BasePagedGetCommand(Class<T> type, int offset, int limit) {
        super(type);
        parameters.put("offset", offset);
        parameters.put("maxReturn", limit);
    }

    @Override
    public Map<String, Object> getParameters() {
        return parameters;
    }
}
