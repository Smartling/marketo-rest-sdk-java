package com.smartling.marketo.sdk.rest.command;

import com.google.common.collect.ImmutableMap;
import com.smartling.marketo.sdk.EmailContentItem;
import com.smartling.marketo.sdk.rest.Command;

import java.lang.reflect.Type;
import java.util.Map;


public class UpdateEmailEditableSection implements Command<Void> {
    private final int emailId;
    private final EmailContentItem contentItem;

    public UpdateEmailEditableSection(int emailId, EmailContentItem contentItem) {
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
        ImmutableMap.Builder<String, Object> builder = ImmutableMap.<String, Object>builder()
                .put("type", contentItem.getContentType());

        for (EmailContentItem.Value value : contentItem.getValue()) {
            if (value.getType().equals("HTML")) {
                builder.put("value", value.getValue());
            } else if (value.getType().equals("Text")) {
                builder.put("textValue", value.getValue());
            }
        }


        return builder.build();
    }
}
