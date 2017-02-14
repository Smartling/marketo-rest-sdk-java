package com.smartling.marketo.sdk.rest.command.emailtemplate;

import com.smartling.marketo.sdk.domain.Asset.Status;
import com.smartling.marketo.sdk.domain.emailtemplate.EmailTemplateContent;
import com.smartling.marketo.sdk.rest.command.BaseGetCommand;

import java.util.Collections;
import java.util.Map;

public class GetEmailTemplateContent extends BaseGetCommand<EmailTemplateContent> {
    private final int id;
    private final Status status;

    public GetEmailTemplateContent(int id, Status status) {
        super(EmailTemplateContent.class);
        this.id = id;
        this.status = status;
    }

    @Override
    public String getPath() {
        return "/asset/v1/emailTemplate/" + id + "/content.json";
    }

    @Override
    public Map<String, Object> getParameters() {
        return status!=null ? Collections.singletonMap("status", status) : super.getParameters();
    }
}
