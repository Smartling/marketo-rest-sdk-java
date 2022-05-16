package com.smartling.marketo.sdk.rest.command;

import java.util.HashMap;
import java.util.Map;

abstract public class BasePagedGetCommand<T> extends BaseGetCommand<T>
{
    private final Map<String, Object> parameters = new HashMap<>();

    protected BasePagedGetCommand(Class<T> type, Integer offset, Integer limit) {
        super(type);

        if (offset != null) {
            parameters.put("offset", offset);
        }

        if (limit != null) {
            parameters.put("maxReturn", limit);
        }
    }

    @Override
    public Map<String, Object> getParameters() {
        return parameters;
    }
}
