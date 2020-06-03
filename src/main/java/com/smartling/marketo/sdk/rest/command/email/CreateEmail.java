package com.smartling.marketo.sdk.rest.command.email;

import com.google.common.collect.ImmutableMap;
import com.smartling.marketo.sdk.domain.email.Email;
import com.smartling.marketo.sdk.domain.folder.FolderId;
import com.smartling.marketo.sdk.rest.command.BaseMarketoCommand;

import java.util.Map;

public class CreateEmail extends BaseMarketoCommand<Email> {
    private String name;
    private FolderId folder;
    private Integer template;

    public CreateEmail(String name, FolderId folder, Integer template) {
        super(Email.class);
        this.name = name;
        this.folder = folder;
        this.template = template;
    }

    @Override
    public String getPath() {
        return "/asset/v1/emails.json";
    }

    @Override
    public String getMethod() {
        return "POST";
    }

    @Override
    public Map<String, Object> getParameters() {
        ImmutableMap.Builder<String, Object> builder = ImmutableMap.<String, Object>builder()
                .put("name", name)
                .put("folder", folder)
                .put("template", template);
        return builder.build();
    }
}
