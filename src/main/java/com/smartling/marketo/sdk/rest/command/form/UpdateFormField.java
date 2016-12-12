package com.smartling.marketo.sdk.rest.command.form;

import com.google.common.collect.ImmutableMap;
import com.smartling.marketo.sdk.domain.form.FormField;
import com.smartling.marketo.sdk.rest.Command;

import java.lang.reflect.Type;
import java.util.Map;

public class UpdateFormField implements Command<FormField> {
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

        if (formField.getLabel() != null) {
            builder.put("label", formField.getLabel());
        }
        if (formField.getInstructions() != null) {
            builder.put("instructions", formField.getInstructions());
        }
        if (formField.getValidationMessage() != null) {
            builder.put("validationMessage", formField.getValidationMessage());
        }
        if (formField.getHintText() != null) {
            builder.put("hintText", formField.getHintText());
        }
        if (formField.getDefaultValue() != null) {
            builder.put("defaultValue", formField.getDefaultValue());
        }
        if (formField.getFieldMetaData() != null && formField.getFieldMetaData().getValues() != null) {
            builder.put("values", formField.getFieldMetaData().getValues());
        }

        return builder.build();
    }
}
