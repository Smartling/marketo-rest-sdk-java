package com.smartling.marketo.sdk.rest.command.email;

import com.smartling.marketo.sdk.domain.email.Email;
import com.smartling.marketo.sdk.domain.folder.FolderId;
import com.smartling.marketo.sdk.rest.command.BasePagedGetCommand;

public class GetEmailsCommand extends BasePagedGetCommand<Email> {
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
