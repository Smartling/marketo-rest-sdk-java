package com.smartling.marketo.sdk.rest.command.email;

import com.smartling.marketo.sdk.domain.email.Email;
import com.smartling.marketo.sdk.rest.command.BaseMarketoCommand;

public class ApproveEmailDraft extends BaseMarketoCommand<Email> {
    private final int id;

    public ApproveEmailDraft(int id) {
        super(Email.class);
        this.id = id;
    }

    @Override
    public String getPath() {
        return "/asset/v1/email/" + id + "/approveDraft.json";
    }

    @Override
    public String getMethod() {
        return "POST";
    }
}