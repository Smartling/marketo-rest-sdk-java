package com.smartling.marketo.sdk.rest.command.email;

import com.smartling.marketo.sdk.domain.email.Email;
import com.smartling.marketo.sdk.rest.command.BaseGetCommand;

public class LoadEmailById extends BaseGetCommand<Email> {
    private final int id;

    public LoadEmailById(int id) {
        super(Email.class);
        this.id = id;
    }

    @Override
    public String getPath() {
        return "/asset/v1/email/" + id + ".json";
    }
}
