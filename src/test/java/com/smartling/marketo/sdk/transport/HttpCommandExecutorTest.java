package com.smartling.marketo.sdk.transport;

import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;
import com.github.tomakehurst.wiremock.client.UrlMatchingStrategy;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.smartling.marketo.sdk.Command;
import com.smartling.marketo.sdk.MarketoApiException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Collections;
import java.util.Random;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.givenThat;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlMatching;
import static com.github.tomakehurst.wiremock.client.WireMock.verify;
import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class HttpCommandExecutorTest {
    private static final int PORT = 10000 + new Random().nextInt(9999);

    private static final String BASE_URL = "http://localhost:" + PORT;
    private static final String IDENTITY_URL = BASE_URL + "/identity";
    private static final String REST_URL = BASE_URL + "/rest";

    private static final String CLIENT_ID = "the_client_id";
    private static final String CLIENT_SECRET = "a_secret_key";

    @Rule
    public final WireMockRule wireMockRule = new WireMockRule(PORT);

    @Rule
    public final ExpectedException thrown = ExpectedException.none();

    @Mock
    private Command<Data> command;

    private final HttpCommandExecutor testedInstance = new HttpCommandExecutor(IDENTITY_URL, REST_URL, CLIENT_ID, CLIENT_SECRET);

    @Before
    public void setUp() throws Exception {
        stubFor(get(urlStartingWith("/identity")).willReturn(aJsonResponse("{}")));
        stubFor(get(urlStartingWith("/rest")).willReturn(aJsonResponse("{\"success\": true}")));

        when(command.getResultType()).thenReturn(Data.class);
        when(command.getMethod()).thenReturn("GET");
        when(command.getPath()).thenReturn("/some/path");
    }

    @Test
    public void shouldExecuteGetCommand() throws Exception {
        given(command.getMethod()).willReturn("GET");
        given(command.getPath()).willReturn("/some/path");
        givenThat(get(path("/rest/some/path")).willReturn(
                aJsonResponse("{\"success\": true, \"result\": {\"string\": \"test\"}}")));

        Data response = testedInstance.execute(command);

        assertThat(response).isNotNull();
        assertThat(response.getString()).isEqualTo("test");
    }

    @Test
    public void shouldIgnoreUnknownFields() throws Exception {
        given(command.getMethod()).willReturn("GET");
        given(command.getPath()).willReturn("/some/path");
        givenThat(get(path("/rest/some/path")).willReturn(
                aJsonResponse("{\"success\": true, \"result\": {}, \"unknown\": true}")));

        testedInstance.execute(command);
    }

    @Test
    public void shouldQueryParameters() throws Exception {
        given(command.getMethod()).willReturn("GET");
        given(command.getPath()).willReturn("/some/path");
        given(command.getParameters()).willReturn(Collections.<String, Object>singletonMap("key", "value"));

        testedInstance.execute(command);

        verify(getRequestedFor(urlStartingWith("/rest")).withQueryParam("key", equalTo("value")));
    }

    @Test
    public void shouldAuthenticateEachRequest() throws Exception {
        givenThat(get(path("/identity/oauth/token")).willReturn(aJsonResponse("{\"access_token\": \"token\"}")));

        testedInstance.execute(command);

        verify(getRequestedFor(urlStartingWith("/rest")).withHeader("Authorization", equalTo("Bearer token")));
    }

    @Test
    public void shouldPassCredentialsToAuthenticate() throws Exception {
        testedInstance.execute(command);

        verify(getRequestedFor(urlStartingWith("/identity/oauth/token"))
                .withQueryParam("grant_type", equalTo("client_credentials"))
                .withQueryParam("client_id", equalTo(CLIENT_ID))
                .withQueryParam("client_secret", equalTo(CLIENT_SECRET)));
    }

    @Test
    public void shouldHandleUnauthorizedError() throws Exception {
        givenThat(get(path("/identity/oauth/token")).willReturn(
                aJsonResponse("{\"error_description\": \"Error!\"}").withStatus(401)));

        thrown.expect(MarketoApiException.class);
        thrown.expectMessage("Error!");

        testedInstance.execute(command);
    }

    @Test
    public void shouldHandleNotSuccessfulResponses() throws Exception {
        givenThat(get(urlStartingWith("/rest")).willReturn(
                aJsonResponse("{\"success\": false, \"errors\":[{\"message\": \"Error!\"}]}")));

        thrown.expect(MarketoApiException.class);
        thrown.expectMessage("Error!");

        testedInstance.execute(command);
    }

    private static UrlMatchingStrategy urlStartingWith(String path) {
        return urlMatching(path + ".*");
    }

    private static UrlMatchingStrategy path(String path) {
        return urlMatching(path + "(\\?.+)?");
    }

    private static ResponseDefinitionBuilder aJsonResponse(String json) {
        return aResponse().withHeader("Content-Type", "application/json").withBody(json);
    }

    private static class Data {
        private String string;

        public String getString() {
            return string;
        }
    }
}