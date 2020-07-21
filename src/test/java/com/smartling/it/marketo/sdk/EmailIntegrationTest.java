package com.smartling.it.marketo.sdk;

import com.smartling.marketo.sdk.domain.email.Email;
import com.smartling.marketo.sdk.domain.Asset.Status;
import com.smartling.marketo.sdk.domain.email.EmailContentItem;
import com.smartling.marketo.sdk.domain.email.EmailDynamicContentItem;
import com.smartling.marketo.sdk.domain.email.EmailFullContent;
import com.smartling.marketo.sdk.domain.email.EmailSnippetContentItem;
import com.smartling.marketo.sdk.domain.email.EmailTextContentItem;
import com.smartling.marketo.sdk.domain.email.EmailVariable;
import com.smartling.marketo.sdk.domain.folder.FolderId;
import com.smartling.marketo.sdk.domain.folder.FolderType;
import com.smartling.marketo.sdk.MarketoApiException;
import com.smartling.marketo.sdk.MarketoEmailClient;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.fest.assertions.api.Assertions.assertThat;

public class EmailIntegrationTest extends BaseIntegrationTest {
    private static final int TEST_EMAIL_ID = 1109;
    private static final int TEST_EMAIL_V2_ID = 2466;
    private static final int TEST_EMAIL_WITH_SNIPPET_ID = 2180;
    private static final int TEST_EMAIL_WITH_DYNAMIC_CONTENT_ID = 2193;
    private static final String TEST_EMAIL_NAME = "Email For Integration Tests";
    private static final FolderId TEST_FOLDER_ID = new FolderId(44, FolderType.FOLDER);
    private static final int TEST_PROGRAM_EMAIL_ID = 1596;
    private static final int TEST_PROGRAM_ID = 1008;

    @Rule
    public final ExpectedException thrown = ExpectedException.none();
    private MarketoEmailClient marketoEmailClient;

    @Before
    public void setUp() {
        marketoEmailClient = marketoClientManager.getMarketoEmailClient();
    }

    @Test
    public void shouldListEmails() throws Exception {
        List<Email> emails = marketoEmailClient.listEmails(0, 1);

        assertThat(emails).hasSize(1);
        assertThat(emails.get(0).getId()).isPositive();
        assertThat(emails.get(0).getName()).isNotEmpty();
        assertThat(emails.get(0).getUpdatedAt()).isNotNull();
        assertThat(emails.get(0).getStatus()).isNotNull();
        assertThat(emails.get(0).getUrl()).isNotEmpty();
        assertThat(emails.get(0).getFolder()).isNotNull();
        assertThat(emails.get(0).getVersion()).isPositive();
    }

    @Test
    public void shouldListEmailsWithFilter() throws Exception {
        List<Email> emails = marketoEmailClient.listEmails(0, 1, TEST_FOLDER_ID, Status.APPROVED);

        assertThat(emails).hasSize(1);
        assertThat(emails.get(0).getId()).isPositive();
        assertThat(emails.get(0).getName()).isNotEmpty();
        assertThat(emails.get(0).getUpdatedAt()).isNotNull();
        assertThat(emails.get(0).getStatus()).isNotNull();
        assertThat(emails.get(0).getUrl()).isNotEmpty();
        assertThat(emails.get(0).getFolder()).isNotNull();
        assertThat(emails.get(0).getVersion()).isPositive();
    }

    @Test
    public void shouldReturnEmptyListWhenEndReached() throws Exception {
        List<Email> emails = marketoEmailClient.listEmails(10000, 1);

        assertThat(emails).isEmpty();
    }

    @Test(expected = MarketoApiException.class)
    public void shouldThrowLogicException() throws Exception {
        marketoEmailClient.listEmails(-5, 5);
    }

    @Test
    public void shouldLoadEmailById() throws Exception {
        Email email = marketoEmailClient.loadEmailById(TEST_EMAIL_V2_ID);

        assertThat(email).isNotNull();
        assertThat(email.getId()).isEqualTo(TEST_EMAIL_V2_ID);
        assertThat(email.getName()).isNotEmpty();
        assertThat(email.getSubject()).isNotEmpty();
        assertThat(email.getFromName()).isNotEmpty();
        assertThat(email.getUrl()).isNotEmpty();
        assertThat(email.getFolder()).isNotNull();
        assertThat(email.getVersion()).isPositive();
        assertThat(email.getTemplate()).isNotNull();
        assertThat(email.getPreHeader()).isNotNull();
    }

    @Test
    public void shouldThrowMarketoApiExceptionWhenCouldNotFindEmailById() throws Exception {

        thrown.expect(MarketoApiException.class);
        thrown.expectMessage("Email[id = 42] not found");

        marketoEmailClient.loadEmailById(42);
    }

