package com.smartling.marketo.sdk.rest.command.email;

import com.google.common.collect.ImmutableMap;
import com.smartling.marketo.sdk.domain.email.EmailVariable;
import com.smartling.marketo.sdk.rest.Command;

import java.lang.reflect.Type;
import java.util.Map;

public class UpdateEmailVariable implements Command<Void> {

    private Integer emailId;
    private EmailVariable var;

    public UpdateEmailVariable(Integer emailId, EmailVariable var) {
        this.emailId = emailId;
        this.var = var;
    }

    @Override
    public String getPath() {
        return "/asset/v1/email/" + emailId + "/variable/" + var.getName() + ".json";
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
        ImmutableMap.Builder<String, Object> builder = ImmutableMap.<String, Object>builder()
                .put("value", var.getValue());

        if (var.isModuleScope()) {
            builder.put("moduleId", var.getModuleId());
        }
        return builder.build();
    }
}
