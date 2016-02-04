package com.smartling.it.marketo.sdk;

import com.smartling.marketo.sdk.AuthenticationErrorException;
import com.smartling.marketo.sdk.Email;
import com.smartling.marketo.sdk.Asset.Status;
import com.smartling.marketo.sdk.EmailContentItem;
import com.smartling.marketo.sdk.EmailDynamicContentItem;
import com.smartling.marketo.sdk.EmailSnippetContentItem;
import com.smartling.marketo.sdk.EmailTextContentItem;
import com.smartling.marketo.sdk.FolderDetails;
import com.smartling.marketo.sdk.FolderId;
import com.smartling.marketo.sdk.FolderType;
import com.smartling.marketo.sdk.MarketoApiException;
import com.smartling.marketo.sdk.MarketoClient;
import com.smartling.marketo.sdk.rest.MarketoRestClient;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.fest.assertions.api.Assertions.assertThat;

public class EmailIntegrationTest extends BaseIntegrationTest {
    private static final int TEST_EMAIL_ID = 1109;
    private static final int TEST_EMAIL_WITH_SNIPPET_ID = 2180;
    private static final int TEST_EMAIL_WITH_DYNAMIC_CONTENT_ID = 2193;
    private static final String TEST_EMAIL_NAME = "Email For Integration Tests";
    private static final FolderId TEST_FOLDER_ID = new FolderId(44, FolderType.FOLDER);
    private static final int TEST_PROGRAM_EMAIL_ID = 1596;
    private static final int TEST_PROGRAM_ID = 1008;

    @Rule
    public final ExpectedException thrown = ExpectedException.none();

    @Test
    public void shouldListEmails() throws Exception {
        List<Email> emails = marketoClient.listEmails(0, 1);

        assertThat(emails).hasSize(1);
        assertThat(emails.get(0).getId()).isPositive();
        assertThat(emails.get(0).getName()).isNotEmpty();
        assertThat(emails.get(0).getUpdatedAt()).isNotNull();
        assertThat(emails.get(0).getStatus()).isNotNull();
        assertThat(emails.get(0).getUrl()).isNotEmpty();
        assertThat(emails.get(0).getFolder()).isNotNull();
    }

    @Test
    public void shouldListEmailsWithFilter() throws Exception {
        List<Email> emails = marketoClient.listEmails(0, 1, TEST_FOLDER_ID, Status.APPROVED);

        assertThat(emails).hasSize(1);
        assertThat(emails.get(0).getId()).isPositive();
        assertThat(emails.get(0).getName()).isNotEmpty();
        assertThat(emails.get(0).getUpdatedAt()).isNotNull();
        assertThat(emails.get(0).getStatus()).isNotNull();
        assertThat(emails.get(0).getUrl()).isNotEmpty();
        assertThat(emails.get(0).getFolder()).isNotNull();
    }

    @Test
    public void shouldReturnEmptyListWhenEndReached() throws Exception {
        List<Email> emails = marketoClient.listEmails(10000, 1);

        assertThat(emails).isEmpty();
    }

    @Test(expected = AuthenticationErrorException.class)
    public void shouldThrowAuthenticationError() throws Exception {
        MarketoClient invalid = MarketoRestClient.create(identityEndpoint, restEndpoint).withCredentials("notCachedClientId", "invalid");
        invalid.listEmails(0, 1);
    }

    @Test(expected = MarketoApiException.class)
    public void shouldThrowLogicException() throws Exception {
        marketoClient.listEmails(-5, 5);
    }

    @Test
    public void shouldLoadEmailById() throws Exception {
        Email email = marketoClient.loadEmailById(TEST_EMAIL_ID);

        assertThat(email).isNotNull();
        assertThat(email.getId()).isEqualTo(TEST_EMAIL_ID);
        assertThat(email.getName()).isNotEmpty();
        assertThat(email.getSubject()).isNotEmpty();
        assertThat(email.getFromName()).isNotEmpty();
        assertThat(email.getUrl()).isNotEmpty();
        assertThat(email.getFolder()).isNotNull();
    }

    @Test
    public void shouldThrowMarketoApiExceptionWhenCouldNotFindEmailById() throws Exception {

        thrown.expect(MarketoApiException.class);
        thrown.expectMessage("Email[id = 42] not found");

        marketoClient.loadEmailById(42);
    }

    @Test
    public void shouldGetEmailsByName() throws Exception {
        List<Email> emails = marketoClient.getEmailsByName(TEST_EMAIL_NAME, null, null);

        assertThat(emails).haveAtLeast(1, new AssetWithName(TEST_EMAIL_NAME));
    }

    @Test
    public void shouldGetEmailsByNameWithFolder() throws Exception {
        List<Email> emails = marketoClient.getEmailsByName(TEST_EMAIL_NAME, TEST_FOLDER_ID, null);

        assertThat(emails).haveAtLeast(1, new AssetWithNameAndFolderId(TEST_EMAIL_NAME, TEST_FOLDER_ID));
    }

