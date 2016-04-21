package com.smartling.marketo.sdk.rest.command.landingpage;

import com.smartling.marketo.sdk.domain.Asset.Status;
import com.smartling.marketo.sdk.domain.landingpage.LandingPage;
import com.smartling.marketo.sdk.rest.command.BaseGetCommand;

import java.util.Collections;
import java.util.Map;

public class GetLandingPageById extends BaseGetCommand<LandingPage> {
    private final int id;
    private final Status status;

    public GetLandingPageById(int id, Status status) {
        super(LandingPage.class);
        this.id = id;
        this.status = status;
    }

    @Override
    public String getPath() {
        return "/asset/v1/landingPage/" + id + ".json";
    }

    @Override
    public Map<String, Object> getParameters() {
        return status!=null ? Collections.singletonMap("status", status) : super.getParameters();
    }
}
