package com.smartling.marketo.sdk.rest;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.github.tomakehurst.wiremock.stubbing.Scenario;
import com.google.common.collect.ImmutableMap;
import com.smartling.marketo.sdk.MarketoApiException;
import com.smartling.marketo.sdk.MarketoFragmentV2Client;
import com.smartling.marketo.sdk.domain.folder.FolderType;
import com.smartling.marketo.sdk.domain.v2.email.SortOrder;
import com.smartling.marketo.sdk.domain.v2.fragment.FragmentV2;
import com.smartling.marketo.sdk.domain.v2.fragment.FragmentV2Filter;
import com.smartling.marketo.sdk.domain.v2.fragment.FragmentV2Page;
import com.smartling.marketo.sdk.domain.v2.fragment.FragmentV2Update;
import com.smartling.marketo.sdk.rest.retry.ExponentialBackOffRetryPolicy;
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
import java.util.Map;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.equalToJson;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.havingExactly;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.postRequestedFor;
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
    private static final String JSON_REQUEST_CONTENT_TYPE = "application/json;charset=UTF-8";
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
    private static final String CREATE_DRAFT_FIXTURE = "/json/fragment-v2-create-draft-response.json";
    private static final String CLONE_FIXTURE = "/json/fragment-v2-clone-response.json";
    private static final String CLONE_PATH = "/rest/asset/v2/fragment/clone";
    private static final String UPDATE_PATH = "/rest/asset/v2/fragment/" + FRAGMENT_ID + "/update";
    private static final String TRANSITION_PATH = "/rest/asset/v2/fragment/state/transition";
    private static final String COPY_NAME = "test-fragment-copy";
    private static final String SPECIAL_HTML = "<p class=\"a\">it's & <b>é</b> 日本</p>";
    private static final String TEXT_BODY = "test-text";
    private static final String ERROR_CODE_CONTENT_REJECTED = "60019";
    private static final String SUCCESS_WITH_ARRAY_RESULT = "{\"success\":true,\"errors\":null,\"result\":[{\"id\":\"test-id\"}]}";
    private static final String SUCCESS_WITHOUT_RESULT = "{\"success\":true,\"errors\":null}";
    private static final String RETRY_SCENARIO = "retry-606";
    private static final String RETRY_SUCCEEDED_STATE = "second-attempt";
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
        client = newClient(RetryPolicy.NONE);

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

    @Test
    public void shouldPostCloneAsJsonAndParseTheCopy() throws Exception
    {
        // Arrange the clone answer
        stubPostResponse(CLONE_PATH, fixture(CLONE_FIXTURE));

        // Clone the fragment
        FragmentV2 copy = client.cloneFragment(FRAGMENT_ID, COPY_NAME);

        // The request is the clone body as JSON and the copy is the parsed fixture
        verifyJsonPost(CLONE_PATH, ImmutableMap.of("assetId", FRAGMENT_ID, "newAsset", ImmutableMap.of(NAME_PARAMETER, COPY_NAME)));
        assertThat(copy).usingRecursiveComparison().isEqualTo(expectedFirstFixtureFragment(CLONE_FIXTURE));
    }

    @Test
    public void shouldPostUpdateAsJsonAndDeliverSpecialHtmlUnchanged() throws Exception
    {
        // Arrange the update answer
        stubPostResponse(UPDATE_PATH, SUCCESS_WITH_ARRAY_RESULT);
        FragmentV2Update update = FragmentV2Update.builder()
                .htmlBody(SPECIAL_HTML)
                .textBody(TEXT_BODY)
                .build();

        // Update the fragment
        client.updateFragment(FRAGMENT_ID, update);

        // The special characters reach the server unchanged
        verifyJsonPost(UPDATE_PATH, ImmutableMap.of("data", ImmutableMap.of(
                "html", ImmutableMap.of("body", SPECIAL_HTML),
                "text", ImmutableMap.of("body", TEXT_BODY))));
    }

    @Test
    public void shouldSucceedOnUpdateWithNoResult()
    {
        stubPostResponse(UPDATE_PATH, SUCCESS_WITHOUT_RESULT);

        // Update the fragment
        Throwable updateFailure = catchThrowable(() -> client.updateFragment(FRAGMENT_ID, FragmentV2Update.builder().htmlBody(SPECIAL_HTML).build()));

        assertThat(updateFailure).isNull();
        wireMockRule.verify(1, postRequestedFor(urlPathEqualTo(UPDATE_PATH)));
    }

    @Test
    public void shouldSucceedOnUpdateWithArrayResult()
    {
        stubPostResponse(UPDATE_PATH, SUCCESS_WITH_ARRAY_RESULT);

        // Update the fragment
        Throwable updateFailure = catchThrowable(() -> client.updateFragment(FRAGMENT_ID, FragmentV2Update.builder().htmlBody(SPECIAL_HTML).build()));

        assertThat(updateFailure).isNull();
        wireMockRule.verify(1, postRequestedFor(urlPathEqualTo(UPDATE_PATH)));
    }

    @Test
    public void shouldPostApproveAsJson() throws Exception
    {
        // Arrange the approve answer
        stubPostResponse(TRANSITION_PATH, SUCCESS_WITH_ARRAY_RESULT);

        // Approve the draft
        client.approveFragment(FRAGMENT_ID);

        verifyJsonPost(TRANSITION_PATH, transitionBody("approve"));
    }

    @Test
    public void shouldSucceedOnApproveWithNoResult()
    {
        stubPostResponse(TRANSITION_PATH, SUCCESS_WITHOUT_RESULT);

        // Approve the draft
        Throwable approveFailure = catchThrowable(() -> client.approveFragment(FRAGMENT_ID));

        assertThat(approveFailure).isNull();
        wireMockRule.verify(1, postRequestedFor(urlPathEqualTo(TRANSITION_PATH)));
    }

    @Test
    public void shouldSucceedOnApproveWithArrayResult()
    {
        stubPostResponse(TRANSITION_PATH, SUCCESS_WITH_ARRAY_RESULT);

        // Approve the draft
        Throwable approveFailure = catchThrowable(() -> client.approveFragment(FRAGMENT_ID));

        assertThat(approveFailure).isNull();
        wireMockRule.verify(1, postRequestedFor(urlPathEqualTo(TRANSITION_PATH)));
    }

    @Test
    public void shouldPostCreateDraftAsJsonAndParseTheDraft() throws Exception
    {
        // Arrange the create draft answer
        stubPostResponse(TRANSITION_PATH, fixture(CREATE_DRAFT_FIXTURE));

        // Create the draft
        FragmentV2 draft = client.createDraft(FRAGMENT_ID);

        verifyJsonPost(TRANSITION_PATH, transitionBody("create_draft"));
        assertThat(draft).usingRecursiveComparison().isEqualTo(expectedFirstFixtureFragment(CREATE_DRAFT_FIXTURE));
    }

    @Test
    public void shouldMapError60019OnUpdateToContentRejectedAndNotRetry()
    {
        // Arrange an error 60019 on the update endpoint and a client that retries
        MarketoFragmentV2Client retryingClient = newClient(new ExponentialBackOffRetryPolicy(1, 1));
        stubPostResponse(UPDATE_PATH, errorBody(ERROR_CODE_CONTENT_REJECTED));

        // Update the fragment
        Throwable updateFailure = catchThrowable(() -> retryingClient.updateFragment(FRAGMENT_ID, FragmentV2Update.builder().htmlBody(SPECIAL_HTML).build()));

        // The error is not retried
        assertApiError(updateFailure, ContentRejectedException.class, ERROR_CODE_CONTENT_REJECTED);
        wireMockRule.verify(1, postRequestedFor(urlPathEqualTo(UPDATE_PATH)));
    }

    @Test
    public void shouldRetryApproveAfterError606() throws Exception
    {
        // Arrange an error 606 followed by success, and a client that retries
        MarketoFragmentV2Client retryingClient = newClient(new ExponentialBackOffRetryPolicy(1, 1));
        wireMockRule.stubFor(post(urlPathEqualTo(TRANSITION_PATH))
                .inScenario(RETRY_SCENARIO)
                .whenScenarioStateIs(Scenario.STARTED)
                .willReturn(jsonResponse(errorBody(ERROR_CODE_REQUEST_LIMIT)))
                .willSetStateTo(RETRY_SUCCEEDED_STATE));
        wireMockRule.stubFor(post(urlPathEqualTo(TRANSITION_PATH))
                .inScenario(RETRY_SCENARIO)
                .whenScenarioStateIs(RETRY_SUCCEEDED_STATE)
                .willReturn(jsonResponse(SUCCESS_WITHOUT_RESULT)));

        // Approve the draft
        Throwable approveFailure = catchThrowable(() -> retryingClient.approveFragment(FRAGMENT_ID));

        // The approve is sent twice with the same body
        assertThat(approveFailure).isNull();
        wireMockRule.verify(2, postRequestedFor(urlPathEqualTo(TRANSITION_PATH))
                .withRequestBody(equalToJson(json(transitionBody("approve")), false, false)));
    }

    @Test
    public void shouldMapError603OnEveryWriteToObjectNotFound()
    {
        // Arrange an error 603 on every write endpoint
        stubPostResponse(CLONE_PATH, errorBody(ERROR_CODE_NOT_FOUND));
        stubPostResponse(UPDATE_PATH, errorBody(ERROR_CODE_NOT_FOUND));
        stubPostResponse(TRANSITION_PATH, errorBody(ERROR_CODE_NOT_FOUND));

        // Run the four write calls
        Throwable cloneFailure = catchThrowable(() -> client.cloneFragment(FRAGMENT_ID, COPY_NAME));
        Throwable updateFailure = catchThrowable(() -> client.updateFragment(FRAGMENT_ID, FragmentV2Update.builder().htmlBody(SPECIAL_HTML).build()));
        Throwable createDraftFailure = catchThrowable(() -> client.createDraft(FRAGMENT_ID));
        Throwable approveFailure = catchThrowable(() -> client.approveFragment(FRAGMENT_ID));

        // Each error becomes an ObjectNotFoundException that keeps the code
        assertApiError(cloneFailure, ObjectNotFoundException.class, ERROR_CODE_NOT_FOUND);
        assertApiError(updateFailure, ObjectNotFoundException.class, ERROR_CODE_NOT_FOUND);
        assertApiError(createDraftFailure, ObjectNotFoundException.class, ERROR_CODE_NOT_FOUND);
        assertApiError(approveFailure, ObjectNotFoundException.class, ERROR_CODE_NOT_FOUND);
    }

    private MarketoFragmentV2Client newClient(RetryPolicy retryPolicy)
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
                retryPolicy);

        return new MarketoRestClientManager(retryingExecutor).getMarketoFragmentV2Client();
    }

    private void stubGetResponse(String path, String responseBody)
    {
        wireMockRule.stubFor(get(urlPathEqualTo(path))
                .willReturn(jsonResponse(responseBody)));
    }

    private void stubPostResponse(String path, String responseBody)
    {
        wireMockRule.stubFor(post(urlPathEqualTo(path))
                .willReturn(jsonResponse(responseBody)));
    }

    private void verifyJsonPost(String path, Map<String, Object> expectedBody) throws Exception
    {
        wireMockRule.verify(1, postRequestedFor(urlPathEqualTo(path))
                .withHeader(CONTENT_TYPE_HEADER, equalTo(JSON_REQUEST_CONTENT_TYPE))
                .withHeader(APP_TYPE_HEADER, equalTo(APP_TYPE))
                .withHeader(AUTHORIZATION_HEADER, equalTo(BEARER_TOKEN))
                .withRequestBody(equalToJson(json(expectedBody), false, false)));
    }

    private String json(Object value) throws Exception
    {
        return objectMapper.writeValueAsString(value);
    }

    private static Map<String, Object> transitionBody(String action)
    {
        return ImmutableMap.of("contentId", FRAGMENT_ID, "action", action);
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
