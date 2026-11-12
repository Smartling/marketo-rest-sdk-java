package com.smartling.marketo.sdk.rest.command.v2.email;

import com.google.common.reflect.TypeToken;
import com.smartling.marketo.sdk.domain.v2.email.EmailV2;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class CloneEmail extends BaseEmailPostCommand<List<EmailV2>>
{
    public CloneEmail(String id, String name)
    {
        super(new TypeToken<List<EmailV2>>() {}.getType(), body(id, name));
    }

    @Override
    public String getPath()
    {
        return "/asset/v2/email/clone";
    }

    private static Map<String, Object> body(String id, String name)
    {
        // The copy gets only the new name
        Map<String, Object> newAsset = new LinkedHashMap<>();
        newAsset.put("name", name);

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("assetId", id);
        body.put("newAsset", newAsset);

        return body;
    }
}
