package com.smartling.marketo.sdk;

import com.smartling.marketo.sdk.rest.MarketoTriggerCampaignClient;

public interface MarketoClientManager {
    MarketoFolderClient getMarketoFolderClient();

    MarketoEmailClient getMarketoEmailClient();

    MarketoEmailTemplateClient getMarketoEmailTemplateClient();

    MarketoSnippetClient getMarketoSnippetClient();

    MarketoLandingPageClient getMarketoLandingPageClient();

    MarketoLandingPageTemplateClient getMarketoLandingPageTemplateClient();

    MarketoFormClient getMarketoFormClient();

    MarketoProgramClient getMarketoProgramClient();

    MarketoTokenClient getMarketoTokenClient();

    MarketoTriggerCampaignClient getMarketoTriggerCampaignClient();
}
