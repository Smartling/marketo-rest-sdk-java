package com.smartling.marketo.sdk.rest.command.email;

import com.smartling.marketo.sdk.domain.email.Email;
import com.smartling.marketo.sdk.domain.folder.FolderId;
import com.smartling.marketo.sdk.rest.command.BaseMarketoCommand;
import jersey.repackaged.com.google.common.collect.ImmutableMap;

import java.util.Map;

public class CloneEmail extends BaseMarketoCommand<Email> {
    private final int sourceEmailId;
    private final String newEmailName;
    private final FolderId folderId;

    public CloneEmail(int sourceEmailId, String newEmailName, FolderId folderId) {
        super(Email.class);
        this.sourceEmailId = sourceEmailId;
        this.newEmailName = newEmailName;
        this.folderId = folderId;
    }

    @Override
    public String getPath() {
        return "/asset/v1/email/" + sourceEmailId + "/clone.json";
    }

    @Override
    public String getMethod() {
        return "POST";
    }

    @Override
    public Map<String, Object> getParameters() {
        return ImmutableMap.<String, Object>of("name", newEmailName, "folder", folderId);
    }
}
