package com.smartling.marketo.sdk.rest;

import com.smartling.marketo.sdk.MarketoApiException;
import com.smartling.marketo.sdk.domain.campaign.LeadId;
import com.smartling.marketo.sdk.domain.campaign.TriggerCampaignResult;
import com.smartling.marketo.sdk.rest.command.triggercampaign.RequestTriggerCampaign;

import java.util.List;

public class MarketoTriggerCampaignClient {

    private final HttpCommandExecutor httpCommandExecutor;

    MarketoTriggerCampaignClient(HttpCommandExecutor httpCommandExecutor) {
        this.httpCommandExecutor = httpCommandExecutor;
    }

    public TriggerCampaignResult requestTriggerCampaign(int campaignId, List<LeadId> leadIds)
            throws MarketoApiException {
        List<TriggerCampaignResult> triggerCampaignResults = httpCommandExecutor.execute(
                new RequestTriggerCampaign(campaignId, leadIds));
        return triggerCampaignResults.get(0);
    }
}
