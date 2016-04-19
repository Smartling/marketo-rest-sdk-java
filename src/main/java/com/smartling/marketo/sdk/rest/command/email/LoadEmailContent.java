package com.smartling.marketo.sdk.rest.command.email;

import com.smartling.marketo.sdk.domain.email.EmailContentItem;
import com.smartling.marketo.sdk.rest.command.BaseGetCommand;

public class LoadEmailContent extends BaseGetCommand<EmailContentItem> {
    private final int emailId;

    public LoadEmailContent(int emailId) {
        super(EmailContentItem.class);
        this.emailId = emailId;
    }

    @Override
    public String getPath() {
        return "/asset/v1/email/" + emailId + "/content";
    }
}
