package com.smartling.marketo.sdk.rest.command.email;

import com.google.common.collect.ImmutableMap;
import com.smartling.marketo.sdk.rest.Command;

import java.lang.reflect.Type;
import java.util.Map;

public class SendSample implements Command<Void> {
    private int emailId;
    private final String emailAddress;
    private boolean textOnly;

    public SendSample(int emailId, String emailAddress, boolean textOnly) {
        this.emailId = emailId;
        this.emailAddress = emailAddress;
        this.textOnly = textOnly;
    }

    @Override
    public String getPath() {
        return "/asset/v1/email/" + emailId + "/sendSample.json";
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
                .put("emailAddress", emailAddress)
                .put("textOnly", textOnly);
        return builder.build();
    }
}
