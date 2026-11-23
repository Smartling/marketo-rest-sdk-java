package com.smartling.marketo.sdk.rest.command.v2.fragment;

import java.lang.reflect.Type;

/**
 * Base for GET commands against the Asset API v2 fragment endpoints.
 */
public abstract class BaseFragmentGetCommand<R> extends BaseFragmentCommand<R>
{
    protected BaseFragmentGetCommand(Type resultType)
    {
        super(resultType);
    }

    @Override
    public String getMethod()
    {
        return "GET";
    }
}
