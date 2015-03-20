package com.smartling.marketo.sdk.command;

import com.smartling.marketo.sdk.EmailContentItem;

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
