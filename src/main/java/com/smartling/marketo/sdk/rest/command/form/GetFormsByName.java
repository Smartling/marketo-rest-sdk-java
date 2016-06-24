package com.smartling.marketo.sdk.rest.command.form;

import com.google.common.collect.ImmutableMap;
import com.smartling.marketo.sdk.domain.form.Form;
import com.smartling.marketo.sdk.domain.folder.FolderId;
import com.smartling.marketo.sdk.rest.command.BaseGetCommand;

import java.util.Map;

public class GetFormsByName extends BaseGetCommand<Form> {
    private final String name;
    private final FolderId folder;
    private final Form.Status status;

    public GetFormsByName(String name, FolderId folder, Form.Status status) {
        super(Form.class);
        this.name = name;
        this.folder = folder;
        this.status = status;
    }

    @Override
    public String getPath() {
        return "/asset/v1/form/byName.json";
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
