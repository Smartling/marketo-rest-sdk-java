package com.smartling.marketo.sdk.rest.command;

import com.smartling.marketo.sdk.Email;
import com.smartling.marketo.sdk.FolderId;

public class GetEmailsCommand extends PagedGetCommand<Email> {
    public GetEmailsCommand(int offset, int limit, FolderId folder, Email.Status status) {
        super(Email.class, offset, limit);

        super.getParameters().put("folder", folder);
        super.getParameters().put("status", status);
    }

    @Override
    public String getPath() {
        return "/asset/v1/emails.json";
    }
}
