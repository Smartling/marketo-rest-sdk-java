package com.smartling.marketo.sdk.rest.transport;

import com.smartling.it.marketo.sdk.BaseIntegrationTest;
import org.glassfish.jersey.client.ClientProperties;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.junit.Test;

import javax.ws.rs.ProcessingException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;

import static org.fest.assertions.api.Assertions.assertThat;

public class BasicTokenProviderIntegrationTest extends BaseIntegrationTest {

    @Test(timeout = 3 * 1000, expected = ProcessingException.class)
    public void shouldSupportTimeoutConfiguration() throws Exception {
        TokenProvider tokenProvider = new BasicTokenProvider();

        Client client = ClientBuilder.newClient();
        client.property(ClientProperties.CONNECT_TIMEOUT, 1000);
        ClientConnectionData clientConnectionData = new ClientConnectionData(client, nonRoutableHostUrl, clientId, clientSecret);

        tokenProvider.authenticate(clientConnectionData);
    }

    @Test
    public void shouldGetValidToken() throws Exception {
        TokenProvider tokenProvider = new BasicTokenProvider();

        Client client = ClientBuilder.newClient().register(JacksonFeature.class).register(ObjectMapperProvider.class);
        ClientConnectionData clientConnectionData = new ClientConnectionData(client, identityEndpoint, clientId, clientSecret);

        Token token = tokenProvider.authenticate(clientConnectionData);
        assertThat(token.isValid()).isTrue();
    }

}