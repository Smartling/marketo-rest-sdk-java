package com.smartling.marketo.sdk.rest.command.landingpage;

import com.smartling.marketo.sdk.domain.landingpage.LandingPage;
import com.smartling.marketo.sdk.rest.command.BaseMarketoCommand;

public class ApproveLandingPageDraft extends BaseMarketoCommand<LandingPage> {
    private final int id;

    public ApproveLandingPageDraft(int id) {
        super(LandingPage.class);
        this.id = id;
    }

    @Override
    public String getPath() {
        return "/asset/v1/landingPage/" + id + "/approveDraft.json";
    }

    @Override
    public String getMethod() {
        return "POST";
    }
}
