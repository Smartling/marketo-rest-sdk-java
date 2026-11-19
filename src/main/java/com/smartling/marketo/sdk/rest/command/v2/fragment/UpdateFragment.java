package com.smartling.marketo.sdk.rest.command.v2.fragment;

import com.smartling.marketo.sdk.domain.v2.fragment.FragmentV2Update;

import java.util.LinkedHashMap;
import java.util.Map;

public class UpdateFragment extends BaseFragmentPostCommand<Void>
{
    private final String id;

    public UpdateFragment(String id, FragmentV2Update update)
    {
        super(Void.TYPE, body(update));
        this.id = id;
    }

    @Override
    public String getPath()
    {
        return "/asset/v2/fragment/" + id + "/update";
    }

    private static Map<String, Object> body(FragmentV2Update update)
    {
        Map<String, Object> html = new LinkedHashMap<>();
        putIfSet(html, "body", update.getHtmlBody());

        Map<String, Object> text = new LinkedHashMap<>();
        putIfSet(text, "body", update.getTextBody());

        // Send only the objects that carry a set part
        Map<String, Object> data = new LinkedHashMap<>();
        putIfNotEmpty(data, "html", html);
        putIfNotEmpty(data, "text", text);

        // The update has at least one part, so data is never empty
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("data", data);

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
