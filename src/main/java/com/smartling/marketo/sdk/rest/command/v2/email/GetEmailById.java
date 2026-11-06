package com.smartling.marketo.sdk.rest.command.v2.email;

import com.google.common.reflect.TypeToken;
import com.smartling.marketo.sdk.domain.v2.email.EmailV2;

import java.util.List;

public class GetEmailById extends BaseEmailGetCommand<List<EmailV2>>
{
    private final String id;

    public GetEmailById(String id)
    {
        super(new TypeToken<List<EmailV2>>() {}.getType());
        this.id = id;
    }

    @Override
    public String getPath()
    {
        return "/asset/v2/email/" + id;
    }
}
