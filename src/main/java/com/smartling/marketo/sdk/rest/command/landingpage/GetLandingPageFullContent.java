package com.smartling.marketo.sdk.rest.command.landingpage;

import com.smartling.marketo.sdk.domain.landingpage.LandingPageFullContent;
import com.smartling.marketo.sdk.rest.command.BaseGetCommand;

public class GetLandingPageFullContent extends BaseGetCommand<LandingPageFullContent> {
    private final int landingPageId;

    public GetLandingPageFullContent(int landingPageId) {
        super(LandingPageFullContent.class);
        this.landingPageId = landingPageId;
    }

    @Override
    public String getPath() {
        return "/asset/v1/landingPage/" + landingPageId + "/fullContent.json";
    }
}
