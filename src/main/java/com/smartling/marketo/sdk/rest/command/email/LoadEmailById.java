package com.smartling.marketo.sdk.rest.command.email;

import com.smartling.marketo.sdk.domain.Asset.Status;
import com.smartling.marketo.sdk.domain.email.Email;
import com.smartling.marketo.sdk.rest.command.BaseGetCommand;

import java.util.Collections;
import java.util.Map;

public class LoadEmailById extends BaseGetCommand<Email> {
    private final int id;
    private final Status status;

    public LoadEmailById(int id, Status status) {
        super(Email.class);
        this.id = id;
        this.status = status;
    }

    @Override
    public String getPath() {
        return "/asset/v1/email/" + id + ".json";
    }

    @Override
    public Map<String, Object> getParameters() {
        if (status != null) {
            return Collections.singletonMap("status", status);
        } else {
            return Collections.emptyMap();
        }
    }
}
