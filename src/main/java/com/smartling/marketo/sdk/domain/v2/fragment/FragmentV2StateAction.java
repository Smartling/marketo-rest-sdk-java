package com.smartling.marketo.sdk.domain.v2.fragment;

/**
 * Actions of the Asset API v2 fragment state transition endpoint.
 */
public enum FragmentV2StateAction
{
    APPROVE("approve"),
    CREATE_DRAFT("create_draft");

    private final String apiValue;

    FragmentV2StateAction(String apiValue)
    {
        this.apiValue = apiValue;
    }

    public String getApiValue()
    {
        return apiValue;
    }
}
