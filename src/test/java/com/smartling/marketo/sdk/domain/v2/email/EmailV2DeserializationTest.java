package com.smartling.marketo.sdk.domain.v2.email;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartling.marketo.sdk.rest.transport.ObjectMapperProvider;
import org.junit.Test;

import java.io.InputStream;
import java.time.Instant;
import java.util.Collections;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

public class EmailV2DeserializationTest
{
    private static final String FILTER_FIXTURE = "/json/email-v2-filter-response.json";
    private static final String GET_FIXTURE = "/json/email-v2-get-response.json";
    private static final String CREATE_DRAFT_FIXTURE = "/json/email-v2-create-draft-response.json";
    private static final String CLONE_FIXTURE = "/json/email-v2-clone-response.json";
    private static final String STATE_DRAFT = "draft";
    private static final String EMAIL_ID = "1001";
    private static final String EMAIL_NAME = "Test Email";
    private static final String EMAIL_DESCRIPTION = "A test email";
    private static final String APP_TYPE = "marketo";
    private static final String SUBJECT = "Test Subject";
    private static final String FROM_EMAIL = "john.doe@example.com";
    private static final String PERSON_NAME = "John Doe";
    private static final String STATUS = "draft";
    private static final String VIRTUAL_ID = "virtual-id-1";
    private static final String WORKSPACE_ID = "3";
    private static final String FOLDER_ID = "2001";
    private static final String TEMPLATE_ID = "template-1";
    private static final String SCRIPT_ENGINE = "VELOCITY";
    private static final String HTML_BODY = "<html><body><h1>Hello</h1></body></html>";
    private static final String TEXT_BODY = "Hello";

    private final ObjectMapper objectMapper = new ObjectMapperProvider().getContext(null);

    @Test
    public void shouldDeserializeFilterResponseIntoPage() throws Exception
    {
        JsonNode result = readFixture(FILTER_FIXTURE).get("result");

        EmailV2 expectedItem = expectedEmail(
                Date.from(Instant.parse("2026-10-30T12:30:07Z")),
                Date.from(Instant.parse("2026-10-30T12:33:01Z")));

        EmailV2Page expected = new EmailV2Page();
        expected.setCurrentPage(0);
        expected.setPageSize(50);
        expected.setTotalItems(1);
        expected.setItems(Collections.singletonList(expectedItem));

        // Deserialize the list response result
        EmailV2Page page = objectMapper.treeToValue(result, EmailV2Page.class);

        assertThat(page).usingRecursiveComparison().isEqualTo(expected);
        assertThat(page.getItems().get(0).getData()).isNull();
    }

