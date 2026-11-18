package com.smartling.marketo.sdk.rest;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.smartling.marketo.sdk.MarketoApiException;
import com.smartling.marketo.sdk.MarketoFragmentV2Client;
import com.smartling.marketo.sdk.domain.folder.FolderType;
import com.smartling.marketo.sdk.domain.v2.email.SortOrder;
import com.smartling.marketo.sdk.domain.v2.fragment.FragmentV2;
import com.smartling.marketo.sdk.domain.v2.fragment.FragmentV2Filter;
import com.smartling.marketo.sdk.domain.v2.fragment.FragmentV2Page;
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
import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.havingExactly;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.InstanceOfAssertFactories.type;

public class MarketoFragmentV2EndToEndTest
{
    private static final String FILTER_FIXTURE = "/json/fragment-v2-filter-response.json";
    private static final String GET_FIXTURE = "/json/fragment-v2-get-response.json";
    private static final String TOKEN_PATH = "/identity/oauth/token";
    private static final String FILTER_PATH = "/rest/asset/v2/fragment/filter";
    private static final String FRAGMENT_ID = "0123456789abcdef01234567";
    private static final String GET_PATH = "/rest/asset/v2/fragment/" + FRAGMENT_ID;
    private static final String WORKSPACE_ID = "test-workspace";
    private static final String TOKEN = "test-token";
    private static final String TOKEN_BODY = "{\"access_token\":\"" + TOKEN + "\",\"expires_in\":3600}";
    private static final String APP_TYPE_HEADER = "x-app-type";
    private static final String APP_TYPE = "marketo";
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_TOKEN = "Bearer " + TOKEN;
    private static final String CONTENT_TYPE_HEADER = "Content-Type";
    private static final String JSON_CONTENT_TYPE = "application/json";
    private static final String SPECIAL_VALUE = "a+b&c=d#e %41 {x} é";
    private static final String FOLDER_ID = "folder-1";
    private static final String STATUS_DRAFT = "draft";
    private static final String STATUS_PUBLISHED = "published";
    private static final String FRAGMENT_NAME = "test-name";
    private static final String FRAGMENT_TYPE = "test-fragment-type";
    private static final String SORT_KEY = "test-sort-key";
    private static final String SCRIPT_ENGINE = "VELOCITY";
    private static final String VALUE_NON_NULLABLE = "test-non-nullable";
    private static final String WORKSPACE_ID_PARAMETER = "workspaceId";
    private static final String NAME_PARAMETER = "name";
    private static final String STATUS_PARAMETER = "status";
    private static final String CREATED_BY_ME_PARAMETER = "isCreatedByMe";
    private static final String MODIFIED_BY_ME_PARAMETER = "isModifiedByMe";
    private static final String INCLUDE_ARCHIVED_PARAMETER = "includeArchived";
    private static final String ERROR_CODE_NOT_FOUND = "603";
    private static final String ERROR_CODE_INVALID_PARAMETER = "611";
    private static final String ERROR_CODE_REQUEST_LIMIT = "606";
    private static final String ERROR_BODY_TEMPLATE =
            "{\"success\":false,\"errors\":[{\"code\":\"%s\",\"message\":\"test error\"}]}";

    private final ObjectMapper objectMapper = new ObjectMapperProvider().getContext(null);

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(wireMockConfig().dynamicPort());

    private MarketoFragmentV2Client client;

    @Before
    public void setUp()
    {
        // Build the fragment client over the real Jersey executor without retries
        client = newClient();

        // Serve the access token
        stubGetResponse(TOKEN_PATH, TOKEN_BODY);
    }

