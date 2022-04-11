package com.smartling.marketo.sdk.rest.command.landingpage;

import com.smartling.marketo.sdk.domain.landingpage.LandingPage;
import com.smartling.marketo.sdk.rest.command.BaseMarketoCommand;

public class DeleteLandingPage extends BaseMarketoCommand<LandingPage> {

    private final int sourceId;

    public DeleteLandingPage(int sourceId) {
        super(LandingPage.class);
        this.sourceId = sourceId;
    }

    @Override
    public String getPath() {
        return "/asset/v1/landingPage/" + sourceId + "/delete.json";
    }

    @Override
    public String getMethod() {
        return "POST";
    }
}
