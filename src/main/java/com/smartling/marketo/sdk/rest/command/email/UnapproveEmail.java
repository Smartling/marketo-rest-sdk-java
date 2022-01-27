package com.smartling.marketo.sdk.rest.command.email;

import com.smartling.marketo.sdk.rest.command.BaseMarketoCommand;
import com.smartling.marketo.sdk.domain.email.Email;

public class UnapproveEmail extends BaseMarketoCommand<Email> {
    private final int id;

    public UnapproveEmail(int id) {
        super(Email.class);
        this.id = id;
    }

    @Override
    public String getPath() {
        return "/asset/v1/email/" + id + "/unapprove.json";
    }

    @Override
    public String getMethod() {
        return "POST";
    }
}
