package com.smartling.marketo.sdk.rest.command.email;

import com.smartling.marketo.sdk.domain.email.DynamicContentItem;
import com.smartling.marketo.sdk.rest.Command;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class UpdateEmailDynamicContent implements Command<Void>
{
    private final int emailId;
    private final String dynamicContentId;
    private final DynamicContentItem dynamicContentItem;

    public UpdateEmailDynamicContent(int emailId, String dynamicContentId, DynamicContentItem dynamicContentItem)
    {
        this.emailId = emailId;
        this.dynamicContentId = dynamicContentId;
        this.dynamicContentItem = dynamicContentItem;
    }

    @Override
    public String getPath()
    {
        return "/asset/v1/email/" + emailId + "/dynamicContent/" + dynamicContentId + ".json";
    }

    @Override
    public String getMethod()
    {
        return "POST";
    }

    @Override
    public Type getResultType()
    {
        return Void.TYPE;
    }

    @Override
    public Map<String, Object> getParameters()
    {
        Map<String, Object> params = new HashMap<>();
        params.put("type", dynamicContentItem.getType());
        params.put("segment", dynamicContentItem.getSegmentName());
        params.put("value", dynamicContentItem.getContent());
        return params;
    }
}
