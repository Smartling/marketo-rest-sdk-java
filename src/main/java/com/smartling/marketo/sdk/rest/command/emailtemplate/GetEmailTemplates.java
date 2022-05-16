package com.smartling.marketo.sdk.rest.command.emailtemplate;

import com.smartling.marketo.sdk.domain.folder.FolderId;
import com.smartling.marketo.sdk.domain.emailtemplate.EmailTemplate;
import com.smartling.marketo.sdk.rest.command.BasePagedGetCommand;

public class GetEmailTemplates extends BasePagedGetCommand<EmailTemplate> {
    public GetEmailTemplates(Integer offset, Integer limit, FolderId folder, EmailTemplate.Status status) {
        super(EmailTemplate.class, offset, limit);

        super.getParameters().put("folder", folder);
        super.getParameters().put("status", status);
    }

    @Override
    public String getPath() {
        return "/asset/v1/emailTemplates.json";
    }
}
