package com.smartling.marketo.sdk.rest.command.landingpagetemplate;

import com.smartling.marketo.sdk.domain.landingpagetemplate.LandingPageTemplate;
import com.smartling.marketo.sdk.domain.folder.FolderId;
import com.smartling.marketo.sdk.rest.command.BasePagedGetCommand;

public class GetLandingPageTemplates extends BasePagedGetCommand<LandingPageTemplate> {
    public GetLandingPageTemplates(int offset, int limit, FolderId folder, LandingPageTemplate.Status status) {
        super(LandingPageTemplate.class, offset, limit);

        super.getParameters().put("folder", folder);
        super.getParameters().put("status", status);
    }

    @Override
    public String getPath() {
        return "/asset/v1/landingPageTemplates.json";
    }
}
