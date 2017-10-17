package com.smartling.marketo.sdk.rest.command.landingpage;

import com.smartling.marketo.sdk.domain.landingpage.LandingPageVariable;
import com.smartling.marketo.sdk.rest.Command;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.Map;

public class UpdateLandingPageVariable implements Command<Void> {
    private final int landingPageId;
    private final LandingPageVariable variable;

    public UpdateLandingPageVariable(int landingPageId, LandingPageVariable variable) {
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
    public Type getResultType() {
        return Void.TYPE;
    }

    @Override
    public Map<String, Object> getParameters() {
        return Collections.singletonMap("value", variable.getValue());
    }
}