    @Test
    public void shouldSendEveryFilterAsQueryParameter() throws Exception
    {
        // Arrange a filter with every property set
        stubGetResponse(FILTER_PATH, fixture(FILTER_FIXTURE));
        FragmentV2Filter filter = FragmentV2Filter.builder(WORKSPACE_ID)
                .folderId(FOLDER_ID)
                .folderType(FolderType.PROGRAM)
                .status(Arrays.asList(STATUS_DRAFT, STATUS_PUBLISHED))
                .createdAtStart(Instant.parse("2026-01-02T03:04:05Z"))
                .createdAtEnd(Instant.parse("2026-02-03T04:05:06Z"))
                .modifiedAtStart(Instant.parse("2026-03-04T05:06:07Z"))
                .modifiedAtEnd(Instant.parse("2026-04-05T06:07:08Z"))
                .name(FRAGMENT_NAME)
                .fragmentType(FRAGMENT_TYPE)
                .sortKey(SORT_KEY)
                .sortOrder(SortOrder.DESC)
                .createdByMe(true)
                .modifiedByMe(false)
                .includeArchived(true)
                .scriptEngine(SCRIPT_ENGINE)
                .valueNonNullable(VALUE_NON_NULLABLE)
                .pageIndex(2)
                .pageSize(25)
                .build();

        // Filter the fragments
        client.filterFragments(filter);

        // Every property reaches the server as its own query parameter
        wireMockRule.verify(getRequestedFor(urlPathEqualTo(FILTER_PATH))
                .withHeader(APP_TYPE_HEADER, equalTo(APP_TYPE))
                .withQueryParam(WORKSPACE_ID_PARAMETER, equalTo(WORKSPACE_ID))
                .withQueryParam("folderId", equalTo(FOLDER_ID))
                .withQueryParam("folderType", equalTo("Program"))
                .withQueryParam(STATUS_PARAMETER, havingExactly(STATUS_DRAFT, STATUS_PUBLISHED))
                .withQueryParam("createdAtStart", equalTo("2026-01-02T03:04:05"))
                .withQueryParam("createdAtEnd", equalTo("2026-02-03T04:05:06"))
                .withQueryParam("modifiedAtStart", equalTo("2026-03-04T05:06:07"))
                .withQueryParam("modifiedAtEnd", equalTo("2026-04-05T06:07:08"))
                .withQueryParam(NAME_PARAMETER, equalTo(FRAGMENT_NAME))
                .withQueryParam("fragmentType", equalTo(FRAGMENT_TYPE))
                .withQueryParam("sortKey", equalTo(SORT_KEY))
                .withQueryParam("sortOrder", equalTo("DESC"))
                .withQueryParam(CREATED_BY_ME_PARAMETER, equalTo("true"))
                .withQueryParam(MODIFIED_BY_ME_PARAMETER, equalTo("false"))
                .withQueryParam(INCLUDE_ARCHIVED_PARAMETER, equalTo("true"))
                .withQueryParam("scriptEngine", equalTo(SCRIPT_ENGINE))
                .withQueryParam("isValueNonNullable", equalTo(VALUE_NON_NULLABLE))
                .withQueryParam("pageIndex", equalTo("2"))
                .withQueryParam("pageSize", equalTo("25")));
    }

    @Test
    public void shouldSendXAppTypeHeaderAndBearerTokenOnFilterAndGet() throws Exception
    {
        // Arrange both fragment endpoints
        stubGetResponse(FILTER_PATH, fixture(FILTER_FIXTURE));
        stubGetResponse(GET_PATH, fixture(GET_FIXTURE));

        // Filter the fragments, then load one by id
        FragmentV2Page fragmentPage = client.filterFragments(emptyFilter());
        FragmentV2 loadedFragment = client.loadFragmentById(FRAGMENT_ID);

        // Both requests carry the app type and the bearer token, and the fixtures are deserialized
        wireMockRule.verify(getRequestedFor(urlPathEqualTo(FILTER_PATH))
                .withHeader(APP_TYPE_HEADER, equalTo(APP_TYPE))
                .withHeader(AUTHORIZATION_HEADER, equalTo(BEARER_TOKEN)));
        wireMockRule.verify(getRequestedFor(urlPathEqualTo(GET_PATH))
                .withHeader(APP_TYPE_HEADER, equalTo(APP_TYPE))
                .withHeader(AUTHORIZATION_HEADER, equalTo(BEARER_TOKEN)));
        assertThat(fragmentPage)
                .usingRecursiveComparison()
                .isEqualTo(expectedFixtureResult(FILTER_FIXTURE, FragmentV2Page.class));
        assertThat(loadedFragment)
                .usingRecursiveComparison()
                .isEqualTo(expectedFirstFixtureFragment(GET_FIXTURE));
    }

