package com.smartling.marketo.sdk.rest.command;

import com.google.common.collect.ImmutableMap;
import com.smartling.marketo.sdk.Email;
import com.smartling.marketo.sdk.FolderId;

import java.util.Map;

public class GetEmailsByName extends BaseGetCommand<Email> {
    private final String name;
    private final FolderId folder;
    private final Email.Status status;

    public GetEmailsByName(String name, FolderId folder, Email.Status status) {
        super(Email.class);
        this.name = name;
        this.folder = folder;
        this.status = status;
    }

    @Override
    public String getPath() {
        return "/asset/v1/email/byName.json";
    }

    @Override
    public Map<String, Object> getParameters() {
        ImmutableMap.Builder<String, Object> builder = ImmutableMap.<String, Object>builder()
                .put("name", name);
        if (folder != null) {
            builder.put("folder", folder);
        }
        if (status != null) {
            builder.put("status", status);
        }
        return builder.build();
    }
}
