package com.smartling.marketo.sdk.rest;

import com.google.common.base.Preconditions;
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
import com.smartling.marketo.sdk.rest.transport.*;
import com.smartling.marketo.sdk.rest.transport.logging.JsonClientLoggingFilter;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import org.glassfish.jersey.client.ClientProperties;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.logging.LoggingFeature;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import java.util.logging.Logger;
import static java.util.logging.Level.INFO;
import static org.glassfish.jersey.logging.LoggingFeature.Verbosity.PAYLOAD_ANY;

public class MarketoRestClientManager implements MarketoClientManager {
    private static final TokenProvider tokenProvider = new CacheableTokenProvider(new BasicTokenProvider());

    private final HttpCommandExecutor httpCommandExecutor;

    private MarketoRestClientManager(HttpCommandExecutor httpCommandExecutor) {
        this.httpCommandExecutor = httpCommandExecutor;
    }

    public static Builder create() {
        return new Builder();
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

    public static final class Builder {
        private int connectionTimeout;
        private int socketReadTimeout;
        private RetryPolicy retryPolicy = RetryPolicy.NONE;
        private JsonClientLoggingFilter loggingFilter;
        private Client client;

        private Builder() {
        }

        public Builder withLoggingFilter(JsonClientLoggingFilter loggingFilter) {
            this.loggingFilter = loggingFilter;
            return this;
        }

        public Builder withConnectionTimeout(int connectionTimeout) {
            this.connectionTimeout = connectionTimeout;
            return this;
        }

        public Builder withSocketReadTimeout(int socketReadTimeout) {
            this.socketReadTimeout = socketReadTimeout;
            return this;
        }

        public Builder withRetryPolicy(RetryPolicy retryPolicy) {
            this.retryPolicy = Preconditions.checkNotNull(retryPolicy, "Retry Policy can not be null");
            return this;
        }

        public MarketoRestClientManager withCredentials(String identityUrl, String restUrl, String clientId, String clientSecret) {
            Preconditions.checkNotNull(identityUrl, "Identity URL is empty");
            Preconditions.checkNotNull(restUrl, "REST endpoint URL is empty");
            Preconditions.checkNotNull(clientId, "Client ID is empty");
            Preconditions.checkNotNull(clientSecret, "Client secret is empty");
            JaxRsHttpCommandExecutor executor = new JaxRsHttpCommandExecutor(
                    identityUrl, restUrl, clientId, clientSecret, tokenProvider, getOrBuildClient());
            return new MarketoRestClientManager(new RetryingHttpCommandExecutor(executor, retryPolicy));
        }

        private Client getOrBuildClient() {
            if (client == null) {
                client = buildClient(loggingFilter, connectionTimeout, socketReadTimeout);
            }
            return client;
        }

        private static Client buildClient(JsonClientLoggingFilter loggingFilter, int connectionTimeout, int socketReadTimeout) {
            Client client = ClientBuilder.newClient()
                    .register(JacksonFeature.class)
                    .register(ObjectMapperProvider.class)
                    .register(MultiPartFeature.class);

            if (loggingFilter == null) {
                client = client.register(new LoggingFeature(Logger.getLogger("PayloadLogger"), INFO, PAYLOAD_ANY, null));
            } else {
                client = client.register(loggingFilter);
            }

            client.property(ClientProperties.CONNECT_TIMEOUT, connectionTimeout);
            client.property(ClientProperties.READ_TIMEOUT, socketReadTimeout);

            return client;
        }
    }
}
