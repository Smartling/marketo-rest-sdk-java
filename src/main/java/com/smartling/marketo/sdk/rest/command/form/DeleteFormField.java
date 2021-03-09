package com.smartling.marketo.sdk.rest.command.form;

import com.smartling.marketo.sdk.domain.form.FormField;
import com.smartling.marketo.sdk.rest.command.BaseMarketoCommand;

public class DeleteFormField extends BaseMarketoCommand<FormField> {
    private final int formId;
    private final String fieldId;

    public DeleteFormField(int formId, String fieldId) {
        super(FormField.class);
        this.formId = formId;
        this.fieldId = fieldId;
    }

    @Override
    public String getPath() {
        return "/asset/v1/form/" + formId + "/field/" + fieldId + "/delete.json";
    }

    @Override
    public String getMethod() {
        return "POST";
    }
}
