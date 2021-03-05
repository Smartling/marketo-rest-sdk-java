package com.smartling.marketo.sdk.rest.command.landingpage;

import com.google.common.collect.ImmutableMap;
import com.smartling.marketo.sdk.domain.landingpage.LandingPage;
import com.smartling.marketo.sdk.domain.folder.FolderId;
import com.smartling.marketo.sdk.rest.command.BaseMarketoCommand;

import java.util.Map;

public class CloneLandingPage extends BaseMarketoCommand<LandingPage> {
    private final int sourceId;
    private final String newName;
    private final FolderId folder;
    private final int templateId;

    public CloneLandingPage(int sourceId, String newName, FolderId folder, int templateId) {
        super(LandingPage.class);
        this.sourceId = sourceId;
        this.newName = newName;
        this.folder = folder;
        this.templateId = templateId;
    }

    @Override
    public String getPath() {
        return "/asset/v1/landingPage/" + sourceId + "/clone.json";
    }

    @Override
    public String getMethod() {
        return "POST";
    }

    @Override
    public Map<String, Object> getParameters() {
        return ImmutableMap.of("name", newName, "folder", folder, "template", templateId);
    }
}
