package com.smartling.marketo.sdk;

public interface MarketoClientManager {
    MarketoFolderClient getMarketoFolderClient();

    MarketoEmailClient getMarketoEmailClient();

    MarketoEmailV2Client getMarketoEmailV2Client();

    MarketoEmailTemplateClient getMarketoEmailTemplateClient();

    MarketoSnippetClient getMarketoSnippetClient();

    MarketoLandingPageClient getMarketoLandingPageClient();

    MarketoLandingPageTemplateClient getMarketoLandingPageTemplateClient();

    MarketoFormClient getMarketoFormClient();

    MarketoProgramClient getMarketoProgramClient();

    MarketoTokenClient getMarketoTokenClient();
}
