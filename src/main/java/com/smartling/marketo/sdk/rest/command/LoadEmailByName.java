package com.smartling.marketo.sdk.rest.command;

import com.smartling.marketo.sdk.Email;

import java.util.Collections;
import java.util.Map;

public class LoadEmailByName extends BaseGetCommand<Email> {
    private final String name;

    public LoadEmailByName(String name) {
        super(Email.class);
        this.name = name;
    }

    @Override
    public String getPath() {
        return "/asset/v1/email/byName.json";
    }

    @Override
    public Map<String, Object> getParameters() {
        return Collections.<String, Object>singletonMap("name", name);
    }
}
