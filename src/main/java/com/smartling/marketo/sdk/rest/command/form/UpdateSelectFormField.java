package com.smartling.marketo.sdk.rest.command.form;

import com.smartling.marketo.sdk.domain.form.SelectFormField;
import com.smartling.marketo.sdk.domain.form.SelectItemValue;

public class UpdateSelectFormField extends UpdateFormFieldBase<SelectItemValue> {
    public UpdateSelectFormField(int formId, SelectFormField formField) {
        super(formId, formField);
    }
}
