package com.smartling.marketo.sdk.rest.command.landingpage;

import com.smartling.marketo.sdk.domain.Asset.Status;
import com.smartling.marketo.sdk.domain.landingpage.LandingPageVariable;
import com.smartling.marketo.sdk.rest.command.BaseGetCommand;

import java.util.Collections;
import java.util.Map;

public class GetLandingPageVariables extends BaseGetCommand<LandingPageVariable> {
    private final int id;
    private final Status status;

    public GetLandingPageVariables(int id, Status status) {
        super(LandingPageVariable.class);
        this.id = id;
        this.status = status;
    }

    @Override
    public String getPath() {
        return "/asset/v1/landingPage/" + id + "/variables.json";
    }

    @Override
    public Map<String, Object> getParameters() {
        return status!=null ? Collections.singletonMap("status", status) : super.getParameters();
    }
}
