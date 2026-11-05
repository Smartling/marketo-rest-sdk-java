package com.smartling.marketo.sdk.rest;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.smartling.marketo.sdk.MarketoApiException;
import com.smartling.marketo.sdk.MarketoEmailV2Client;
import com.smartling.marketo.sdk.domain.folder.FolderType;
import com.smartling.marketo.sdk.domain.v2.email.EmailV2;
import com.smartling.marketo.sdk.domain.v2.email.EmailV2Filter;
import com.smartling.marketo.sdk.domain.v2.email.EmailV2Page;
import com.smartling.marketo.sdk.domain.v2.email.SortOrder;
import com.smartling.marketo.sdk.rest.transport.BasicTokenProvider;
import com.smartling.marketo.sdk.rest.transport.JaxRsHttpCommandExecutor;
import com.smartling.marketo.sdk.rest.transport.ObjectMapperProvider;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.Arrays;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.havingExactly;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

public class MarketoEmailV2EndToEndTest
{
    private static final String FILTER_FIXTURE = "/json/email-v2-filter-response.json";
    private static final String GET_FIXTURE = "/json/email-v2-get-response.json";
    private static final String TOKEN_PATH = "/identity/oauth/token";
    private static final String FILTER_PATH = "/rest/asset/v2/email/filter";
    private static final String EMAIL_ID = "1001";
    private static final String GET_PATH = "/rest/asset/v2/email/" + EMAIL_ID;
    private static final String WORKSPACE_ID = "test-workspace";
    private static final String TOKEN = "test-token";
    private static final String TOKEN_BODY = "{\"access_token\":\"" + TOKEN + "\",\"expires_in\":3600}";
    private static final String APP_TYPE_HEADER = "x-app-type";
    private static final String APP_TYPE = "marketo";
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_TOKEN = "Bearer " + TOKEN;
    private static final String SPECIAL_VALUE = "a+b&c=d#e %41 {x} é";
    private static final String ERROR_BODY_TEMPLATE =
            "{\"success\":false,\"errors\":[{\"code\":\"%s\",\"message\":\"test error\"}]}";

    private final ObjectMapper objectMapper = new ObjectMapperProvider().getContext(null);

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(wireMockConfig().dynamicPort());

    private MarketoEmailV2Client client;

    @Before
    public void setUp()
    {
        String baseUrl = "http://localhost:" + wireMockRule.port();
        Client jerseyClient = ClientBuilder.newClient()
                .register(JacksonFeature.class)
                .register(ObjectMapperProvider.class);
        HttpCommandExecutor executor = new RetryingHttpCommandExecutor(
                new JaxRsHttpCommandExecutor(
                        baseUrl + "/identity",
                        baseUrl + "/rest",
                        "test-client-id",
                        "test-secret",
                        new BasicTokenProvider(),
                        jerseyClient),
                RetryPolicy.NONE);
        client = new MarketoRestClientManager(executor).getMarketoEmailV2Client();

        wireMockRule.stubFor(get(urlPathEqualTo(TOKEN_PATH)).willReturn(jsonResponse(TOKEN_BODY)));
    }

    @Test
    public void shouldSendXAppTypeHeaderAndBearerTokenOnFilterAndGet() throws Exception
    {
        wireMockRule.stubFor(get(urlPathEqualTo(FILTER_PATH)).willReturn(jsonResponse(fixture(FILTER_FIXTURE))));
        wireMockRule.stubFor(get(urlPathEqualTo(GET_PATH)).willReturn(jsonResponse(fixture(GET_FIXTURE))));

        EmailV2Page page = client.filterEmails(EmailV2Filter.builder(WORKSPACE_ID).build());
        EmailV2 email = client.loadEmailById(EMAIL_ID);

        wireMockRule.verify(getRequestedFor(urlPathEqualTo(FILTER_PATH))
                .withHeader(APP_TYPE_HEADER, equalTo(APP_TYPE))
                .withHeader(AUTHORIZATION_HEADER, equalTo(BEARER_TOKEN)));
        wireMockRule.verify(getRequestedFor(urlPathEqualTo(GET_PATH))
                .withHeader(APP_TYPE_HEADER, equalTo(APP_TYPE))
                .withHeader(AUTHORIZATION_HEADER, equalTo(BEARER_TOKEN)));
        assertThat(page).usingRecursiveComparison().isEqualTo(expectedFixtureResult(FILTER_FIXTURE, EmailV2Page.class));
        assertThat(email).usingRecursiveComparison().isEqualTo(expectedFirstFixtureItem(GET_FIXTURE));
    }

