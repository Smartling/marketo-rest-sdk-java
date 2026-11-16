package com.smartling.marketo.sdk.rest.command.v2.email;

import com.smartling.marketo.sdk.rest.Command;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.Map;

/**
 * Base for commands against the Asset API v2 email endpoints. Unlike the v1 base commands, the result is
 * not necessarily a list, so the result type is supplied by the subclass.
 */
public abstract class BaseEmailCommand<R> implements Command<R>
{
    private static final Map<String, String> HEADERS = Collections.singletonMap("x-app-type", "marketo");

    private final Type resultType;

    protected BaseEmailCommand(Type resultType)
    {
        this.resultType = resultType;
    }

    @Override
    public Type getResultType()
    {
        return resultType;
    }

    @Override
    public Map<String, String> getHeaders()
    {
        return HEADERS;
    }

    @Override
    public Map<String, Object> getParameters()
    {
        return Collections.emptyMap();
    }
}
