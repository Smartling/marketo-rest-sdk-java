package com.smartling.marketo.sdk.rest.command.v2.email;

import com.smartling.marketo.sdk.domain.v2.email.EmailV2Update;

import java.util.LinkedHashMap;
import java.util.Map;

public class UpdateEmail extends BaseEmailPostCommand<Void>
{
    private final String id;

    public UpdateEmail(String id, EmailV2Update update)
    {
        super(Void.TYPE, body(update));
        this.id = id;
    }

    @Override
    public String getPath()
    {
        return "/asset/v2/email/" + id + "/update";
    }

    private static Map<String, Object> body(EmailV2Update update)
    {
        Map<String, Object> headers = new LinkedHashMap<>();
        putIfSet(headers, "subject", update.getSubject());
        putIfSet(headers, "fromName", update.getFromName());

        Map<String, Object> html = new LinkedHashMap<>();
        putIfSet(html, "body", update.getHtmlBody());

        Map<String, Object> text = new LinkedHashMap<>();
        putIfSet(text, "body", update.getTextBody());

        Map<String, Object> data = new LinkedHashMap<>();
        putIfNotEmpty(data, "html", html);
        putIfNotEmpty(data, "text", text);

        // Send only the objects that carry a set part
        Map<String, Object> body = new LinkedHashMap<>();
        putIfNotEmpty(body, "headers", headers);
        putIfNotEmpty(body, "data", data);

        return body;
    }

    private static void putIfSet(Map<String, Object> target, String key, String value)
    {
        if (value != null)
        {
            target.put(key, value);
        }
    }

    private static void putIfNotEmpty(Map<String, Object> target, String key, Map<String, Object> value)
    {
        if (!value.isEmpty())
        {
            target.put(key, value);
        }
    }
}
