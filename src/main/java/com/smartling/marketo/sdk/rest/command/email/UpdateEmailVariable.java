package com.smartling.marketo.sdk.rest.command.email;

import com.google.common.collect.ImmutableMap;
import com.smartling.marketo.sdk.domain.email.EmailVariable;
import com.smartling.marketo.sdk.rest.command.BaseMarketoCommand;

import java.util.Map;

public class UpdateEmailVariable extends BaseMarketoCommand<EmailVariable> {
    private final int emailId;
    private final EmailVariable variable;

    public UpdateEmailVariable(int emailId, EmailVariable variable) {
        super(EmailVariable.class);
        this.emailId = emailId;
        this.variable = variable;
    }

    @Override
    public String getPath() {
        return "/asset/v1/email/" + emailId + "/variable/" + variable.getName() + ".json";
    }

    @Override
    public String getMethod() {
        return "POST";
    }

    @Override
    public Map<String, Object> getParameters() {
        ImmutableMap.Builder<String, Object> builder = ImmutableMap.<String, Object>builder()
                .put("value", variable.getValue());

        if (variable.getModuleId() != null) {
            builder.put("moduleId", variable.getModuleId());
        }

        return builder.build();
    }
}
