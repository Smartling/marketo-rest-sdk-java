package com.smartling.marketo.sdk.rest.command.email;

import com.smartling.marketo.sdk.domain.email.Email;
import com.smartling.marketo.sdk.rest.command.BaseMarketoCommand;

public class DeleteEmail extends BaseMarketoCommand<Email> {

    private final int emailId;

    public DeleteEmail(int emailId) {
        super(Email.class);
        this.emailId = emailId;
    }

    @Override
    public String getPath() {
        return "/asset/v1/email/" + emailId + "/delete.json";
    }

    @Override
    public String getMethod() {
        return "POST";
    }

}
