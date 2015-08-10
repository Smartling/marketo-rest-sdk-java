package com.smartling.marketo.sdk.rest.transport;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.smartling.marketo.sdk.MarketoApiException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.givenThat;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.verify;

@RunWith(MockitoJUnitRunner.class)
public class AuthManagerTest  extends BaseTransportTest{

    @Rule
    public final WireMockRule wireMockRule = new WireMockRule(PORT);

    @Rule
    public final ExpectedException thrown = ExpectedException.none();

    private final AuthManager AuthManager = new AuthManager();

    @Before
    public void setUp() throws Exception {
        stubFor(get(urlStartingWith("/identity")).willReturn(aJsonResponse("{\"access_token\": \"\",\"expires_in\": 100000}")));
    }

    @Test
    public void shouldPassCredentialsToAuthenticate() throws Exception {

        AuthManager.authenticate(getClientConnectionData());

        verify(getRequestedFor(urlStartingWith("/identity/oauth/token"))
                .withQueryParam("grant_type", equalTo("client_credentials"))
                .withQueryParam("client_id", equalTo(CLIENT_ID))
                .withQueryParam("client_secret", equalTo(CLIENT_SECRET)));
    }

    @Test
    public void shouldHandleUnauthorizedError() throws Exception {
        givenThat(get(path("/identity/oauth/token"))
                .willReturn(aJsonResponse("{\"error\": \"unauthorized\", \"error_description\": \"Error!\"}").withStatus(401)));

        thrown.expect(MarketoApiException.class);
        thrown.expectMessage("unauthorized: Error!");
        thrown.expect(exceptionWithCode("401"));


        AuthManager.authenticate(getClientConnectionData());
    }

    @Test
    public void shouldExpireToken() throws Exception {
        givenThat(get(urlStartingWith("/identity")).willReturn(aJsonResponse(
                "{\"access_token\": \"\",\"expires_in\": 1}")));

        AuthManager.authenticate(getClientConnectionData());
        Thread.sleep(1000);
        AuthManager.authenticate(getClientConnectionData());

        verify(2, getRequestedFor(urlStartingWith("/identity")));
    }
}