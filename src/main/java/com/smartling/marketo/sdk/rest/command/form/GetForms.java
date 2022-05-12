package com.smartling.marketo.sdk.rest.command.form;

import com.smartling.marketo.sdk.domain.folder.FolderId;
import com.smartling.marketo.sdk.domain.form.Form;
import com.smartling.marketo.sdk.rest.command.BasePagedGetCommand;

public class GetForms extends BasePagedGetCommand<Form> {
    public GetForms(Integer offset, Integer limit, FolderId folder, Form.Status status) {
        super(Form.class, offset, limit);

        super.getParameters().put("folder", folder);
        super.getParameters().put("status", status);
    }

    @Override
    public String getPath() {
        return "/asset/v1/forms.json";
    }
}
