package com.smartling.marketo.sdk.rest.command.form;

import com.smartling.marketo.sdk.domain.form.FormField;
import com.smartling.marketo.sdk.rest.command.BaseGetCommand;

public class LoadFormFields extends BaseGetCommand<FormField> {
    private final int formId;

    public LoadFormFields(int formId) {
        super(FormField.class);
        this.formId = formId;
    }

    @Override
    public String getPath() {
        return "/asset/v1/form/" + formId + "/fields.json";
    }
}
