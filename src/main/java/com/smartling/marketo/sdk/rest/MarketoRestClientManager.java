package com.smartling.marketo.sdk.rest;

import com.google.common.base.Preconditions;
import com.smartling.marketo.sdk.MarketoClientManager;
import com.smartling.marketo.sdk.MarketoEmailClient;
import com.smartling.marketo.sdk.MarketoFolderClient;
import com.smartling.marketo.sdk.MarketoFormClient;
import com.smartling.marketo.sdk.MarketoLandingPageClient;
import com.smartling.marketo.sdk.MarketoProgramClient;
import com.smartling.marketo.sdk.MarketoSnippetClient;
import com.smartling.marketo.sdk.rest.transport.BasicTokenProvider;
import com.smartling.marketo.sdk.rest.transport.CacheableTokenProvider;
import com.smartling.marketo.sdk.rest.transport.HttpCommandExecutor;
import com.smartling.marketo.sdk.rest.transport.TokenProvider;

public class MarketoRestClientManager implements MarketoClientManager {
    private static final TokenProvider tokenProvider = new CacheableTokenProvider(new BasicTokenProvider());

    private final HttpCommandExecutor httpCommandExecutor;

    public MarketoRestClientManager(HttpCommandExecutor httpCommandExecutor) {
        this.httpCommandExecutor = httpCommandExecutor;
    }

    public static Builder create(String identityUrl, String restUrl) {
        Preconditions.checkNotNull(identityUrl, "Identity URL is empty");
        Preconditions.checkNotNull(restUrl, "REST endpoint URL is empty");

        return new Builder(identityUrl, restUrl);
    }

    @Override
    public MarketoFolderClient getMarketoFolderClient() {
        return new MarketoFolderRestClient(httpCommandExecutor);
    }

    @Override
    public MarketoEmailClient getMarketoEmailClient() {
        return new MarketoEmailRestClient(httpCommandExecutor);
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
    public MarketoFormClient getMarketoFormClient() {
        return new MarketoFormRestClient(httpCommandExecutor);
    }

    @Override
    public MarketoProgramClient getMarketoProgramClient() {
        return new MarketoProgramRestClient(httpCommandExecutor);
    }

    public final static class Builder {
        private final String identityUrl;
        private final String restUrl;
        private int connectionTimeout;
        private int socketReadTimeout;

        private Builder(String identityUrl, String restUrl) {
            this.identityUrl = identityUrl;
            this.restUrl = restUrl;
        }

        public MarketoRestClientManager withCredentials(String clientId, String clientSecret) {
            Preconditions.checkNotNull(clientId, "Client ID is empty");
            Preconditions.checkNotNull(clientSecret, "Client secret is empty");

            HttpCommandExecutor executor = new HttpCommandExecutor(identityUrl, restUrl, clientId, clientSecret, tokenProvider);
            executor.setConnectionTimeout(connectionTimeout);
            executor.setSocketReadTimeout(socketReadTimeout);
            return new MarketoRestClientManager(executor);
        }

        public Builder withConnectionTimeout(int connectionTimeout) {
            this.connectionTimeout = connectionTimeout;
            return this;
        }

        public Builder withSocketReadTimeout(int socketReadTimeout) {
            this.socketReadTimeout = socketReadTimeout;
            return this;
        }
    }
}