    @Test
    public void shouldDeserializeGetResponseIntoEmail() throws Exception
    {
        JsonNode result = readFixture(GET_FIXTURE).get("result");

        EmailV2.Html html = new EmailV2.Html();
        html.setBody(HTML_BODY);

        EmailV2.Text text = new EmailV2.Text();
        text.setBody(TEXT_BODY);
        text.setSyncFromHtml(true);

        EmailV2.Data data = new EmailV2.Data();
        data.setHtml(html);
        data.setText(text);

        EmailV2 expected = expectedEmail(
                Date.from(Instant.parse("2026-10-26T15:58:13Z")),
                Date.from(Instant.parse("2026-10-30T10:31:55Z")));
        expected.setData(data);

        // Deserialize the first element of the get response result array
        EmailV2 email = objectMapper.treeToValue(result.get(0), EmailV2.class);

        assertThat(email).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    public void shouldDeserializeCreateDraftResponseWithPublishedAndDraftStates() throws Exception
    {
        JsonNode result = readFixture(CREATE_DRAFT_FIXTURE).get("result");

        // Deserialize the first element of the create-draft response result array
        EmailV2 email = objectMapper.treeToValue(result.get(0), EmailV2.class);

        assertThat(email.getId()).isEqualTo("1002");
        assertThat(email.getStatus()).isEqualTo("published_with_draft");
        assertThat(email.getAssociatedStates()).usingRecursiveFieldByFieldElementComparator().containsExactly(
                associatedState(EMAIL_ID, EMAIL_ID, "published"),
                associatedState("draft-content-id-1", null, STATE_DRAFT));
    }

    @Test
    public void shouldDeserializeCloneResponseWithAllSeventeenFields() throws Exception
    {
        JsonNode result = readFixture(CLONE_FIXTURE).get("result");
        assertThat(result.get(0).size()).isEqualTo(17);

        EmailV2.Html html = new EmailV2.Html();
        html.setBody(HTML_BODY);

        EmailV2.Text text = new EmailV2.Text();
        text.setBody(TEXT_BODY);
        text.setSyncFromHtml(true);

        EmailV2.Data data = new EmailV2.Data();
        data.setHtml(html);
        data.setText(text);

        EmailV2 expected = expectedEmail(
                Date.from(Instant.parse("2026-10-30T10:31:55Z")),
                Date.from(Instant.parse("2026-10-30T10:31:55Z")));
        expected.setId("1003");
        expected.setName("Test Email Copy");
        expected.setVirtualId("virtual-id-2");
        expected.setExternalId("1003");
        expected.setAssociatedStates(Collections.singletonList(associatedState("1003", "1003", STATE_DRAFT)));
        expected.setData(data);

        // Deserialize the first element of the clone response result array
        EmailV2 email = objectMapper.treeToValue(result.get(0), EmailV2.class);

        assertThat(email).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    public void shouldLeaveAssociatedStatesNullWhenAbsent() throws Exception
    {
        EmailV2 email = objectMapper.readValue("{\"id\":\"1001\"}", EmailV2.class);

        assertThat(email.getAssociatedStates()).isNull();
    }

    @Test
    public void shouldReturnEmptyItemsWhenPageHasNoItems() throws Exception
    {
        String json = "{\"currentPage\":0,\"pageSize\":50,\"totalItems\":0}";

        EmailV2Page page = objectMapper.readValue(json, EmailV2Page.class);

        assertThat(page.getItems()).isNotNull().isEmpty();
    }

    @Test
    public void shouldReturnEmptyItemsWhenItemsAreNull() throws Exception
    {
        String json = "{\"currentPage\":0,\"pageSize\":50,\"totalItems\":0,\"items\":null}";

        EmailV2Page page = objectMapper.readValue(json, EmailV2Page.class);

        assertThat(page.getItems()).isNotNull().isEmpty();
    }

    @Test
    public void shouldReturnEmptyPage()
    {
        EmailV2Page page = EmailV2Page.empty();

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

    private static EmailV2 expectedEmail(Date createdAt, Date modifiedAt)
    {
        EmailV2.Headers headers = new EmailV2.Headers();
        headers.setSubject(SUBJECT);
        headers.setFromEmail(FROM_EMAIL);
        headers.setFromName(PERSON_NAME);
        headers.setReplyEmail(FROM_EMAIL);

        EmailV2.Metadata metadata = new EmailV2.Metadata();
        metadata.setCreatedBy(PERSON_NAME);
        metadata.setCreatedAt(createdAt);
        metadata.setModifiedBy(PERSON_NAME);
        metadata.setModifiedAt(modifiedAt);

        EmailV2.AppData appData = new EmailV2.AppData();
        appData.setWorkspaceId(WORKSPACE_ID);
        appData.setFolderId(FOLDER_ID);

        EmailV2 email = new EmailV2();
        email.setId(EMAIL_ID);
        email.setName(EMAIL_NAME);
        email.setDescription(EMAIL_DESCRIPTION);
        email.setAppType(APP_TYPE);
        email.setHeaders(headers);
        email.setMetadata(metadata);
        email.setStatus(STATUS);
        email.setState(STATUS);
        email.setVirtualId(VIRTUAL_ID);
        email.setAppData(appData);
        email.setTemplateId(TEMPLATE_ID);
        email.setExternalId(EMAIL_ID);
        email.setScriptEngine(SCRIPT_ENGINE);
        email.setAssociatedStates(Collections.singletonList(associatedState(EMAIL_ID, EMAIL_ID, STATE_DRAFT)));
        return email;
    }

    private static EmailV2.AssociatedState associatedState(String contentId, String externalId, String state)
    {
        EmailV2.AssociatedState associatedState = new EmailV2.AssociatedState();
        associatedState.setContentId(contentId);
        associatedState.setExternalId(externalId);
        associatedState.setState(state);
        return associatedState;
    }
}
