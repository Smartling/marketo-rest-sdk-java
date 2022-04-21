package com.smartling.marketo.sdk.rest.command.form;

import com.google.common.collect.ImmutableMap;
import com.smartling.marketo.sdk.domain.form.FormField;
import com.smartling.marketo.sdk.rest.command.BaseMarketoCommand;

import java.util.Map;

import static com.smartling.marketo.sdk.rest.command.form.FormUtils.copyFormFieldProperties;

public class CreateFormField extends BaseMarketoCommand<FormField> {
    private final int formId;
    private final FormField formField;

    public CreateFormField(int formId, FormField formField) {
        super(FormField.class);
        this.formId = formId;
        this.formField = formField;
    }

    @Override
    public String getPath() {
        return "/asset/v1/form/" + formId + "/fields.json";
    }

    @Override
    public String getMethod() {
        return "POST";
    }

    @Override
    public Map<String, Object> getParameters() {
        ImmutableMap.Builder<String, Object> builder = ImmutableMap.builder();
        builder.put("fieldId", formField.getId());
        return copyFormFieldProperties(builder, formField);
    }
}
