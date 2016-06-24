package com.smartling.marketo.sdk.rest.command.form;

import com.smartling.marketo.sdk.domain.Asset.Status;
import com.smartling.marketo.sdk.domain.form.FormField;
import com.smartling.marketo.sdk.rest.command.BaseGetCommand;

import java.util.Collections;
import java.util.Map;

public class GetFormFields extends BaseGetCommand<FormField> {
    private final int formId;
    private Status status;

    public GetFormFields(int formId, Status status) {
        super(FormField.class);
        this.formId = formId;
        this.status = status;
    }

    @Override
    public String getPath() {
        return "/asset/v1/form/" + formId + "/fields.json";
    }

    @Override
    public Map<String, Object> getParameters() {
        return status!=null ? Collections.singletonMap("status", status) : super.getParameters();
    }
}
