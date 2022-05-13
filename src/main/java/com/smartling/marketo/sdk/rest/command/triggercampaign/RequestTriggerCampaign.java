package com.smartling.marketo.sdk.rest.command.triggercampaign;

import com.google.common.collect.ImmutableMap;
import com.smartling.marketo.sdk.domain.campaign.LeadId;
import com.smartling.marketo.sdk.domain.campaign.TriggerCampaignResult;
import com.smartling.marketo.sdk.rest.command.BaseMarketoCommand;

import java.util.List;
import java.util.Map;

public class RequestTriggerCampaign extends BaseMarketoCommand<TriggerCampaignResult> {

    private final int campaignId;
    private final LeadId[] leadIds;

    public RequestTriggerCampaign(int campaignId, List<LeadId> leadIds) {
        super(TriggerCampaignResult.class);
        this.campaignId = campaignId;
        this.leadIds = (LeadId[]) leadIds.toArray();
    }

    public RequestTriggerCampaign(int campaignId, LeadId[] leadIds) {
        super(TriggerCampaignResult.class);
        this.campaignId = campaignId;
        this.leadIds = leadIds;
    }

    @Override
    public String getPath() {
        return "/rest/v1/campaigns/" + campaignId + "/trigger.json";
    }

    @Override
    public String getMethod() {
        return "POST";
    }

    @Override
    public Map<String, Object> getParameters() {
        ImmutableMap.Builder<String, Object> builder = ImmutableMap.<String, Object>builder()
                .put("leads", this.leadIds)
                .put("tokens", new Object[]{});

        return builder.build();
    }
}
