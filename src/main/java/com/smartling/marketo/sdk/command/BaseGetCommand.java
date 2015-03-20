package com.smartling.marketo.sdk.command;

import com.smartling.marketo.sdk.Command;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public abstract class BaseGetCommand<T> implements Command<List<T>> {
    private final Class<T> type;

    BaseGetCommand(Class<T> type) {
        this.type = type;
    }

    @Override
    public String getMethod() {
        return "GET";
    }

    @Override
    public Type getResultType() {
        return new ParameterizedType() {
            @Override
            public Type[] getActualTypeArguments() {
                return new Type[]{type};
            }

            @Override
            public Type getRawType() {
                return List.class;
            }

            @Override
            public Type getOwnerType() {
                return List.class;
            }
        };
    }

    @Override
    public Map<String, Object> getParameters() {
        return Collections.emptyMap();
    }
}
