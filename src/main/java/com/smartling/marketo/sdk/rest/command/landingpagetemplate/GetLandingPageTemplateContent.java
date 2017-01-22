package com.smartling.marketo.sdk.rest.command.landingpagetemplate;

import com.smartling.marketo.sdk.domain.Asset.Status;
import com.smartling.marketo.sdk.domain.landingpagetemplate.LandingPageTemplateContentItem;
import com.smartling.marketo.sdk.rest.command.BaseGetCommand;

import java.util.Collections;
import java.util.Map;

public class GetLandingPageTemplateContent extends BaseGetCommand<LandingPageTemplateContentItem> {
    private final int id;
    private final Status status;

    public GetLandingPageTemplateContent(int id, Status status) {
        super(LandingPageTemplateContentItem.class);
        this.id = id;
        this.status = status;
    }

    @Override
    public String getPath() {
        return "/asset/v1/landingPageTemplate/" + id + "/content.json";
    }

    @Override
    public Map<String, Object> getParameters() {
        return status!=null ? Collections.singletonMap("status", status) : super.getParameters();
    }
}
