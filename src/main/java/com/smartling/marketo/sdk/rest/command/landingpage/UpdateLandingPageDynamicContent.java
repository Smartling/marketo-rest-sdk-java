package com.smartling.marketo.sdk.rest.command.landingpage;

import com.smartling.marketo.sdk.domain.landingpage.DynamicContentItem;
import com.smartling.marketo.sdk.rest.Command;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class UpdateLandingPageDynamicContent implements Command<Void>
{
    private final int landingPageId;
    private final String dynamicContentId;
    private final DynamicContentItem dynamicContentItem;

    public UpdateLandingPageDynamicContent(int landingPageId, String dynamicContentId, DynamicContentItem dynamicContentItem)
    {
        this.landingPageId = landingPageId;
        this.dynamicContentId = dynamicContentId;
        this.dynamicContentItem = dynamicContentItem;
    }

    @Override
    public String getPath()
    {
        return "/asset/v1/landingPage/" + landingPageId + "/dynamicContent/" + dynamicContentId + ".json";
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
