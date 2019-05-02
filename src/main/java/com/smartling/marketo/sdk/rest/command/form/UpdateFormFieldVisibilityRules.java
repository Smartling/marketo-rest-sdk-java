package com.smartling.marketo.sdk.rest.command.form;

import com.smartling.marketo.sdk.domain.form.VisibilityRules;
import com.smartling.marketo.sdk.domain.form.VisibilityRulesParameter;
import com.smartling.marketo.sdk.rest.command.BaseMarketoCommand;

import java.util.Collections;
import java.util.Map;

public class UpdateFormFieldVisibilityRules extends BaseMarketoCommand<VisibilityRules> {
    private final int formId;
    private final String formField;
    private final VisibilityRulesParameter visibilityRule;

    public UpdateFormFieldVisibilityRules(int formId, String formField, VisibilityRulesParameter visibilityRule) {
        super(VisibilityRules.class);
        this.formId = formId;
        this.formField = formField;
        this.visibilityRule = visibilityRule;
    }

    @Override
    public String getPath() {
        return "/asset/v1/form/" + formId + "/field/" + formField + "/visibility.json";
    }

    @Override
    public String getMethod() {
        return "POST";
    }

    @Override
    public Map<String, Object> getParameters() {
        return Collections.singletonMap("visibilityRule", visibilityRule);
    }

}