    @Test
    public void shouldLoadFragmentById() throws Exception
    {
        // Arrange the fragment endpoint
        stubGetResponse(GET_PATH, fixture(GET_FIXTURE));

        // Load the fragment by its id
        FragmentV2 loadedFragment = client.loadFragmentById(FRAGMENT_ID);

        // The id is in the path and the first element of the result is returned
        wireMockRule.verify(1, getRequestedFor(urlPathEqualTo(GET_PATH)));
        assertThat(loadedFragment)
                .usingRecursiveComparison()
                .isEqualTo(expectedFirstFixtureFragment(GET_FIXTURE));
    }

    @Test
    public void shouldSendEachBooleanFilterUnderItsOwnQueryParameter() throws Exception
    {
        // Arrange a filter with a different value in each boolean property
        stubGetResponse(FILTER_PATH, fixture(FILTER_FIXTURE));
        FragmentV2Filter filter = FragmentV2Filter.builder(WORKSPACE_ID)
                .createdByMe(false)
                .modifiedByMe(true)
                .includeArchived(false)
                .build();

        // Filter the fragments
        client.filterFragments(filter);

        // Each value reaches the server under its own parameter
        wireMockRule.verify(getRequestedFor(urlPathEqualTo(FILTER_PATH))
                .withQueryParam(CREATED_BY_ME_PARAMETER, equalTo("false"))
                .withQueryParam(MODIFIED_BY_ME_PARAMETER, equalTo("true"))
                .withQueryParam(INCLUDE_ARCHIVED_PARAMETER, equalTo("false")));
    }

    @Test
    public void shouldDeliverSpecialCharactersToServerUnchanged() throws Exception
    {
        // Arrange a filter whose name and status contain reserved characters
        stubGetResponse(FILTER_PATH, fixture(FILTER_FIXTURE));
        FragmentV2Filter filter = FragmentV2Filter.builder(WORKSPACE_ID)
                .name(SPECIAL_VALUE)
                .status(Collections.singletonList(SPECIAL_VALUE))
                .build();

        // Filter the fragments
        client.filterFragments(filter);

        // The server decodes both values to exactly what was given
        wireMockRule.verify(getRequestedFor(urlPathEqualTo(FILTER_PATH))
                .withQueryParam(NAME_PARAMETER, equalTo(SPECIAL_VALUE))
                .withQueryParam(STATUS_PARAMETER, equalTo(SPECIAL_VALUE)));
    }

    @Test
    public void shouldMapError603OnGetToObjectNotFound()
    {
        // Arrange an error 603 on the fragment endpoint
        stubGetResponse(GET_PATH, errorBody(ERROR_CODE_NOT_FOUND));

        // Load the fragment by its id
        Throwable loadFailure = catchThrowable(() -> client.loadFragmentById(FRAGMENT_ID));

        // The error becomes an ObjectNotFoundException that keeps the code
        assertApiError(loadFailure, ObjectNotFoundException.class, ERROR_CODE_NOT_FOUND);
    }

    @Test
    public void shouldKeepError603OnFilterAsMarketoApiException()
    {
        // Arrange an error 603 on the filter endpoint
        stubGetResponse(FILTER_PATH, errorBody(ERROR_CODE_NOT_FOUND));

        // Filter the fragments
        Throwable filterFailure = catchThrowable(() -> client.filterFragments(emptyFilter()));

        // The error stays a plain MarketoApiException
        assertApiError(filterFailure, MarketoApiException.class, ERROR_CODE_NOT_FOUND);
    }

