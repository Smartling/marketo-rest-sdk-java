package com.smartling.marketo.sdk.rest.transport;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.smartling.marketo.sdk.domain.email.EmailContentItem;
import com.smartling.marketo.sdk.domain.email.EmailSnippetContentItem;
import com.smartling.marketo.sdk.domain.email.EmailTextContentItem;
import com.smartling.marketo.sdk.domain.folder.FolderId;
import com.smartling.marketo.sdk.domain.folder.FolderType;
import com.smartling.marketo.sdk.rest.Command;
import com.smartling.marketo.sdk.MarketoApiException;
import com.smartling.marketo.sdk.rest.command.email.LoadEmailContent;
import net.minidev.json.JSONObject;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.ws.rs.ProcessingException;
import javax.ws.rs.client.Client;
import java.net.URLEncoder;

import static java.time.LocalDateTime.now;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class JaxRsHttpCommandExecutorTest extends BaseTransportTest {

    private static final String FOLDER_ID_JSON = "{\"id\":42,\"type\":\"Folder\"}";
    private static final String CONTENT_TYPE_TEXT = "Text";
    private static final String CONTENT_TYPE_SNIPPET = "Snippet";

    private JaxRsHttpCommandExecutor testedInstance;

    @Rule
    public final WireMockRule wireMockRule = new WireMockRule(PORT);
    @Rule
    public final ExpectedException thrown = ExpectedException.none();

    @Mock
    private Command<Data> command;
    @Mock
    private TokenProvider tokenProvider;

    @Before
    public void setUp() throws Exception {
        stubFor(get(urlStartingWith("/rest")).willReturn(aJsonResponse("{\"success\": true}")));
        stubFor(post(urlStartingWith("/rest")).willReturn(aJsonResponse("{\"success\": true}")));

        when(command.getResultType()).thenReturn(Data.class);
        when(command.getMethod()).thenReturn("GET");
        when(command.getPath()).thenReturn("/some/path");

        when(tokenProvider.authenticate(getClientConnectionData(any(Client.class), CLIENT_ID)))
                .thenReturn(new Token(now().plusHours(1), "token"));

        testedInstance = new JaxRsHttpCommandExecutor(IDENTITY_URL, REST_URL, CLIENT_ID, CLIENT_SECRET, tokenProvider);
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
    public void shouldReturnAllNeededDataInResponseForLoadEmailContentCommand() throws Exception {
        LoadEmailContent command = new LoadEmailContent(42);

        givenThat(get(path("/rest/asset/v1/email/42/content")).willReturn(
                aJsonResponse(json())));

        List<EmailContentItem> response = testedInstance.execute(command);

        List<EmailTextContentItem.Value> value = ((EmailTextContentItem)response.get(0)).getValue();

        EmailTextContentItem.Value html = getTextContentItemValue("HTML", "<p>html");
        EmailTextContentItem.Value text = getTextContentItemValue("Text", "text");

        assertThat(response).isNotNull();
        assertThat(response.get(0).getHtmlId()).isEqualTo("edit_content");
        assertThat(value.size()).isEqualTo(2);

        assertThat(value.get(0)).isEqualsToByComparingFields(html);
        assertThat(value.get(1)).isEqualsToByComparingFields(text);
    }

    @Test
    public void shouldHaveTextValueAsNullIfNoDataInJson() throws Exception {
        LoadEmailContent command = new LoadEmailContent(42);

        givenThat(get(path("/rest/asset/v1/email/42/content")).willReturn(
                aJsonResponse(jsonWithNoTextValue())));

        List<EmailContentItem> response = testedInstance.execute(command);

        assertThat(((EmailTextContentItem)response.get(0)).getValue().get(0).getValue()).isNull();
    }

    @Test
    public void shouldHaveTextValueAsEmptyStringIfEmptyStringInJson() throws Exception {
        LoadEmailContent command = new LoadEmailContent(42);

        givenThat(get(path("/rest/asset/v1/email/42/content")).willReturn(
                aJsonResponse(jsonWithEmptyTextValue())));

        List<EmailContentItem> response = testedInstance.execute(command);

        assertThat(((EmailTextContentItem)response.get(0)).getValue().get(0).getValue()).isEqualTo("");
    }

    @Test
    public void shouldReadSnippetItemsFromJson() throws Exception {
        LoadEmailContent command = new LoadEmailContent(42);

        givenThat(get(path("/rest/asset/v1/email/42/content")).willReturn(
                aJsonResponse(jsonWithSnippetItem())));

        List<EmailContentItem> response = testedInstance.execute(command);

        assertThat(response.get(0)).isInstanceOf(EmailSnippetContentItem.class);
        assertThat(response.get(1)).isInstanceOf(EmailTextContentItem.class);
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
        given(command.getParameters()).willReturn(Collections.singletonMap("key", "value"));

        testedInstance.execute(command);

        verify(getRequestedFor(urlStartingWith("/rest")).withQueryParam("key", equalTo("value")));
    }

    @Test
    public void shouldAuthenticateEachRequest() throws Exception {

        givenThat(get(path("/identity/oauth/token")).willReturn(aJsonResponse(
                "{\"access_token\": \"token\",\"expires_in\": 100000}\"}")));

        testedInstance.execute(command);

        verify(getRequestedFor(urlStartingWith("/rest")).withHeader("Authorization", equalTo("Bearer token")));
    }

    @Test
    public void shouldPassCredentialsToAuthenticate() throws Exception {

        ArgumentCaptor<ClientConnectionData> clientConnectionDataArgumentCaptor
                = ArgumentCaptor.forClass(ClientConnectionData.class);
        testedInstance.execute(command);

        org.mockito.Mockito.verify(tokenProvider).authenticate(clientConnectionDataArgumentCaptor.capture());

        ClientConnectionData value = clientConnectionDataArgumentCaptor.getValue();
        assertThat(value.getClientId()).isEqualTo(CLIENT_ID);
        assertThat(value.getClientSecret()).isEqualTo(CLIENT_SECRET);
        assertThat(value.getIdentityUrl()).isEqualTo(IDENTITY_URL);

    }

    @Test
    public void shouldReThrowExceptionWhenTokenProviderThrowException() throws Exception {

        when(tokenProvider.authenticate(any(ClientConnectionData.class)))
                .thenThrow(new MarketoApiException("401", "unauthorized: Error!"));

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
        given(command.getParameters()).willReturn(Collections.singletonMap("key", "value"));

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
    public void shouldDeserializeEnumsAccordingToJsonAnnotations() throws Exception {
        givenThat(get(path("/rest/some/path")).willReturn(
                aJsonResponse("{\"success\": true, \"result\": {\"enumeration\": \"val-1\"}}")));

        Data response = testedInstance.execute(command);

        assertThat(response.enumeration).isEqualTo(Data.CustomEnum.VALUE_ONE);
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
        given(command.getParameters()).willReturn(Collections.singletonMap("folderId", new FolderId(42, FolderType.FOLDER)));

        testedInstance.execute(command);

        verify(postRequestedFor(urlStartingWith("/rest")).withRequestBody(withFormParam("folderId", URLEncoder.encode(FOLDER_ID_JSON, "UTF-8"))));
    }


    @Test
    public void shouldSerializeAndUrlEncodeJsonParametersOnGet() throws Exception {
        given(command.getPath()).willReturn("/some/path");
        given(command.getParameters()).willReturn(Collections.singletonMap("folderId", new FolderId(42, FolderType.FOLDER)));

        testedInstance.execute(command);

        verify(getRequestedFor(urlStartingWith("/rest")).withQueryParam("folderId", equalTo(FOLDER_ID_JSON)));
    }

    @SuppressWarnings("unused")
    private static class Data {
        private String string;
        private Date date;
        private CustomEnum enumeration;

        private enum CustomEnum {
            @JsonProperty("val-1")
            VALUE_ONE,
            @JsonProperty("val-2")
            VALUE_TWO
        }
    }

    private String json()
    {
        List<JSONObject> value = Arrays.asList(
                jsonTextContentItemValue("HTML", "<p>html"),
                jsonTextContentItemValue("Text", "text")
        );

        List<JSONObject> result = Collections.singletonList(
                jsonTextContentItem(value)
        );

        JSONObject response = jsonResponse(result);
        return response.toJSONString();
    }

    private String jsonWithNoTextValue()
    {
        List<JSONObject> value = Collections.singletonList(
                jsonTextContentItemValue("Text", null)
        );

        List<JSONObject> result = Collections.singletonList(
                jsonTextContentItem(value)
        );

        JSONObject response = jsonResponse(result);
        return response.toJSONString();
    }

    private String jsonWithEmptyTextValue()
    {
        List<JSONObject> value = Collections.singletonList(
                jsonTextContentItemValue("Text", "")
        );

        List<JSONObject> result = Collections.singletonList(
                jsonTextContentItem(value)
        );

        JSONObject response = jsonResponse(result);
        return response.toJSONString();
    }

    private String jsonWithSnippetItem()
    {
        List<JSONObject> value = Arrays.asList(
                jsonTextContentItemValue("HTML", "<p>html"),
                jsonTextContentItemValue("Text", "text")
        );

        List<JSONObject> result = Arrays.asList(jsonSnippetContentItem(), jsonTextContentItem(value));

        JSONObject response = jsonResponse(result);
        return response.toJSONString();
    }

    private JSONObject jsonTextContentItem(List<JSONObject> value) {
        return new JSONObject() {{
            this.put("htmlId", "edit_content");
            this.put("value", value);
            this.put("contentType", CONTENT_TYPE_TEXT);
        }};
    }

    private JSONObject jsonSnippetContentItem() {
        return new JSONObject() {{
            this.put("htmlId", "edit_content");
            this.put("value", "Snippet text value");
            this.put("contentType", CONTENT_TYPE_SNIPPET);
        }};
    }

    private JSONObject jsonResponse(List<JSONObject> result) {
        JSONObject response = new JSONObject();
        response.put("success", true);
        response.put("result", result);
        return response;
    }

    private JSONObject jsonTextContentItemValue(String type, String value) {
        return new JSONObject()
        {{
            this.put("type", type);
            this.put("value", value);
        }};
    }

    private EmailTextContentItem.Value getTextContentItemValue(String type, String value) {
        EmailTextContentItem.Value item = new EmailTextContentItem.Value();
        item.setType(type);
        item.setValue(value);
        return item;
    }
}