package com.smartling.marketo.sdk.rest.command.form;

import com.google.common.collect.ImmutableMap;
import com.smartling.marketo.sdk.domain.form.FormFieldBase;
import com.smartling.marketo.sdk.rest.Command;

import java.lang.reflect.Type;
import java.util.Map;

import static com.smartling.marketo.sdk.rest.command.form.FormUtils.copyFormFieldProperties;

public class UpdateFormFieldBase<T> implements Command<FormFieldBase<T>>
{
    private final int formId;
    private final FormFieldBase<T> formField;

    public UpdateFormFieldBase(int formId, FormFieldBase<T> formField) {
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
