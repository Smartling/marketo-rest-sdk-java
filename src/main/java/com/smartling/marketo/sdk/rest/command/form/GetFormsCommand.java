package com.smartling.marketo.sdk.rest.command.form;

import com.smartling.marketo.sdk.domain.folder.FolderId;
import com.smartling.marketo.sdk.domain.form.Form;
import com.smartling.marketo.sdk.rest.command.BasePagedGetCommand;

public class GetFormsCommand extends BasePagedGetCommand<Form> {
    public GetFormsCommand(int offset, int limit, FolderId folder, Form.Status status) {
        super(Form.class, offset, limit);

        super.getParameters().put("folder", folder);
        super.getParameters().put("status", status);
    }

    @Override
    public String getPath() {
        return "/asset/v1/forms.json";
    }
}
