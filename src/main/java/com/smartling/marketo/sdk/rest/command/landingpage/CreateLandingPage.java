package com.smartling.marketo.sdk.rest.command.landingpage;

import com.google.common.collect.ImmutableMap;
import com.smartling.marketo.sdk.domain.folder.FolderId;
import com.smartling.marketo.sdk.domain.landingpage.LandingPage;
import com.smartling.marketo.sdk.rest.command.BaseMarketoCommand;

import java.util.Map;

public class CreateLandingPage extends BaseMarketoCommand<LandingPage> {
    private String name;
    private FolderId folder;
    private Integer template;

    public CreateLandingPage(String name, FolderId folder, Integer template) {
        super(LandingPage.class);
        this.name = name;
        this.folder = folder;
        this.template = template;
    }

    @Override
    public String getPath() {
        return "/asset/v1/landingPages.json";
    }

    @Override
    public String getMethod() {
        return "POST";
    }

    @Override
    public Map<String, Object> getParameters() {
        ImmutableMap.Builder<String, Object> builder = ImmutableMap.<String, Object>builder()
                .put("name", name)
                .put("folder", folder)
                .put("template", template);
        return builder.build();
    }
}