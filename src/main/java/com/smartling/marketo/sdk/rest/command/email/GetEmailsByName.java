package com.smartling.marketo.sdk.rest.command.email;

import com.smartling.marketo.sdk.domain.email.Email;
import com.smartling.marketo.sdk.domain.folder.FolderId;
import com.smartling.marketo.sdk.rest.command.BasePagedGetCommand;

public class GetEmailsByName extends BasePagedGetCommand<Email> {

    public GetEmailsByName(Integer offset, Integer limit, String name, FolderId folder, Email.Status status) {
        super(Email.class, offset, limit);

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
        return "/asset/v1/email/byName.json";
    }
}
