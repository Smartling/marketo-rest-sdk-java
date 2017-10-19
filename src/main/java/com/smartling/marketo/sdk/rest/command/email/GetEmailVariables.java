package com.smartling.marketo.sdk.rest.command.email;

import com.smartling.marketo.sdk.domain.email.EmailVariable;
import com.smartling.marketo.sdk.rest.command.BaseGetCommand;

public class GetEmailVariables extends BaseGetCommand<EmailVariable> {
    private final int id;

    public GetEmailVariables(int id) {
        super(EmailVariable.class);
        this.id = id;
    }

    @Override
    public String getPath() {
        return "/asset/v1/email/" + id + "/variables.json";
    }
}
