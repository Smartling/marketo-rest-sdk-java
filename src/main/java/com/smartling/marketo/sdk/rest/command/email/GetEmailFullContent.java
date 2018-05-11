package com.smartling.marketo.sdk.rest.command.email;

import com.smartling.marketo.sdk.domain.Asset.Status;
import com.smartling.marketo.sdk.domain.email.EmailFullContent;
import com.smartling.marketo.sdk.rest.command.BaseGetCommand;

import java.util.Collections;
import java.util.Map;

public class GetEmailFullContent extends BaseGetCommand<EmailFullContent> {
    private final int emailId;
    private final Status status;

    public GetEmailFullContent(int emailId, Status status) {
        super(EmailFullContent.class);
        this.emailId = emailId;
        this.status = status;
    }

    @Override
    public String getPath() {
        return "/asset/v1/email/" + emailId + "/fullContent.json";
    }

    @Override
    public Map<String, Object> getParameters() {
        return Collections.singletonMap("status", status);
    }
}
