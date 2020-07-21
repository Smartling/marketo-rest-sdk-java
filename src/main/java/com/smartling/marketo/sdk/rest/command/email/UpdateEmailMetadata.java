package com.smartling.marketo.sdk.rest.command.email;

import com.smartling.marketo.sdk.domain.email.Email;
import com.smartling.marketo.sdk.rest.command.BaseMarketoCommand;

import java.util.Collections;
import java.util.Map;

public class UpdateEmailMetadata extends BaseMarketoCommand<Email>
{
    private final Email email;

    public UpdateEmailMetadata(Email email) {
        super(Email.class);
        this.email = email;
    }

    @Override
    public String getPath() {
        return "/asset/v1/email/" + email.getId() + ".json";
    }

    @Override
    public String getMethod() {
        return "POST";
    }

    @Override
    public Map<String, Object> getParameters() {
        return Collections.singletonMap("preHeader", email.getPreHeader());
    }
}
