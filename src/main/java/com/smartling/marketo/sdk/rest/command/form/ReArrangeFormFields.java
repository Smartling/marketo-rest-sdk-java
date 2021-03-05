package com.smartling.marketo.sdk.rest.command.form;

import com.smartling.marketo.sdk.domain.form.FormField;
import com.smartling.marketo.sdk.rest.command.BaseMarketoCommand;

import java.util.Collections;
import java.util.Map;

public class ReArrangeFormFields extends BaseMarketoCommand<FormField> {
    private final int formId;
    private final UpdateFieldPositionsList positions;

    public ReArrangeFormFields(int formId, UpdateFieldPositionsList positions) {
        super(FormField.class);
        this.formId = formId;
        this.positions = positions;
    }

    @Override
    public String getPath() {
        return "/asset/v1/form/" + formId + "/reArrange.json";
    }

    @Override
    public String getMethod() {
        return "POST";
    }

    @Override
    public Map<String, Object> getParameters() {
        return Collections.singletonMap("positions", positions);
    }
}