    @Test
    public void shouldGetEmailsByName() throws Exception {
        List<Email> emails = marketoEmailClient.getEmailsByName(TEST_EMAIL_NAME, null, null);

        assertThat(emails).haveAtLeast(1, new EntityWithName(TEST_EMAIL_NAME));
    }

    @Test
    public void shouldGetEmailsByNameWithFolder() throws Exception {
        List<Email> emails = marketoEmailClient.getEmailsByName(TEST_EMAIL_NAME, TEST_FOLDER_ID, null);

        assertThat(emails).haveAtLeast(1, new EntityWithNameAndFolderId(TEST_EMAIL_NAME, TEST_FOLDER_ID));
    }

    @Test
    public void shouldGetEmailsByNameWithStatus() throws Exception {
        List<Email> emails = marketoEmailClient.getEmailsByName(TEST_EMAIL_NAME, null, Status.APPROVED);

        assertThat(emails).haveAtLeast(1, new AssetWithNameAndStatus(TEST_EMAIL_NAME, Status.APPROVED));
    }

    @Test
    public void shouldReadEmailContent() throws Exception {
        List<EmailContentItem> contentItems = marketoEmailClient.loadEmailContent(TEST_EMAIL_ID);

        assertThat(contentItems).hasSize(2);
        assertThat(contentItems.get(0)).isInstanceOf(EmailTextContentItem.class);
        EmailTextContentItem textContentItem = (EmailTextContentItem)contentItems.get(0);
        assertThat(textContentItem.getHtmlId()).isEqualTo("greeting");
        assertThat(textContentItem.getValue()).hasSize(2);
        assertThat(textContentItem.getValue().get(0).getType()).isEqualTo("HTML");
        assertThat(textContentItem.getValue().get(0).getValue()).isNotEmpty();
        assertThat(textContentItem.getValue().get(1).getType()).isEqualTo("Text");
        assertThat(textContentItem.getValue().get(1).getValue()).isNotEmpty();
    }

    @Test
    public void shouldNotFailOnUnsupportedContentItems() throws Exception {
        List<EmailContentItem> contentItems = marketoEmailClient.loadEmailContent(TEST_EMAIL_V2_ID);

        assertThat(contentItems).hasSize(59);
    }

    @Test
    public void shouldReadEmailContentWithSnippetItem() throws Exception {
        List<EmailContentItem> contentItems = marketoEmailClient.loadEmailContent(TEST_EMAIL_WITH_SNIPPET_ID);

        assertThat(contentItems).hasSize(2);
        assertThat(contentItems.get(0)).isInstanceOf(EmailTextContentItem.class);
        assertThat(contentItems.get(1)).isInstanceOf(EmailSnippetContentItem.class);
        EmailSnippetContentItem snippetContentItem = (EmailSnippetContentItem)contentItems.get(1);
        assertThat(snippetContentItem.getHtmlId()).isEqualTo("sign");
        assertThat(snippetContentItem.getValue()).isEqualTo("2");
    }

    @Test
    public void shouldReadEmailContentWithDynamicContentItem() throws Exception {
        List<EmailContentItem> contentItems = marketoEmailClient.loadEmailContent(TEST_EMAIL_WITH_DYNAMIC_CONTENT_ID);

        assertThat(contentItems).hasSize(2);
        assertThat(contentItems.get(0)).isInstanceOf(EmailTextContentItem.class);
        assertThat(contentItems.get(1)).isInstanceOf(EmailDynamicContentItem.class);
        EmailDynamicContentItem dynamicContentItem = (EmailDynamicContentItem)contentItems.get(1);
        assertThat(dynamicContentItem.getHtmlId()).isEqualTo("sign");
        assertThat(dynamicContentItem.getValue()).isEqualTo("RVMtc2lnbg==");
    }

    @Test
    public void shouldCloneEmail() throws Exception {
        String newEmailName = "integration-test-clone-" + UUID.randomUUID().toString();

        Email clone = marketoEmailClient.cloneEmail(TEST_EMAIL_ID, newEmailName, TEST_FOLDER_ID);

        assertThat(clone).isNotNull();
        assertThat(clone.getId()).isPositive();
        assertThat(clone.getName()).isEqualTo(newEmailName);
        assertThat(new FolderId(clone.getFolder())).isEqualTo(TEST_FOLDER_ID);
    }

