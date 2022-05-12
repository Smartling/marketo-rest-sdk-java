package com.smartling.marketo.sdk.rest.command.email;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.ImmutableMap;
import com.smartling.marketo.sdk.domain.email.Email;
import com.smartling.marketo.sdk.rest.Command;

import java.lang.reflect.Type;
import java.util.Map;

public class UpdateEmailContent implements Command<Void> {

    private final static ObjectMapper objectMapper = new ObjectMapper();

    private final Email email;

    public UpdateEmailContent(Email email) {
        this.email = email;
    }

    @Override
    public String getPath() {
        return "/asset/v1/email/" + email.getId() + "/content.json";
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
                .put("subject", getWrappedValue(email.getSubjectType(), email.getSubject()))
                .put("fromName", getWrappedValue(email.getFromNameType(), email.getFromName()));
        return builder.build();
    }

    private String getWrappedValue(String type, String value) {
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("type", type);
        objectNode.put("value", value);
        return objectNode.toString();
    }
}
