package com.smartling.marketo.sdk.rest.command;

import com.smartling.marketo.sdk.rest.Command;
import jersey.repackaged.com.google.common.collect.ImmutableMap;

import java.lang.reflect.Type;
import java.util.Map;

public class UpdateEmailContent implements Command<Void> {
    private final int id;
    private final String subject;
    private final String htmlContent;
    private final String textContent;

    public UpdateEmailContent(int id, String subject, String htmlContent, String textContent) {
        this.id = id;
        this.subject = subject;
        this.htmlContent = htmlContent;
        this.textContent = textContent;
    }

    @Override
    public String getPath() {
        return "/asset/v1/email/" + id + "/content.json";
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
        return ImmutableMap.<String, Object>builder()
                .put("subject", subject)
                .put("htmlContent", htmlContent)
                .put("textContent", textContent)
                .build();
    }
}
