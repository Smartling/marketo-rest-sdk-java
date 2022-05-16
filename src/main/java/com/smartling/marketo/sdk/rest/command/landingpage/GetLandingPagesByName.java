package com.smartling.marketo.sdk.rest.command.landingpage;

import com.smartling.marketo.sdk.domain.Asset.Status;
import com.smartling.marketo.sdk.domain.folder.FolderId;
import com.smartling.marketo.sdk.domain.landingpage.LandingPage;
import com.smartling.marketo.sdk.rest.command.BasePagedGetCommand;

public class GetLandingPagesByName extends BasePagedGetCommand<LandingPage> {

    public GetLandingPagesByName(Integer offset, Integer limit, String name, FolderId folder, Status status) {
        super(LandingPage.class, offset, limit);

        super.getParameters().put("name", name);

        if (folder != null) {
            super.getParameters().put("folder", folder);
        }
        if (status != null) {
            super.getParameters().put("status", status);
        }
    }

    @Override
    public String getPath() {
        return "/asset/v1/landingPage/byName.json";
    }
}
