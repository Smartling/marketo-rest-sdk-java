package com.smartling.marketo.sdk.rest.command.form;

import com.smartling.marketo.sdk.domain.folder.FolderId;
import com.smartling.marketo.sdk.domain.form.Form;
import com.smartling.marketo.sdk.rest.command.BasePagedGetCommand;

public class GetFormsByName extends BasePagedGetCommand<Form> {
    public GetFormsByName(Integer offset, Integer limit, String name, FolderId folder, Form.Status status) {
        super(Form.class, offset, limit);
        super.getParameters().put("name", name);

        if (folder != null) {
            super.getParameters().put("folder", folder);
        }
        if (status != null) {
            super.getParameters().put("status", status);
        }
    }

    @Override
    public String getPath() {
        return "/asset/v1/form/byName.json";
    }
}
