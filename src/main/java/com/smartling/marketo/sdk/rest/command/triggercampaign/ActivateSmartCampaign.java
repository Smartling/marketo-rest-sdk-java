package com.smartling.marketo.sdk.rest.command.triggercampaign;

import com.smartling.marketo.sdk.domain.campaign.TriggerCampaignResult;
import com.smartling.marketo.sdk.rest.command.BaseMarketoCommand;

public class ActivateSmartCampaign extends BaseMarketoCommand<TriggerCampaignResult> {
    private final int campaignId;

    public ActivateSmartCampaign(int campaignId) {
        super(TriggerCampaignResult.class);
        this.campaignId = campaignId;
    }

    @Override
    public String getPath() {
        return "/asset/v1/smartCampaign/" + campaignId + "/activate.json";
    }

    @Override
    public String getMethod() {
        return "POST_BODY";
    }
}
