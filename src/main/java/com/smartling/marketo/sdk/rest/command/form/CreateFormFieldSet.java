package com.smartling.marketo.sdk.rest.command.form;

import com.google.common.collect.ImmutableMap;
import com.smartling.marketo.sdk.domain.form.FormField;
import com.smartling.marketo.sdk.rest.command.BaseMarketoCommand;

import java.util.Map;

public class CreateFormFieldSet extends BaseMarketoCommand<FormField>  {

    private final int formId;
    private final String label;

    public CreateFormFieldSet(int formId, String label) {
        super(FormField.class);
        this.formId = formId;
        this.label = label;
    }

    @Override
    public String getPath() {
        return "/asset/v1/form/" + formId + "/fieldSet.json";
    }

    @Override
    public String getMethod() {
        return "POST";
    }

    @Override
    public Map<String, Object> getParameters() {
        ImmutableMap.Builder<String, Object> builder = ImmutableMap.builder();
        builder.put("label", label);
        return builder.build();
    }

}
