package com.smartling.marketo.sdk.rest;

import com.smartling.marketo.sdk.MarketoClientManager;
import com.smartling.marketo.sdk.MarketoEmailClient;
import com.smartling.marketo.sdk.MarketoEmailTemplateClient;
import com.smartling.marketo.sdk.MarketoFolderClient;
import com.smartling.marketo.sdk.MarketoFormClient;
import com.smartling.marketo.sdk.MarketoLandingPageClient;
import com.smartling.marketo.sdk.MarketoLandingPageTemplateClient;
import com.smartling.marketo.sdk.MarketoProgramClient;
import com.smartling.marketo.sdk.MarketoSnippetClient;
import com.smartling.marketo.sdk.MarketoTokenClient;

public class MarketoRestClientManager implements MarketoClientManager {

    private final HttpCommandExecutor httpCommandExecutor;

    MarketoRestClientManager(HttpCommandExecutor httpCommandExecutor) {
        this.httpCommandExecutor = httpCommandExecutor;
    }

    @Override
    public MarketoFolderClient getMarketoFolderClient() {
        return new MarketoFolderRestClient(httpCommandExecutor);
    }

    @Override
    public MarketoEmailClient getMarketoEmailClient() {
        return new MarketoEmailRestClient(httpCommandExecutor);
    }

    public MarketoEmailTemplateClient getMarketoEmailTemplateClient() {
        return new MarketoEmailTemplateRestClient(httpCommandExecutor);
    }

    @Override
    public MarketoSnippetClient getMarketoSnippetClient() {
        return new MarketoSnippetRestClient(httpCommandExecutor);
    }

    @Override
    public MarketoLandingPageClient getMarketoLandingPageClient() {
        return new MarketoLandingPageRestClient(httpCommandExecutor);
    }

    @Override
    public MarketoLandingPageTemplateClient getMarketoLandingPageTemplateClient() {
        return new MarketoLandingPageTemplateRestClient(httpCommandExecutor);
    }

    @Override
    public MarketoFormClient getMarketoFormClient() {
        return new MarketoFormRestClient(httpCommandExecutor);
    }

    @Override
    public MarketoProgramClient getMarketoProgramClient() {
        return new MarketoProgramRestClient(httpCommandExecutor);
    }

    @Override
    public MarketoTokenClient getMarketoTokenClient() {
        return new MarketoTokenRestClient(httpCommandExecutor);
    }
}
