package com.smartling.marketo.sdk.rest.command.form;

import com.smartling.marketo.sdk.domain.form.Form;
import com.smartling.marketo.sdk.rest.command.BaseMarketoCommand;

public class DiscardFormDraftCommand extends BaseMarketoCommand<Form> {
    private final int formId;

    public DiscardFormDraftCommand(int formId) {
        super(Form.class);
        this.formId = formId;
    }

    @Override
    public String getPath() {
        return "/asset/v1/form/" + formId + "/discardDraft.json";
    }

    @Override
    public String getMethod() {
        return "POST";
    }
}