    @Test
    public void shouldGetEmailsByNameWithStatus() throws Exception {
        List<Email> emails = marketoClient.getEmailsByName(TEST_EMAIL_NAME, null, Status.APPROVED);

        assertThat(emails).haveAtLeast(1, new AssetWithNameAndStatus(TEST_EMAIL_NAME, Status.APPROVED));
    }

    @Test
    public void shouldReadEmailContent() throws Exception {
        List<EmailContentItem> contentItems = marketoClient.loadEmailContent(TEST_EMAIL_ID);

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
    public void shouldReadEmailContentWithSnippetItem() throws Exception {
        List<EmailContentItem> contentItems = marketoClient.loadEmailContent(TEST_EMAIL_WITH_SNIPPET_ID);

        assertThat(contentItems).hasSize(2);
        assertThat(contentItems.get(0)).isInstanceOf(EmailTextContentItem.class);
        assertThat(contentItems.get(1)).isInstanceOf(EmailSnippetContentItem.class);
        EmailSnippetContentItem snippetContentItem = (EmailSnippetContentItem)contentItems.get(1);
        assertThat(snippetContentItem.getHtmlId()).isEqualTo("sign");
        assertThat(snippetContentItem.getValue()).isEqualTo("2");
    }

    @Test
    public void shouldReadEmailContentWithDynamicContentItem() throws Exception {
        List<EmailContentItem> contentItems = marketoClient.loadEmailContent(TEST_EMAIL_WITH_DYNAMIC_CONTENT_ID);

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

        Email clone = marketoClient.cloneEmail(TEST_EMAIL_ID, newEmailName, TEST_FOLDER_ID);

        assertThat(clone).isNotNull();
        assertThat(clone.getId()).isPositive();
        assertThat(clone.getName()).isEqualTo(newEmailName);
        assertThat(new FolderId(clone.getFolder())).isEqualTo(TEST_FOLDER_ID);
    }

    @Test
    public void shouldCloneEmailInProgram() throws Exception {
        String newEmailName = "integration-test-clone-" + UUID.randomUUID().toString();

        Email clone = marketoClient.cloneEmail(TEST_PROGRAM_EMAIL_ID, newEmailName, new FolderId(TEST_PROGRAM_ID, FolderType.PROGRAM));

        assertThat(clone).isNotNull();
        assertThat(clone.getId()).isPositive();
        assertThat(clone.getName()).isEqualTo(newEmailName);
        assertThat(clone.getFolder().getValue()).isEqualTo(TEST_PROGRAM_ID);
    }
    @Test
    public void shouldCloneEmailViaShorthandMethod() throws Exception {
        String newEmailName = "integration-test-clone-" + UUID.randomUUID().toString();
        Email existingEmail = marketoClient.loadEmailById(TEST_EMAIL_ID);

        Email clone = marketoClient.cloneEmail(existingEmail, newEmailName);

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

        marketoClient.updateEmailContent(TEST_EMAIL_ID, Collections.singletonList(newItem));

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
        newItem.getValue().get(1).setValue(null);

        marketoClient.updateEmailContent(TEST_EMAIL_ID, Collections.singletonList(newItem));

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

        marketoClient.updateEmailContent(TEST_EMAIL_ID, Collections.singletonList(newItem));

        // Can not verify - no way to fetch not approved content
    }

    @Test
    public void shouldUpdateEmailSubject() throws Exception {

        Email email = new Email();
        email.setId(TEST_EMAIL_ID);
        email.setSubject("Subject from integration test");
        email.setFromName("From SDK committer");

        marketoClient.updateEmail(email);

        // Can not verify - no way to fetch not approved content
    }

    @Test
    public void shouldGetFolders() throws Exception {
        List<FolderDetails> folders = marketoClient.getFolders(new FolderId(1, FolderType.FOLDER), 0, 1, 1, null);

        assertThat(folders).hasSize(1);
        assertThat(folders.get(0).getId()).isPositive();
        assertThat(folders.get(0).getName()).isNotEmpty();
        assertThat(folders.get(0).getDescription()).isNotEmpty();
        assertThat(folders.get(0).getCreatedAt()).isBefore(new Date());
        assertThat(folders.get(0).getUpdatedAt()).isBefore(new Date());
        assertThat(folders.get(0).getFolderId()).isNotNull();
        assertThat(folders.get(0).getPath()).isNotEmpty();
        assertThat(folders.get(0).getFolderId()).isNotNull();
        assertThat(folders.get(0).getFolderType()).isNotEmpty();
        assertThat(folders.get(0).getParent()).isNull();
        assertThat(folders.get(0).getWorkspace()).isNotEmpty();
    }

}
