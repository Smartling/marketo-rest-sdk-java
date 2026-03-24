package com.smartling.marketo.sdk.rest;

import com.google.common.base.Preconditions;
import com.smartling.marketo.sdk.rest.transport.CacheableTokenProvider;
import com.smartling.marketo.sdk.rest.transport.BasicTokenProvider;
import com.smartling.marketo.sdk.rest.transport.JaxRsHttpCommandExecutor;
import com.smartling.marketo.sdk.rest.transport.ObjectMapperProvider;
import com.smartling.marketo.sdk.rest.transport.TokenProvider;
import com.smartling.marketo.sdk.rest.transport.logging.JsonClientLoggingFilter;
import org.glassfish.jersey.client.ClientProperties;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.logging.LoggingFeature;
import org.glassfish.jersey.media.multipart.MultiPartFeature;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import java.util.logging.Logger;

import static java.util.logging.Level.INFO;
import static org.glassfish.jersey.logging.LoggingFeature.Verbosity.PAYLOAD_ANY;

public class MarketoRestClientManagerFactory {
    private static final TokenProvider TOKEN_PROVIDER = new CacheableTokenProvider(new BasicTokenProvider());

    private final Client client;
    private final RetryPolicy retryPolicy;

    private MarketoRestClientManagerFactory(Client client, RetryPolicy retryPolicy) {
        this.client = client;
        this.retryPolicy = retryPolicy;
    }

    public MarketoRestClientManager create(String identityUrl, String restUrl, String clientId, String clientSecret) {
        Preconditions.checkNotNull(identityUrl, "Identity URL is empty");
        Preconditions.checkNotNull(restUrl, "REST endpoint URL is empty");
        Preconditions.checkNotNull(clientId, "Client ID is empty");
        Preconditions.checkNotNull(clientSecret, "Client secret is empty");
        JaxRsHttpCommandExecutor executor = new JaxRsHttpCommandExecutor(identityUrl, restUrl, clientId, clientSecret, TOKEN_PROVIDER, client);
        return new MarketoRestClientManager(new RetryingHttpCommandExecutor(executor, retryPolicy));
    }

    public static final class Builder {
        private int connectionTimeout;
        private int socketReadTimeout;
        private RetryPolicy retryPolicy = RetryPolicy.NONE;
        private JsonClientLoggingFilter loggingFilter;

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

        public MarketoRestClientManagerFactory build() {
            return new MarketoRestClientManagerFactory(buildClient(loggingFilter, connectionTimeout, socketReadTimeout), retryPolicy);
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
