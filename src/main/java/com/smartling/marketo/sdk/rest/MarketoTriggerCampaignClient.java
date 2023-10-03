package com.smartling.marketo.sdk.rest;

import com.smartling.marketo.sdk.MarketoApiException;
import com.smartling.marketo.sdk.domain.campaign.TriggerCampaignRequest;
import com.smartling.marketo.sdk.domain.campaign.TriggerCampaignResult;
import com.smartling.marketo.sdk.rest.command.triggercampaign.ActivateSmartCampaign;
import com.smartling.marketo.sdk.rest.command.triggercampaign.RequestTriggerCampaign;

import java.util.List;

public class MarketoTriggerCampaignClient {

    private final HttpCommandExecutor httpCommandExecutor;

    MarketoTriggerCampaignClient(HttpCommandExecutor httpCommandExecutor) {
        this.httpCommandExecutor = httpCommandExecutor;
    }

    public TriggerCampaignResult requestTriggerCampaign(int campaignId,
            TriggerCampaignRequest.LeadId[] leadIds)
            throws MarketoApiException {
        return this.requestTriggerCampaign(campaignId, leadIds,
                new TriggerCampaignRequest.Token[]{});
    }

    public TriggerCampaignResult requestTriggerCampaign(int campaignId,
            TriggerCampaignRequest.LeadId[] leadIds, TriggerCampaignRequest.Token[] tokens)
            throws MarketoApiException {
        List<TriggerCampaignResult> triggerCampaignResults = httpCommandExecutor.execute(
                new RequestTriggerCampaign(campaignId, leadIds, tokens));
        return triggerCampaignResults.get(0);
    }

    public TriggerCampaignResult activateSmartCampaign(int campaignId)
            throws MarketoApiException {
        List<TriggerCampaignResult> triggerCampaignResults = httpCommandExecutor.execute(
                new ActivateSmartCampaign(campaignId));
        return triggerCampaignResults.get(0);
    }


}