    @Test
    public void shouldSendEveryFilterAsQueryParameter() throws Exception
    {
        wireMockRule.stubFor(get(urlPathEqualTo(FILTER_PATH)).willReturn(jsonResponse(fixture(FILTER_FIXTURE))));
        EmailV2Filter filter = EmailV2Filter.builder(WORKSPACE_ID)
                .folderId("folder-1")
                .folderType(FolderType.PROGRAM)
                .status(Arrays.asList("draft", "published"))
                .createdAtStart(LocalDateTime.of(2026, 1, 2, 3, 4, 5))
                .createdAtEnd(LocalDateTime.of(2026, 2, 3, 4, 5, 6))
                .modifiedAtStart(LocalDateTime.of(2026, 3, 4, 5, 6, 7))
                .modifiedAtEnd(LocalDateTime.of(2026, 4, 5, 6, 7, 8))
                .name("test-name")
                .sortKey("test-sort-key")
                .sortOrder(SortOrder.DESC)
                .createdByMe(true)
                .modifiedByMe(false)
                .includeArchived(true)
                .templateId("template-1")
                .scriptEngine("VELOCITY")
                .valueNonNullable("test-non-nullable")
                .pageIndex(2)
                .pageSize(25)
                .build();

        client.filterEmails(filter);

        wireMockRule.verify(getRequestedFor(urlPathEqualTo(FILTER_PATH))
                .withQueryParam("workspaceId", equalTo(WORKSPACE_ID))
                .withQueryParam("folderId", equalTo("folder-1"))
                .withQueryParam("folderType", equalTo("Program"))
                .withQueryParam("status", havingExactly("draft", "published"))
                .withQueryParam("createdAtStart", equalTo("2026-01-02T03:04:05"))
                .withQueryParam("createdAtEnd", equalTo("2026-02-03T04:05:06"))
                .withQueryParam("modifiedAtStart", equalTo("2026-03-04T05:06:07"))
                .withQueryParam("modifiedAtEnd", equalTo("2026-04-05T06:07:08"))
                .withQueryParam("name", equalTo("test-name"))
                .withQueryParam("sortKey", equalTo("test-sort-key"))
                .withQueryParam("sortOrder", equalTo("DESC"))
                .withQueryParam("isCreatedByMe", equalTo("true"))
                .withQueryParam("isModifiedByMe", equalTo("false"))
                .withQueryParam("includeArchived", equalTo("true"))
                .withQueryParam("templateId", equalTo("template-1"))
                .withQueryParam("scriptEngine", equalTo("VELOCITY"))
                .withQueryParam("isValueNonNullable", equalTo("test-non-nullable"))
                .withQueryParam("pageIndex", equalTo("2"))
                .withQueryParam("pageSize", equalTo("25")));
    }

    @Test
    public void shouldMapErrorCode611ToMarketoApiException()
    {
        stubFilterError("611");

        Throwable thrown = catchThrowable(() -> client.filterEmails(EmailV2Filter.builder(WORKSPACE_ID).build()));

        assertThat(thrown).isExactlyInstanceOf(MarketoApiException.class);
        assertThat(((MarketoApiException) thrown).getErrorCode()).isEqualTo("611");
    }

    @Test
    public void shouldMapErrorCode606ToRequestLimitExceeded()
    {
        stubFilterError("606");

        Throwable thrown = catchThrowable(() -> client.filterEmails(EmailV2Filter.builder(WORKSPACE_ID).build()));

        assertThat(thrown).isExactlyInstanceOf(RequestLimitExceededException.class);
        assertThat(((MarketoApiException) thrown).getErrorCode()).isEqualTo("606");
    }

    @Test
    public void shouldSurface603OnGetAsMarketoApiExceptionNotObjectNotFound()
    {
        wireMockRule.stubFor(get(urlPathEqualTo(GET_PATH)).willReturn(jsonResponse(String.format(ERROR_BODY_TEMPLATE, "603"))));

        Throwable thrown = catchThrowable(() -> client.loadEmailById(EMAIL_ID));

        assertThat(thrown).isExactlyInstanceOf(MarketoApiException.class);
        assertThat(thrown).isNotInstanceOf(ObjectNotFoundException.class);
        assertThat(((MarketoApiException) thrown).getErrorCode()).isEqualTo("603");
    }

    @Test
    public void shouldDeliverSpecialCharactersToServerUnchanged() throws Exception
    {
        wireMockRule.stubFor(get(urlPathEqualTo(FILTER_PATH)).willReturn(jsonResponse(fixture(FILTER_FIXTURE))));
        EmailV2Filter filter = EmailV2Filter.builder(WORKSPACE_ID)
                .name(SPECIAL_VALUE)
                .status(Arrays.asList(SPECIAL_VALUE))
                .build();

        client.filterEmails(filter);

        wireMockRule.verify(getRequestedFor(urlPathEqualTo(FILTER_PATH))
                .withQueryParam("name", equalTo(SPECIAL_VALUE))
                .withQueryParam("status", equalTo(SPECIAL_VALUE)));
    }

    private void stubFilterError(String code)
    {
        wireMockRule.stubFor(get(urlPathEqualTo(FILTER_PATH)).willReturn(jsonResponse(String.format(ERROR_BODY_TEMPLATE, code))));
    }

    private static com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder jsonResponse(String body)
    {
        return aResponse()
                .withHeader("Content-Type", "application/json")
                .withBody(body);
    }

    private String fixture(String path) throws Exception
    {
        try (InputStream stream = getClass().getResourceAsStream(path))
        {
            return objectMapper.readTree(stream).toString();
        }
    }

    private <T> T expectedFixtureResult(String path, Class<T> type) throws Exception
    {
        return objectMapper.treeToValue(objectMapper.readTree(fixture(path)).get("result"), type);
    }

    private EmailV2 expectedFirstFixtureItem(String path) throws Exception
    {
        JsonNode result = objectMapper.readTree(fixture(path)).get("result");
        return objectMapper.treeToValue(result.get(0), EmailV2.class);
    }
}