    @Test
    public void shouldCloneEmailInProgram() throws Exception {
        String newEmailName = "integration-test-clone-" + UUID.randomUUID().toString();

        Email clone = marketoEmailClient.cloneEmail(TEST_PROGRAM_EMAIL_ID, newEmailName, new FolderId(TEST_PROGRAM_ID, FolderType.PROGRAM));

        assertThat(clone).isNotNull();
        assertThat(clone.getId()).isPositive();
        assertThat(clone.getName()).isEqualTo(newEmailName);
        assertThat(clone.getFolder().getValue()).isEqualTo(TEST_PROGRAM_ID);
    }
    @Test
    public void shouldCloneEmailViaShorthandMethod() throws Exception {
        String newEmailName = "integration-test-clone-" + UUID.randomUUID().toString();
        Email existingEmail = marketoEmailClient.loadEmailById(TEST_EMAIL_ID);

        Email clone = marketoEmailClient.cloneEmail(existingEmail, newEmailName);

        assertThat(clone).isNotNull();
        assertThat(clone.getId()).isPositive();
        assertThat(clone.getName()).isEqualTo(newEmailName);
        assertThat(clone.getFolder().getValue()).isEqualTo(existingEmail.getFolder().getValue());
    }

    @Test
    public void shouldUpdateEmailContent() throws Exception {
        EmailTextContentItem newItem = new EmailTextContentItem();
        newItem.setHtmlId("greeting");
        newItem.setContentType("Text");
        newItem.setValue(Arrays.asList(new EmailTextContentItem.Value(), new EmailTextContentItem.Value()));
        newItem.getValue().get(0).setType("HTML");
        newItem.getValue().get(0).setValue("<strong>" + UUID.randomUUID() + "<strong>");
        newItem.getValue().get(1).setType("Text");
        newItem.getValue().get(1).setValue(UUID.randomUUID().toString());

        marketoEmailClient.updateEmailContent(TEST_EMAIL_ID, Collections.singletonList(newItem));

        // Can not verify - no way to fetch not approved content
    }

    @Test
    public void shouldUpdateNullableValueEmailContent() throws Exception {
        EmailTextContentItem newItem = new EmailTextContentItem();
        newItem.setHtmlId("greeting");
        newItem.setContentType("Text");
        newItem.setValue(Arrays.asList(new EmailTextContentItem.Value(), new EmailTextContentItem.Value()));
        newItem.getValue().get(0).setType("HTML");
        newItem.getValue().get(0).setValue(null);
        newItem.getValue().get(1).setType("Text");
        newItem.getValue().get(1).setValue("");

        marketoEmailClient.updateEmailContent(TEST_EMAIL_ID, Collections.singletonList(newItem));

        // Can not verify - no way to fetch not approved content
    }

    @Test
    public void shouldUpdateHtmlOnlyValueEmailContent() throws Exception {
        EmailTextContentItem newItem = new EmailTextContentItem();
        newItem.setHtmlId("greeting");
        newItem.setContentType("Text");
        newItem.setValue(Arrays.asList(new EmailTextContentItem.Value(), new EmailTextContentItem.Value()));
        newItem.getValue().get(0).setType("HTML");
        newItem.getValue().get(0).setValue("<strong>" + UUID.randomUUID() + "<strong>");
        newItem.getValue().get(1).setType("Text");
        newItem.getValue().get(1).setValue(null);

        marketoEmailClient.updateEmailContent(TEST_EMAIL_ID, Collections.singletonList(newItem));

        // Can not verify - no way to fetch not approved content
    }

    @Test
    public void shouldUpdateEmailSubject() throws Exception {

        Email email = new Email();
        email.setId(TEST_EMAIL_ID);
        email.setSubject("Subject from integration test");
        email.setFromName("From SDK committer");

        marketoEmailClient.updateEmail(email);

        // Can not verify - no way to fetch not approved content
    }

    @Test
    public void shouldSendSample() throws Exception {

        marketoEmailClient.sendSample(TEST_EMAIL_ID, "connectors-context+ignore-int-tests@smartling.com", true);

        // Can not verify - no way to fetch sent sample
    }

    @Test
    public void shouldGetEmailVariables() throws Exception {
        List<EmailVariable> variables = marketoEmailClient.getEmailVariables(TEST_EMAIL_V2_ID);

        assertThat(variables).hasSize(94);
        assertThat(variables.get(5).getName()).isEqualTo("twoArticlesLinkText");
        assertThat(variables.get(5).getValue()).isEqualTo("READ MORE");
    }

    @Test
    public void shouldGetEmailFullContent() throws Exception {
        EmailFullContent fullContent = marketoEmailClient.getEmailFullContent(TEST_EMAIL_V2_ID, Status.APPROVED);

        assertThat(fullContent.getContent()).isNotEmpty();
    }
}
