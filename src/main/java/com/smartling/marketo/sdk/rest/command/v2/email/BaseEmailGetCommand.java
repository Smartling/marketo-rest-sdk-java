package com.smartling.marketo.sdk.rest.command.v2.email;

import java.lang.reflect.Type;

/**
 * Base for GET commands against the Asset API v2 email endpoints.
 */
public abstract class BaseEmailGetCommand<R> extends BaseEmailCommand<R>
{
    protected BaseEmailGetCommand(Type resultType)
    {
        super(resultType);
    }

    @Override
    public String getMethod()
    {
        return "GET";
    }
}
