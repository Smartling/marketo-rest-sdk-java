package com.smartling.marketo.sdk.domain.v2.email;

/**
 * Actions of the Asset API v2 email state transition endpoint.
 */
public enum EmailV2StateAction
{
    APPROVE("approve"),
    CREATE_DRAFT("create_draft");

    private final String apiValue;

    EmailV2StateAction(String apiValue)
    {
        this.apiValue = apiValue;
    }

    public String getApiValue()
    {
        return apiValue;
    }
}
