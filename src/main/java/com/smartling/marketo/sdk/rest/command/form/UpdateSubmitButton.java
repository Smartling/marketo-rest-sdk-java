package com.smartling.marketo.sdk.rest.command.form;

import com.google.common.collect.ImmutableMap;
import com.smartling.marketo.sdk.domain.form.Form;
import com.smartling.marketo.sdk.rest.command.BaseMarketoCommand;

import java.util.Map;

public class UpdateSubmitButton extends BaseMarketoCommand<Form> {
    private final int formId;
    private final String label;
    private final String waitingLabel;

    public UpdateSubmitButton(int formId, String label, String waitingLabel) {
        super(Form.class);
        this.formId = formId;
        this.label = label;
        this.waitingLabel = waitingLabel;
    }

    @Override
    public String getPath() {
        return "/asset/v1/form/" + formId + "/submitButton.json";
    }

    @Override
    public String getMethod() {
        return "POST";
    }

    @Override
    public Map<String, Object> getParameters() {
        return ImmutableMap.of("label", label, "waitingLabel", waitingLabel);
    }
}
