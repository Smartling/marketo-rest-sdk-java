package com.smartling.marketo.sdk.rest.command.form;

import com.smartling.marketo.sdk.domain.form.Form;
import com.smartling.marketo.sdk.rest.command.BaseMarketoCommand;

public class ApproveFormDraft extends BaseMarketoCommand<Form> {
    private final int id;

    public ApproveFormDraft(int id) {
        super(Form.class);
        this.id = id;
    }

    @Override
    public String getPath() {
        return "/asset/v1/form/" + id + "/approveDraft.json";
    }

    @Override
    public String getMethod() {
        return "POST";
    }
}
