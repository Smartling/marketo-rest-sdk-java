package com.smartling.marketo.sdk.rest.command.form;

import com.smartling.marketo.sdk.domain.folder.FolderId;
import com.smartling.marketo.sdk.domain.form.Form;
import com.smartling.marketo.sdk.rest.command.BaseMarketoCommand;
import jersey.repackaged.com.google.common.collect.ImmutableMap;

import java.util.Map;

public class CloneFormCommand extends BaseMarketoCommand<Form> {
    private final int sourceFormId;
    private final String newFormName;
    private final FolderId folderId;
    private final String description;

    public CloneFormCommand(int sourceFormId, String newFormName, FolderId folderId, String description) {
        super(Form.class);
        this.sourceFormId = sourceFormId;
        this.newFormName = newFormName;
        this.folderId = folderId;
        this.description = description;
    }

    @Override
    public String getPath() {
        return "/asset/v1/form/" + sourceFormId + "/clone.json";
    }

    @Override
    public String getMethod() {
        return "POST";
    }

    @Override
    public Map<String, Object> getParameters() {
        return ImmutableMap.<String, Object>of("name", newFormName, "folder", folderId, "description", description);
    }
}
