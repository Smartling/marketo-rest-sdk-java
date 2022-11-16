package com.smartling.marketo.sdk.rest.command.form;

import com.google.common.collect.ImmutableMap;
import com.smartling.marketo.sdk.domain.form.FormField;
import com.smartling.marketo.sdk.rest.Command;

import java.lang.reflect.Type;
import java.util.Map;

import static com.smartling.marketo.sdk.rest.command.form.FormUtils.copyFormFieldProperties;

public class UpdateFormField implements Command<FormField>
{
    private final int formId;
    private final FormField formField;

    public UpdateFormField(int formId, FormField formField) {
        this.formId = formId;
        this.formField = formField;
    }

    @Override
    public String getPath() {
        return "/asset/v1/form/" + formId + "/field/" + formField.getId() + ".json";
    }

    @Override
    public String getMethod() {
        return "POST";
    }

    @Override
    public Type getResultType() {
        return Void.TYPE;
    }

    @Override
    public Map<String, Object> getParameters() {
        ImmutableMap.Builder<String, Object> builder = ImmutableMap.builder();
        return copyFormFieldProperties(builder, formField);
    }
}
