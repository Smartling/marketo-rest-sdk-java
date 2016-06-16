package com.smartling.marketo.sdk.rest.command.landingpage;

import com.smartling.marketo.sdk.domain.landingpage.LandingPage;
import com.smartling.marketo.sdk.rest.command.BaseMarketoCommand;

import java.util.Collections;
import java.util.Map;

public class UpdateLandingPageMetadata extends BaseMarketoCommand<LandingPage> {
    private final int id;
    private final String title;

    public UpdateLandingPageMetadata(int id, String title) {
        super(LandingPage.class);
        this.id = id;
        this.title = title;
    }

    @Override
    public String getPath() {
        return "/asset/v1/landingPage/" + id + ".json";
    }

    @Override
    public String getMethod() {
        return "POST";
    }

    @Override
    public Map<String, Object> getParameters() {
        return Collections.singletonMap("title", title);
    }
}
