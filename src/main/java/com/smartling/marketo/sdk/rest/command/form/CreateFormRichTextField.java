package com.smartling.marketo.sdk.rest.command.form;

import com.smartling.marketo.sdk.domain.form.FormField;
import com.smartling.marketo.sdk.rest.command.BaseMarketoCommand;

import java.util.Collections;
import java.util.Map;

public class CreateFormRichTextField extends BaseMarketoCommand<FormField> {
    private final int formId;
    private final String text;

    public CreateFormRichTextField(int formId, String text) {
        super(FormField.class);
        this.formId = formId;
        this.text = text;
    }

    @Override
    public String getPath() {
        return "/asset/v1/form/" + formId + "/richText.json";
    }

    @Override
    public String getMethod() {
        return "MULTIPART";
    }

    @Override
    public Map<String, Object> getParameters() {
        return Collections.singletonMap("text", text);
    }
}
