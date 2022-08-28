package com.smartling.marketo.sdk.rest.command.form;

import com.google.common.collect.ImmutableMap;
import com.smartling.marketo.sdk.domain.form.FormField;

import java.util.Map;

public class FormUtils {

    private FormUtils() {
    }

    public static Map<String, Object> copyFormFieldProperties(ImmutableMap.Builder<String, Object> builder, FormField formField) {
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
            builder.put("values", formField.getFieldMetaData());
        }
        if (formField.getDataType() != null) {
            builder.put("dataType", formField.getDataType());
        }
        if (formField.isRequired() != null) {
            builder.put("required", formField.isRequired());
        }

        return builder.build();
    }

}
