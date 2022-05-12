package com.smartling.marketo.sdk.rest.command.landingpage;

import com.smartling.marketo.sdk.domain.Asset.Status;
import com.smartling.marketo.sdk.domain.folder.FolderId;
import com.smartling.marketo.sdk.domain.landingpage.LandingPage;
import com.smartling.marketo.sdk.rest.command.BasePagedGetCommand;

public class GetLandingPages extends BasePagedGetCommand<LandingPage> {
    public GetLandingPages(Integer offset, Integer limit, FolderId folder, Status status) {
        super(LandingPage.class, offset, limit);

        super.getParameters().put("folder", folder);
        super.getParameters().put("status", status);
    }

    @Override
    public String getPath() {
        return "/asset/v1/landingPages.json";
    }
}
