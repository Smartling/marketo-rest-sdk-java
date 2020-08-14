package com.smartling.marketo.sdk.rest.command.landingpage;

import com.smartling.marketo.sdk.domain.landingpage.LandingPageVariable;
import com.smartling.marketo.sdk.rest.command.BaseMarketoCommand;

import java.util.Collections;
import java.util.Map;

public class UpdateLandingPageVariable extends BaseMarketoCommand<LandingPageVariable> {
    private final int landingPageId;
    private final LandingPageVariable variable;

    public UpdateLandingPageVariable(int landingPageId, LandingPageVariable variable) {
        super(LandingPageVariable.class);
        this.landingPageId = landingPageId;
        this.variable = variable;
    }

    @Override
    public String getPath() {
        return "/asset/v1/landingPage/" + landingPageId + "/variable/" + variable.getId() + ".json";
    }

    @Override
    public String getMethod() {
        return "POST";
    }

    @Override
    public Map<String, Object> getParameters() {
        return Collections.singletonMap("value", variable.getValue());
    }
}
