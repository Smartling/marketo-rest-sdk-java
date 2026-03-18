package com.smartling.marketo.sdk;

import java.io.Closeable;

public interface MarketoClientManager extends Closeable {
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
