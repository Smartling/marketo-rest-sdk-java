package com.smartling.marketo.sdk.rest.command.email;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;
import com.smartling.marketo.sdk.domain.email.EmailSnippetContentItem;
import com.smartling.marketo.sdk.rest.Command;

import java.lang.reflect.Type;
import java.util.Map;

public class UpdateEmailSnippetContent implements Command<Void> {
    private final int emailId;
    private final EmailSnippetContentItem contentItem;

    public UpdateEmailSnippetContent(int emailId, EmailSnippetContentItem contentItem) {
        this.emailId = emailId;
        this.contentItem = contentItem;
    }

    @Override
    public String getPath() {
        return "/asset/v1/email/" + emailId + "/content/" + contentItem.getHtmlId() + ".json";
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
        Builder<String, Object> builder = ImmutableMap.builder();

        builder.put("type", contentItem.getContentType());
        builder.put("value", contentItem.getValue());

        return builder.build();
    }
}
