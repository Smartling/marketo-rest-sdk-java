package com.smartling.marketo.sdk.rest.command.landingpage;

import com.smartling.marketo.sdk.domain.landingpage.DynamicContent;
import com.smartling.marketo.sdk.rest.command.BaseGetCommand;

public class GetLandingPageDynamicContent extends BaseGetCommand<DynamicContent>
{
    private final int landingPageId;
    private final String dynamicContentId;

    public GetLandingPageDynamicContent(int landingPageId, String dynamicContentId)
    {
        super(DynamicContent.class);
        this.landingPageId = landingPageId;
        this.dynamicContentId = dynamicContentId;
    }

    @Override
    public String getPath()
    {
        return "/asset/v1/landingPage/" + landingPageId + "/dynamicContent/" + dynamicContentId + ".json";
    }
}
