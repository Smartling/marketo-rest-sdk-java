package com.smartling.marketo.sdk;

public interface MarketoClientManager {
    MarketoFolderClient getMarketoFolderClient();
    MarketoEmailClient getMarketoEmailClient();
    MarketoSnippetClient getMarketoSnippetClient();
    MarketoLandingPageClient getMarketoLandingPageClient();
}
