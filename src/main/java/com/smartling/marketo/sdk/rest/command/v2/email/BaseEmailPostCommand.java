package com.smartling.marketo.sdk.rest.command.v2.email;

import java.lang.reflect.Type;
import java.util.Map;

/**
 * Base for commands that post a JSON body to the Asset API v2 email endpoints. The body is a map of plain
 * strings and nested maps built by the subclass.
 */
public abstract class BaseEmailPostCommand<R> extends BaseEmailCommand<R>
{
    private final Map<String, Object> body;

    protected BaseEmailPostCommand(Type resultType, Map<String, Object> body)
    {
        super(resultType);
        this.body = body;
    }

    @Override
    public String getMethod()
    {
        return "JSON_POST";
    }

    @Override
    public Object getBody()
    {
        return body;
    }
}
