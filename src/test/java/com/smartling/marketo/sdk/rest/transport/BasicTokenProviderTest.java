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

import static com.smartling.marketo.sdk.rest.transport.BasicTokenProvider.APPROXIMATE_RESPONSE_TIME;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class BasicTokenProviderTest extends BaseTransportTest {

    @Rule
    public final WireMockRule wireMockRule = new WireMockRule(PORT);

    @Rule
    public final ExpectedException thrown = ExpectedException.none();

    private final BasicTokenProvider testedInstance = new BasicTokenProvider();

    @Before
    public void setUp() throws Exception {
        stubFor(get(urlStartingWith("/identity")).willReturn(aJsonResponse("{\"access_token\":\"accessToken\",\"expires_in\": 100000}")));
    }

    @Test
    public void shouldPassCredentialsToAuthenticate() throws Exception {

        testedInstance.authenticate(getClientConnectionData(CLIENT_ID));

        verify(getRequestedFor(urlStartingWith("/identity/oauth/token"))
                .withQueryParam("grant_type", equalTo("client_credentials"))
                .withQueryParam("client_id", equalTo(CLIENT_ID))
                .withQueryParam("client_secret", equalTo(CLIENT_SECRET)));
    }

    @Test
    public void shouldHandleUnauthorizedError() throws Exception {
        givenThat(get(path("/identity/oauth/token"))
                .willReturn(aJsonResponse("{\"error\": \"unauthorized\", \"error_description\": \"Error!\"}")
                        .withStatus(401)));

        thrown.expect(MarketoApiException.class);
        thrown.expectMessage("unauthorized: Error!");
        thrown.expect(exceptionWithCode("401"));

        testedInstance.authenticate(getClientConnectionData(CLIENT_ID));
    }

    @Test
    public void shouldSendRequestWhenTokenExpires() throws Exception {
        int validTime = 1;
        int time = APPROXIMATE_RESPONSE_TIME + validTime;

        String expiresIn = Integer.toString(time);
        givenThat(get(urlStartingWith("/identity")).willReturn(aJsonResponse(
                "{\"access_token\": \"\",\"expires_in\": " + expiresIn + "}")));

        testedInstance.authenticate(getClientConnectionData(CLIENT_ID));
        Thread.sleep(validTime * 2_000);
        testedInstance.authenticate(getClientConnectionData(CLIENT_ID));

        verify(2, getRequestedFor(urlStartingWith("/identity")));
    }

    @Test
    public void shouldReturnCorrectAccessToken() throws Exception {
        Token result = testedInstance.authenticate(getClientConnectionData(CLIENT_ID));
        assertThat(result.getAccessToken()).isEqualTo("accessToken");
    }

    @Test
    public void shouldReturnValidTokenIfExpirationTimeIsEnough() throws Exception {
        Token result = testedInstance.authenticate(getClientConnectionData(CLIENT_ID));
        assertThat(result.isValid()).isTrue();
    }

    @Test
    public void shouldReturnInvalidTokenIfExpirationIsNotEnough() throws Exception {
        givenThat(get(urlStartingWith("/identity")).willReturn(aJsonResponse(
                "{\"access_token\": \"\",\"expires_in\": 1}")));
        Token result = testedInstance.authenticate(getClientConnectionData(CLIENT_ID));
        assertThat(result.isValid()).isFalse();
    }
}