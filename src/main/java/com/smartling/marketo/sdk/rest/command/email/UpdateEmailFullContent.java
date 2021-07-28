package com.smartling.marketo.sdk.rest.command.email;

import com.smartling.marketo.sdk.rest.Command;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.Map;

public class UpdateEmailFullContent implements Command<Void>
{

    private final int id;
    private final String content;

    public UpdateEmailFullContent(int id, String content)
    {
        this.id = id;
        this.content = content;
    }


    @Override
    public String getPath()
    {
        return "/asset/v1/email/" + id + "/fullContent.json";
    }

    @Override
    public String getMethod()
    {
        return "MULTIPART";
    }

    @Override
    public Type getResultType()
    {
        return Void.TYPE;
    }

    @Override
    public Map<String, Object> getParameters()
    {
        return Collections.singletonMap("content", content);
    }
}
