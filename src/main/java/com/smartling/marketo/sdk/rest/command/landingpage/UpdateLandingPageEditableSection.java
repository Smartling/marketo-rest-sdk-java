package com.smartling.marketo.sdk.rest.command.landingpage;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;
import com.smartling.marketo.sdk.domain.landingpage.LandingPageTextContentItem;
import com.smartling.marketo.sdk.rest.Command;

import java.lang.reflect.Type;
import java.util.Map;

public class UpdateLandingPageEditableSection implements Command<Void> {
    private final int landingPageId;
    private final LandingPageTextContentItem contentItem;

    public UpdateLandingPageEditableSection(int landingPageId, LandingPageTextContentItem contentItem) {
        this.landingPageId = landingPageId;
        this.contentItem = contentItem;
    }

    @Override
    public String getPath() {
        return "/asset/v1/landingPage/" + landingPageId + "/content/" + contentItem.getId() + ".json";
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
        Builder<String, Object> builder = ImmutableMap.<String, Object>builder();

        builder.put("type", contentItem.getType());
        builder.put("value", contentItem.getContent());

        return builder.build();
    }
}
