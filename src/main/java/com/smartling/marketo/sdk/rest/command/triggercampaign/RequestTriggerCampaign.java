package com.smartling.marketo.sdk.rest.command.triggercampaign;

import com.google.common.collect.ImmutableMap;
import com.smartling.marketo.sdk.domain.campaign.TriggerCampaignRequest;
import com.smartling.marketo.sdk.domain.campaign.TriggerCampaignResult;
import com.smartling.marketo.sdk.rest.command.BaseMarketoCommand;

import java.util.Map;

public class RequestTriggerCampaign extends BaseMarketoCommand<TriggerCampaignResult> {

    private final int campaignId;
    private final TriggerCampaignRequest triggerCampaignRequest;

    public RequestTriggerCampaign(int campaignId, TriggerCampaignRequest.LeadId[] leadIds,
            TriggerCampaignRequest.Token[] tokens) {
        super(TriggerCampaignResult.class);
        this.campaignId = campaignId;
        this.triggerCampaignRequest = new TriggerCampaignRequest(leadIds, tokens);
    }

    @Override
    public String getPath() {
        return "/v1/campaigns/" + campaignId + "/trigger.json";
    }

    @Override
    public String getMethod() {
        return "POST_BODY";
    }

    @Override
    public Map<String, Object> getParameters() {
        ImmutableMap.Builder<String, Object> builder = ImmutableMap.<String, Object>builder()
                .put("input", this.triggerCampaignRequest);
        return builder.build();
    }
}
