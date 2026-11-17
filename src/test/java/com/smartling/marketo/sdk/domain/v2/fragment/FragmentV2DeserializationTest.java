package com.smartling.marketo.sdk.domain.v2.fragment;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartling.marketo.sdk.rest.transport.ObjectMapperProvider;
import org.junit.Test;

import java.io.InputStream;
import java.time.Instant;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class FragmentV2DeserializationTest
{
    private static final String FILTER_FIXTURE = "/json/fragment-v2-filter-response.json";
    private static final String GET_FIXTURE = "/json/fragment-v2-get-response.json";
    private static final String PUBLISHED_WITH_DRAFT_FIXTURE = "/json/fragment-v2-get-published-with-draft-response.json";
    private static final String FRAGMENT_ID = "0123456789abcdef01234567";
    private static final String DRAFT_FRAGMENT_ID = "fragment-id-2";
    private static final String DRAFT_CONTENT_ID = "draft-content-id-1";
    private static final String FRAGMENT_NAME = "fragment-1";
    private static final String FRAGMENT_DESCRIPTION = "A test fragment";
    private static final String APP_TYPE = "marketo";
    private static final String PERSON_NAME = "John Doe";
    private static final String VIRTUAL_ID = "virtual-id-1";
    private static final String EXTERNAL_ID = "external-id-1";
    private static final String WORKSPACE_ID = "workspace-id-1";
    private static final String FOLDER_ID = "folder-id-1";
    private static final String THEME_ID = "theme-id-1";
    private static final String SCRIPT_ENGINE = "VELOCITY";
    private static final String FRAGMENT_TYPE = "visual";
    private static final String CHANNEL_EMAIL = "email";
    private static final String STATE_PUBLISHED = "published";
    private static final String STATE_DRAFT = "draft";
    private static final String HTML_BODY = "<div>Hello</div>";
    private static final String TEXT_BODY = "Hello";
    private static final Date CREATED_AT = Date.from(Instant.parse("2026-01-02T03:04:05Z"));
    private static final Date MODIFIED_AT = Date.from(Instant.parse("2026-01-03T04:05:06Z"));

    private final ObjectMapper objectMapper = new ObjectMapperProvider().getContext(null);

    @Test
    public void shouldDeserializeFilterResponse() throws Exception
    {
        JsonNode result = readFixture(FILTER_FIXTURE).get("result");

        // Deserialize the list response result
        FragmentV2Page page = objectMapper.treeToValue(result, FragmentV2Page.class);

        assertThat(page.getTotalItems()).isEqualTo(3);
        assertThat(page.getPageSize()).isEqualTo(50);
        assertThat(page.getCurrentPage()).isZero();
        assertThat(page.getItems()).hasSize(3);

        // Published item: whole object, without data
        FragmentV2 published = page.getItems().get(0);
        assertThat(published).usingRecursiveComparison().isEqualTo(expectedPublishedFragment());
        assertThat(published.getData()).isNull();

        // Draft item: sub type and the single associated state pointing to itself
        FragmentV2 draft = page.getItems().get(1);
        assertThat(draft.getId()).isEqualTo(DRAFT_FRAGMENT_ID);
        assertThat(draft.getSettings().getFragmentSubType()).isEqualTo("html");
        assertThat(draft.getAssociatedStates())
                .usingRecursiveFieldByFieldElementComparator()
                .containsExactly(associatedState(DRAFT_FRAGMENT_ID, "external-id-2", STATE_DRAFT));
        assertThat(draft.getData()).isNull();

        // Item without the optional keys
        FragmentV2 stripped = page.getItems().get(2);
        assertThat(stripped.getId()).isEqualTo("fragment-id-3");
        assertThat(stripped.getDescription()).isNull();
        assertThat(stripped.getThemeId()).isNull();
        assertThat(stripped.getSettings().getFragmentSubType()).isNull();
        assertThat(stripped.getData()).isNull();
    }

    @Test
    public void shouldDeserializeGetResponse() throws Exception
    {
        JsonNode result = readFixture(GET_FIXTURE).get("result");
        assertThat(result.size()).isEqualTo(1);

        FragmentV2 expected = expectedPublishedFragment();
        expected.setData(expectedData());

        // Deserialize the first element of the get response result array
        FragmentV2 fragment = objectMapper.treeToValue(result.get(0), FragmentV2.class);

        assertThat(fragment).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    public void shouldDeserializePublishedWithDraftGetResponse() throws Exception
    {
        JsonNode result = readFixture(PUBLISHED_WITH_DRAFT_FIXTURE).get("result");

        // Deserialize the first element of the get response result array
        FragmentV2 fragment = objectMapper.treeToValue(result.get(0), FragmentV2.class);

        assertThat(fragment.getStatus()).isEqualTo("published_with_draft");
        assertThat(fragment.getState()).isEqualTo(STATE_PUBLISHED);
        assertThat(fragment.getAssociatedStates()).hasSize(2);
        assertThat(stateEntry(fragment, STATE_PUBLISHED))
                .usingRecursiveComparison()
                .isEqualTo(associatedState(FRAGMENT_ID, EXTERNAL_ID, STATE_PUBLISHED));
        assertThat(stateEntry(fragment, STATE_DRAFT))
                .usingRecursiveComparison()
                .isEqualTo(associatedState(DRAFT_CONTENT_ID, null, STATE_DRAFT));
    }

    @Test
    public void shouldDeserializeListItemDates() throws Exception
    {
        JsonNode result = readFixture(FILTER_FIXTURE).get("result");

        // Deserialize the list response result
        FragmentV2Page page = objectMapper.treeToValue(result, FragmentV2Page.class);

        FragmentV2.Metadata metadata = page.getItems().get(0).getMetadata();
        assertThat(metadata.getCreatedAt()).isEqualTo(CREATED_AT);
        assertThat(metadata.getModifiedAt()).isEqualTo(MODIFIED_AT);
        assertThat(result.get("items").get(0).get("appData").has("editorType")).isTrue();
    }

    @Test
    public void shouldReturnEmptyListWhenItemsIsNull() throws Exception
    {
        String json = "{\"currentPage\":0,\"pageSize\":50,\"totalItems\":0,\"items\":null}";

        FragmentV2Page page = objectMapper.readValue(json, FragmentV2Page.class);

        assertThat(page.getItems()).isNotNull().isEmpty();
    }

    @Test
    public void shouldReturnEmptyPage()
    {
        FragmentV2Page page = FragmentV2Page.empty();

        assertThat(page.getItems()).isEmpty();
        assertThat(page.getTotalItems()).isZero();
        assertThat(page.getPageSize()).isZero();
        assertThat(page.getCurrentPage()).isZero();
    }

    private JsonNode readFixture(String path) throws Exception
    {
        try (InputStream stream = getClass().getResourceAsStream(path))
        {
            return objectMapper.readTree(stream);
        }
    }

    private static FragmentV2.AssociatedState stateEntry(FragmentV2 fragment, String state)
    {
        List<FragmentV2.AssociatedState> matching = fragment.getAssociatedStates().stream()
                .filter(associatedState -> state.equals(associatedState.getState()))
                .collect(Collectors.toList());
        assertThat(matching).hasSize(1);
        return matching.get(0);
    }

    private static FragmentV2 expectedPublishedFragment()
    {
        FragmentV2.Settings settings = new FragmentV2.Settings();
        settings.setFragmentType(FRAGMENT_TYPE);
        settings.setSupportedChannels(Collections.singletonList(CHANNEL_EMAIL));

        FragmentV2.Metadata metadata = new FragmentV2.Metadata();
        metadata.setCreatedBy(PERSON_NAME);
        metadata.setCreatedAt(CREATED_AT);
        metadata.setModifiedBy(PERSON_NAME);
        metadata.setModifiedAt(MODIFIED_AT);

        FragmentV2.AppData appData = new FragmentV2.AppData();
        appData.setWorkspaceId(WORKSPACE_ID);
        appData.setFolderId(FOLDER_ID);

        FragmentV2 fragment = new FragmentV2();
        fragment.setId(FRAGMENT_ID);
        fragment.setName(FRAGMENT_NAME);
        fragment.setDescription(FRAGMENT_DESCRIPTION);
        fragment.setStatus(STATE_PUBLISHED);
        fragment.setState(STATE_PUBLISHED);
        fragment.setAppType(APP_TYPE);
        fragment.setExternalId(EXTERNAL_ID);
        fragment.setVirtualId(VIRTUAL_ID);
        fragment.setScriptEngine(SCRIPT_ENGINE);
        fragment.setThemeId(THEME_ID);
        fragment.setSettings(settings);
        fragment.setMetadata(metadata);
        fragment.setAppData(appData);
        fragment.setAssociatedStates(Collections.singletonList(associatedState(FRAGMENT_ID, EXTERNAL_ID, STATE_PUBLISHED)));
        return fragment;
    }

    private static FragmentV2.Data expectedData()
    {
        FragmentV2.Html html = new FragmentV2.Html();
        html.setBody(HTML_BODY);

        FragmentV2.Text text = new FragmentV2.Text();
        text.setBody(TEXT_BODY);
        text.setSyncFromHtml(true);

        FragmentV2.Data fragmentData = new FragmentV2.Data();
        fragmentData.setHtml(html);
        fragmentData.setText(text);
        return fragmentData;
    }

    private static FragmentV2.AssociatedState associatedState(String contentId, String externalId, String state)
    {
        FragmentV2.AssociatedState associatedState = new FragmentV2.AssociatedState();
        associatedState.setContentId(contentId);
        associatedState.setExternalId(externalId);
        associatedState.setState(state);
        return associatedState;
    }
}
