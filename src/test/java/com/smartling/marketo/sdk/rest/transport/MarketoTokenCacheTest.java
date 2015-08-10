package com.smartling.marketo.sdk.rest.transport;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.smartling.marketo.sdk.MarketoApiException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.givenThat;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.verify;

import static com.smartling.marketo.sdk.rest.transport.MarketoTokenCache.APPROXIMATE_RESPONSE_TIME;

@RunWith(MockitoJUnitRunner.class)
public class MarketoTokenCacheTest extends BaseTransportTest {

    @Rule
    public final WireMockRule wireMockRule = new WireMockRule(PORT);

    @Rule
    public final ExpectedException thrown = ExpectedException.none();

    private final MarketoTokenCache tokenCache = new MarketoTokenCache();

    @Before
    public void setUp() throws Exception {
        stubFor(get(urlStartingWith("/identity")).willReturn(aJsonResponse("{\"access_token\": \"\",\"expires_in\": 100000}")));
    }

    @Test
    public void shouldSendOnlyOneRequestForTheSameClient() throws Exception {

        tokenCache.getAccessToken(getClientConnectionData());
        tokenCache.getAccessToken(getClientConnectionData());

        verify(1, getRequestedFor(urlStartingWith("/identity")));
    }

    @Test
    public void shouldSendSeparateRequestsForDifferentClients() throws Exception {

        tokenCache.getAccessToken(getClientConnectionData());
        tokenCache.getAccessToken(getClientConnectionData("another_client"));

        verify(2, getRequestedFor(urlStartingWith("/identity")));
    }


    @Test
    public void shouldSendRequestWhenTokenExpires() throws Exception {
        int validTime = 1;
        int time = APPROXIMATE_RESPONSE_TIME + validTime;

        String expiresIn = Integer.toString(time);
        givenThat(get(urlStartingWith("/identity")).willReturn(aJsonResponse(
                "{\"access_token\": \"\",\"expires_in\": " + expiresIn + "}")));

        tokenCache.getAccessToken(getClientConnectionData());
        Thread.sleep(validTime * 2_000);
        tokenCache.getAccessToken(getClientConnectionData());

        verify(2, getRequestedFor(urlStartingWith("/identity")));
    }

    @Test
    public void shouldHandleUnauthorizedError() throws Exception {
        givenThat(get(path("/identity/oauth/token"))
                .willReturn(aJsonResponse("{\"error\": \"unauthorized\", \"error_description\": \"Error!\"}").withStatus(401)));

        thrown.expect(MarketoApiException.class);
        thrown.expectMessage("unauthorized: Error!");
        thrown.expect(exceptionWithCode("401"));


        tokenCache.getAccessToken(getClientConnectionData());
    }

}