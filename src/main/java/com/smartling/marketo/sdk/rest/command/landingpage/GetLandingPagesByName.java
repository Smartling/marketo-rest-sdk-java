package com.smartling.marketo.sdk.rest.command.landingpage;

import com.google.common.collect.ImmutableMap;
import com.smartling.marketo.sdk.domain.Asset.Status;
import com.smartling.marketo.sdk.domain.folder.FolderId;
import com.smartling.marketo.sdk.domain.landingpage.LandingPage;
import com.smartling.marketo.sdk.rest.command.BaseGetCommand;

import java.util.Map;

public class GetLandingPagesByName extends BaseGetCommand<LandingPage> {
    private final String name;
    private final FolderId folderId;
    private final Status status;

    public GetLandingPagesByName(String name, FolderId folderId, Status status) {
        super(LandingPage.class);
        this.name = name;
        this.folderId = folderId;
        this.status = status;
    }

    @Override
    public String getPath() {
        return "/asset/v1/landingPage/byName.json";
    }

    @Override
    public Map<String, Object> getParameters() {
        ImmutableMap.Builder<String, Object> builder = ImmutableMap.<String, Object>builder()
                .put("name", name);
        if (folderId != null) {
            builder.put("folder", folderId);
        }
        if (status != null) {
            builder.put("status", status);
        }
        return builder.build();
    }
}
