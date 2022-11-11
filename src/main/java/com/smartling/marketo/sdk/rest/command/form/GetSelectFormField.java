package com.smartling.marketo.sdk.rest.command.form;

import com.smartling.marketo.sdk.domain.form.SelectFormField;
import com.smartling.marketo.sdk.rest.command.BaseGetCommand;

public class GetSelectFormField extends BaseGetCommand<SelectFormField>
{
    private final int formId;
    private final String fieldId;

    public GetSelectFormField(int formId, String fieldId) {
        super(SelectFormField.class);
        this.formId = formId;
        this.fieldId = fieldId;
    }

    @Override
    public String getPath() {
        return "/asset/v1/form/" + formId + "/fields" + fieldId +".json";
    }
}
