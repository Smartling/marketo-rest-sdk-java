package com.smartling.marketo.sdk.command;

import com.smartling.marketo.sdk.Email;

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
