package com.smartling.marketo.sdk.rest.command;

import com.google.common.collect.ImmutableMap;
import com.smartling.marketo.sdk.EmailTextContentItem;
import com.smartling.marketo.sdk.rest.Command;

import java.lang.reflect.Type;
import java.util.Map;

public class UpdateEmailEditableSection implements Command<Void> {
    private final int emailId;
    private final EmailTextContentItem contentItem;

    public UpdateEmailEditableSection(int emailId, EmailTextContentItem contentItem) {
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
        ImmutableMap.Builder<String, Object> builder = ImmutableMap.<String, Object>builder();

        builder.put("type", contentItem.getContentType());

        contentItem.getValue().stream().filter(value -> value.getValue() != null).forEach(value -> {
            if (value.getType().equals("HTML")) {
                builder.put("value", value.getValue());
            } else if (value.getType().equals("Text")) {
                builder.put("textValue", value.getValue());
            }
        });

        return builder.build();
    }
}
