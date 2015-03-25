package com.smartling.marketo.sdk.rest.command;

import com.smartling.marketo.sdk.Email;

import java.util.HashMap;
import java.util.Map;

public class GetEmailsCommand extends BaseGetCommand<Email> {
    private final Map<String, Object> parameters = new HashMap<>();

    public GetEmailsCommand(int offset, int limit) {
        super(Email.class);
        parameters.put("offset", offset);
        parameters.put("maxReturn", limit);
    }

    @Override
    public String getPath() {
        return "/asset/v1/emails.json";
    }

    @Override
    public Map<String, Object> getParameters() {
        return parameters;
    }
}
