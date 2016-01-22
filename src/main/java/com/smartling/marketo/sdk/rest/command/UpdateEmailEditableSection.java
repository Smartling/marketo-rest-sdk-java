package com.smartling.marketo.sdk.rest.command;

import com.smartling.marketo.sdk.EmailTextContentItem;
import com.smartling.marketo.sdk.rest.Command;

import java.lang.reflect.Type;
import java.util.HashMap;
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
        Map<String, Object> map = new HashMap<>();

        map.put("type", contentItem.getContentType());

        for (EmailTextContentItem.Value value : contentItem.getValue()) {
            if (value.getType().equals("HTML")) {
                map.put("value", value.getValue());
            } else if (value.getType().equals("Text")) {
                map.put("textValue", value.getValue());
            }
        }

        return map;
    }
}
