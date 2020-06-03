package com.smartling.marketo.sdk.rest.command.landingpage;

import com.google.common.collect.ImmutableMap;
import com.smartling.marketo.sdk.domain.landingpage.LandingPage;
import com.smartling.marketo.sdk.rest.command.BaseMarketoCommand;

import java.util.Collections;
import java.util.Map;

public class UpdateLandingPageMetadata extends BaseMarketoCommand<LandingPage> {
    private final int id;
    private final Map<String, String> tagMap;

    public UpdateLandingPageMetadata(int id, String title) {
        super(LandingPage.class);
        this.id = id;
        this.tagMap = ImmutableMap.<String, String>builder().put("title", title).build();
    }

    public UpdateLandingPageMetadata(int id, Map<String, String> tagMap) {
        super(LandingPage.class);
        this.id = id;
        this.tagMap = tagMap;
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
        ImmutableMap.Builder<String, Object> builder = ImmutableMap.<String, Object>builder();
        tagMap.entrySet().forEach(t -> builder.put(t.getKey(), t.getValue()));
        return builder.build();
    }
}