    @Test
    public void shouldMapErrorCode611ToMarketoApiException()
    {
        // Arrange an error 611 on the filter endpoint
        stubGetResponse(FILTER_PATH, errorBody(ERROR_CODE_INVALID_PARAMETER));

        // Filter the fragments
        Throwable filterFailure = catchThrowable(() -> client.filterFragments(emptyFilter()));

        // The error is a plain MarketoApiException with its code
        assertApiError(filterFailure, MarketoApiException.class, ERROR_CODE_INVALID_PARAMETER);
    }

    @Test
    public void shouldMapErrorCode606ToRequestLimitExceeded()
    {
        // Arrange an error 606 on the filter endpoint
        stubGetResponse(FILTER_PATH, errorBody(ERROR_CODE_REQUEST_LIMIT));

        // Filter the fragments
        Throwable filterFailure = catchThrowable(() -> client.filterFragments(emptyFilter()));

        // The error becomes a RequestLimitExceededException with its code
        assertApiError(filterFailure, RequestLimitExceededException.class, ERROR_CODE_REQUEST_LIMIT);
    }

    private MarketoFragmentV2Client newClient()
    {
        // Point the executor at the WireMock server
        String baseUrl = "http://localhost:" + wireMockRule.port();
        Client jerseyClient = ClientBuilder.newClient()
                .register(JacksonFeature.class)
                .register(ObjectMapperProvider.class);
        JaxRsHttpCommandExecutor jaxRsExecutor = new JaxRsHttpCommandExecutor(
                baseUrl + "/identity",
                baseUrl + "/rest",
                "test-client-id",
                "test-secret",
                new BasicTokenProvider(),
                jerseyClient);
        HttpCommandExecutor retryingExecutor = new RetryingHttpCommandExecutor(
                jaxRsExecutor,
                RetryPolicy.NONE);

        return new MarketoRestClientManager(retryingExecutor).getMarketoFragmentV2Client();
    }

    private void stubGetResponse(String path, String responseBody)
    {
        wireMockRule.stubFor(get(urlPathEqualTo(path))
                .willReturn(jsonResponse(responseBody)));
    }

    private static FragmentV2Filter emptyFilter()
    {
        return FragmentV2Filter.builder(WORKSPACE_ID)
                .build();
    }

    private static void assertApiError(Throwable failure, Class<? extends MarketoApiException> expectedType, String expectedErrorCode)
    {
        assertThat(failure)
                .isExactlyInstanceOf(expectedType)
                .asInstanceOf(type(MarketoApiException.class))
                .extracting(MarketoApiException::getErrorCode)
                .isEqualTo(expectedErrorCode);
    }

    private static String errorBody(String errorCode)
    {
        return String.format(ERROR_BODY_TEMPLATE, errorCode);
    }

    private static ResponseDefinitionBuilder jsonResponse(String responseBody)
    {
        return aResponse()
                .withHeader(CONTENT_TYPE_HEADER, JSON_CONTENT_TYPE)
                .withBody(responseBody);
    }

    private String fixture(String path) throws Exception
    {
        try (InputStream fixtureStream = getClass().getResourceAsStream(path))
        {
            return objectMapper.readTree(fixtureStream).toString();
        }
    }

    private <T> T expectedFixtureResult(String path, Class<T> type) throws Exception
    {
        JsonNode fixtureResult = objectMapper.readTree(fixture(path)).get("result");

        return objectMapper.treeToValue(fixtureResult, type);
    }

    private FragmentV2 expectedFirstFixtureFragment(String path) throws Exception
    {
        JsonNode fixtureResult = objectMapper.readTree(fixture(path)).get("result");

        return objectMapper.treeToValue(fixtureResult.get(0), FragmentV2.class);
    }
}
