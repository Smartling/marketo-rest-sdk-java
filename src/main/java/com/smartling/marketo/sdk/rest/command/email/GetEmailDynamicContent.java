package com.smartling.marketo.sdk.rest.command.email;

import com.smartling.marketo.sdk.domain.email.DynamicContent;
import com.smartling.marketo.sdk.rest.command.BaseGetCommand;

public class GetEmailDynamicContent extends BaseGetCommand<DynamicContent>
{
    private final int emailId;
    private final String dynamicContentId;

    public GetEmailDynamicContent(int emailId, String dynamicContentId)
    {
        super(DynamicContent.class);
        this.emailId = emailId;
        this.dynamicContentId = dynamicContentId;
    }

    @Override
    public String getPath()
    {
        return "/asset/v1/email/" + emailId + "/dynamicContent/" + dynamicContentId + ".json";
    }
}
