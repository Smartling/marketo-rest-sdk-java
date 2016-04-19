package com.smartling.marketo.sdk.rest.command.email;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;
import com.smartling.marketo.sdk.domain.email.EmailTextContentItem;
import com.smartling.marketo.sdk.domain.email.EmailTextContentItem.Value;
import com.smartling.marketo.sdk.rest.Command;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.stream.Collectors;

public class UpdateEmailEditableSection implements Command<Void> {
    private final int emailId;
    private final EmailTextContentItem contentItem;

    private static Map<String, String> mapItemType = ImmutableMap.of("HTML", "value", "Text", "textValue");

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
        Builder<String, Object> builder = ImmutableMap.<String, Object>builder();

        builder.put("type", contentItem.getContentType());

        Map<String, String> contentMap = contentItem.getValue().stream().filter(value -> value.getValue() != null)
                .collect(Collectors.toMap(key -> mapItemType.get(key.getType()), Value::getValue));
        builder.putAll(contentMap);

        return builder.build();
    }
}
