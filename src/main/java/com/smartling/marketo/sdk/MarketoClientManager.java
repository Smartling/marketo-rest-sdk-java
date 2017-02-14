package com.smartling.marketo.sdk;

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
}
