package com.smartling.marketo.sdk.rest.command.form;

import com.smartling.marketo.sdk.domain.form.FormField;
import com.smartling.marketo.sdk.domain.form.Value;

public class UpdateFormField extends UpdateFormFieldBase<Value> {
    public UpdateFormField(int formId, FormField formField) {
        super(formId, formField);
    }
}
