package com.smartling.marketo.sdk.rest.transport;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.smartling.marketo.sdk.FolderId;
import com.smartling.marketo.sdk.FolderType;
import com.smartling.marketo.sdk.rest.Command;
import com.smartling.marketo.sdk.MarketoApiException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.ws.rs.ProcessingException;
import java.lang.annotation.ElementType;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.URLEncoder;
import java.util.Collections;
import java.util.Date;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class HttpCommandExecutorTest extends BaseTransportTest{

    private static final String FOLDER_ID_JSON = "{\"id\":42,\"type\":\"FOLDER\"}";

    @Rule
    public final WireMockRule wireMockRule = new WireMockRule(PORT);

    @Rule
    public final ExpectedException thrown = ExpectedException.none();

    @Mock
    private Command<Data> command;

    private final HttpCommandExecutor testedInstance = new HttpCommandExecutor(IDENTITY_URL, REST_URL, CLIENT_ID, CLIENT_SECRET);

    @Before
    public void setUp() throws Exception {
        stubFor(get(urlStartingWith("/identity")).willReturn(aJsonResponse("{\"access_token\": \"\",\"expires_in\": 100000}")));
        stubFor(get(urlStartingWith("/rest")).willReturn(aJsonResponse("{\"success\": true}")));
        stubFor(post(urlStartingWith("/rest")).willReturn(aJsonResponse("{\"success\": true}")));

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
        assertThat(response.string).isEqualTo("test");
    }

    @Test
    public void shouldIgnoreUnknownFields() throws Exception {
        given(command.getPath()).willReturn("/some/path");
        givenThat(get(path("/rest/some/path")).willReturn(
                aJsonResponse("{\"success\": true, \"result\": {}, \"unknown\": true}")));

        testedInstance.execute(command);
    }

    @Test
    public void shouldQueryParameters() throws Exception {
        given(command.getPath()).willReturn("/some/path");
        given(command.getParameters()).willReturn(Collections.<String, Object>singletonMap("key", "value"));

        testedInstance.execute(command);

        verify(getRequestedFor(urlStartingWith("/rest")).withQueryParam("key", equalTo("value")));
    }

    @Test
    public void shouldAuthenticateEachRequest() throws Exception {
        reloadTokenCache();

        givenThat(get(path("/identity/oauth/token")).willReturn(aJsonResponse(
                "{\"access_token\": \"token\",\"expires_in\": 100000}\"}")));

        testedInstance.execute(command);

        verify(getRequestedFor(urlStartingWith("/rest")).withHeader("Authorization", equalTo("Bearer token")));
    }

    @Test
    public void shouldPassCredentialsToAuthenticate() throws Exception {
        reloadTokenCache();

        testedInstance.execute(command);

        verify(getRequestedFor(urlStartingWith("/identity/oauth/token"))
                .withQueryParam("grant_type", equalTo("client_credentials"))
                .withQueryParam("client_id", equalTo(CLIENT_ID))
                .withQueryParam("client_secret", equalTo(CLIENT_SECRET)));
    }

    @Test
    public void shouldHandleUnauthorizedError() throws Exception {
        reloadTokenCache();

        givenThat(get(path("/identity/oauth/token"))
                .willReturn(aJsonResponse("{\"error\": \"unauthorized\", \"error_description\": \"Error!\"}").withStatus(401)));

        thrown.expect(MarketoApiException.class);
        thrown.expectMessage("unauthorized: Error!");
        thrown.expect(exceptionWithCode("401"));


        testedInstance.execute(command);
    }

    @Test
    public void shouldHandleNotSuccessfulResponses() throws Exception {
        givenThat(get(urlStartingWith("/rest")).willReturn(
                aJsonResponse("{\"success\": false, \"errors\":[{\"code\": \"100\",\"message\": \"Error!\"}]}")));

        thrown.expect(MarketoApiException.class);
        thrown.expect(exceptionWithCode("100"));
        thrown.expectMessage("Error!");

        testedInstance.execute(command);
    }

    @Test
    public void shouldExecutePostCommands() throws Exception {
        given(command.getMethod()).willReturn("POST");
        given(command.getPath()).willReturn("/some/path");
        givenThat(post(urlMatching("/rest/some/path")).willReturn(
                aJsonResponse("{\"success\": true, \"result\": {\"string\": \"test\"}}")));

        Data response = testedInstance.execute(command);

        assertThat(response).isNotNull();
        assertThat(response.string).isEqualTo("test");
    }

    @Test
    public void shouldPassPostParameters() throws Exception {
        given(command.getMethod()).willReturn("POST");
        given(command.getParameters()).willReturn(Collections.<String, Object>singletonMap("key", "value"));

        testedInstance.execute(command);

        verify(postRequestedFor(urlStartingWith("/rest")).withRequestBody(withFormParam("key", "value")));
    }

    @Test
    public void shouldSpecifyCharsetOfForm() throws Exception {
        given(command.getMethod()).willReturn("POST");

        testedInstance.execute(command);

        verify(postRequestedFor(urlStartingWith("/rest")).withHeader("Content-Type", containing(";charset=UTF-8")));
    }

    @Test
    public void shouldDeserializeVoid() throws Exception {
        given(command.getResultType()).willReturn(Void.TYPE);

        testedInstance.execute(command);
    }

    @Test
    public void shouldDeserializeDates() throws Exception {
        givenThat(get(path("/rest/some/path")).willReturn(
                aJsonResponse("{\"success\": true, \"result\": {\"date\": \"2015-02-10T07:53:03Z+0000\"}}")));

        Data response = testedInstance.execute(command);

        assertThat(response.date).isNotNull();
    }

    @Test
    public void shouldDeserializeEnumsIgnoringCase() throws Exception {
        givenThat(get(path("/rest/some/path")).willReturn(
                aJsonResponse("{\"success\": true, \"result\": {\"enumeration\": \"method\"}}")));

        Data response = testedInstance.execute(command);

        assertThat(response.enumeration).isEqualTo(ElementType.METHOD);
    }

    @Test(timeout = 2 * 1000, expected = ProcessingException.class)
    public void shouldSupportSocketTimeoutConfiguration() throws Exception {
        addRequestProcessingDelay(5 * 1000);

        testedInstance.setSocketReadTimeout(1000);
        testedInstance.execute(command);
    }

    @Test
    public void shouldSerializeAndUrlEncodeJsonParametersOnPost() throws Exception {
        given(command.getMethod()).willReturn("POST");
        given(command.getParameters()).willReturn(Collections.<String, Object>singletonMap("folderId", new FolderId(42, FolderType.FOLDER)));

        testedInstance.execute(command);

        verify(postRequestedFor(urlStartingWith("/rest")).withRequestBody(withFormParam("folderId", URLEncoder.encode(FOLDER_ID_JSON, "UTF-8"))));
    }


    @Test
    public void shouldSerializeAndUrlEncodeJsonParametersOnGet() throws Exception {
        given(command.getPath()).willReturn("/some/path");
        given(command.getParameters()).willReturn(Collections.<String, Object>singletonMap("folderId", new FolderId(42, FolderType.FOLDER)));

        testedInstance.execute(command);

        verify(getRequestedFor(urlStartingWith("/rest")).withQueryParam("folderId", equalTo(FOLDER_ID_JSON)));
    }

    @SuppressWarnings("unused")
    private static class Data {
        private String string;
        private Date date;
        private ElementType enumeration;
    }

    private void reloadTokenCache() throws NoSuchFieldException, IllegalAccessException {
        Field tokenCache = testedInstance.getClass().getDeclaredField("tokenCache");
        tokenCache.setAccessible(true);
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(tokenCache, tokenCache.getModifiers() & ~Modifier.FINAL);
        tokenCache.set(testedInstance, new MarketoTokenCache());
    }
}