package com.smartling.it.marketo.sdk;

import com.smartling.marketo.sdk.Email;
import com.smartling.marketo.sdk.EmailContentItem;
import com.smartling.marketo.sdk.FolderDetails;
import com.smartling.marketo.sdk.FolderId;
import com.smartling.marketo.sdk.FolderType;
import com.smartling.marketo.sdk.MarketoApiException;
import com.smartling.marketo.sdk.MarketoClient;
import com.smartling.marketo.sdk.rest.MarketoRestClient;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.ws.rs.ProcessingException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.fest.assertions.api.Assertions.assertThat;

public class EmailIntegrationTest {
    private static final int TEST_EMAIL_ID = 1109;
    private static final String TEST_EMAIL_NAME = "Email For Integration Tests";
    private static final int TEST_FOLDER_ID = 44;

    private static String identityEndpoint;
    private static String restEndpoint;
    private static String clientId;
    private static String clientSecret;
    private static String nonRoutableHostUrl;

    private MarketoClient marketoClient;

    @BeforeClass
    public static void checkPreconditions() {
        identityEndpoint = System.getProperty("marketo.identity");
        restEndpoint = System.getProperty("marketo.rest");
        clientId = System.getProperty("marketo.clientId");
        clientSecret = System.getProperty("marketo.clientSecret");

        nonRoutableHostUrl = "http://192.0.2.0:81";

        assertThat(identityEndpoint).overridingErrorMessage("Identity endpoint is missing").isNotEmpty();
        assertThat(restEndpoint).overridingErrorMessage("REST endpoint is missing").isNotEmpty();
        assertThat(clientId).overridingErrorMessage("Client ID is missing").isNotEmpty();
        assertThat(clientSecret).overridingErrorMessage("Client Secret is missing").isNotEmpty();
    }

    @Before
    public void setUp() throws Exception {
        marketoClient = MarketoRestClient.create(identityEndpoint, restEndpoint).withCredentials(clientId, clientSecret);
    }

    @Test
    public void shouldListEmails() throws Exception {
        List<Email> emails = marketoClient.listEmails(0, 1);

        assertThat(emails).hasSize(1);
        assertThat(emails.get(0).getId()).isPositive();
        assertThat(emails.get(0).getName()).isNotEmpty();
        assertThat(emails.get(0).getUpdatedAt()).isNotNull();
        assertThat(emails.get(0).getStatus()).isNotNull();
    }

    @Test
    public void shouldListEmailsWithFilter() throws Exception {
        List<Email> emails = marketoClient.listEmails(0, 1, new FolderId(TEST_FOLDER_ID, FolderType.FOLDER), Email.Status.APPROVED);

        assertThat(emails).hasSize(1);
        assertThat(emails.get(0).getId()).isPositive();
        assertThat(emails.get(0).getName()).isNotEmpty();
        assertThat(emails.get(0).getUpdatedAt()).isNotNull();
        assertThat(emails.get(0).getStatus()).isNotNull();
    }

    @Test
    public void shouldReturnEmptyListWhenEndReached() throws Exception {
        List<Email> emails = marketoClient.listEmails(10000, 1);

        assertThat(emails).isEmpty();
    }

    @Test(expected = MarketoApiException.class)
    public void shouldThrowAuthenticationError() throws Exception {
        MarketoClient invalid = MarketoRestClient.create(identityEndpoint, restEndpoint).withCredentials(clientId, "invalid");
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
    }

    @Test
    public void shouldLoadEmailByName() throws Exception {
        Email email = marketoClient.loadEmailByName(TEST_EMAIL_NAME);

        assertThat(email).isNotNull();
        assertThat(email.getId()).isEqualTo(TEST_EMAIL_ID);
    }

    @Test
    public void shouldReadEmailContent() throws Exception {
        List<EmailContentItem> contentItems = marketoClient.loadEmailContent(TEST_EMAIL_ID);

        assertThat(contentItems).hasSize(2);
        assertThat(contentItems.get(0).getHtmlId()).isEqualTo("greeting");
        assertThat(contentItems.get(0).getValue()).hasSize(2);
        assertThat(contentItems.get(0).getValue().get(0).getType()).isEqualTo("HTML");
        assertThat(contentItems.get(0).getValue().get(0).getValue()).isNotEmpty();
        assertThat(contentItems.get(0).getValue().get(1).getType()).isEqualTo("Text");
        assertThat(contentItems.get(0).getValue().get(1).getValue()).isNotEmpty();
    }

    @Test
    public void shouldCloneEmail() throws Exception {
        String newEmailName = "integration-test-clone-" + UUID.randomUUID().toString();

        Email clone = marketoClient.cloneEmail(TEST_EMAIL_ID, newEmailName, TEST_FOLDER_ID);

        assertThat(clone).isNotNull();
        assertThat(clone.getId()).isPositive();
        assertThat(clone.getName()).isEqualTo(newEmailName);
        assertThat(clone.getFolder().getValue()).isEqualTo(TEST_FOLDER_ID);
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
        EmailContentItem newItem = new EmailContentItem();
        newItem.setHtmlId("greeting");
        newItem.setContentType("Text");
        newItem.setValue(Arrays.asList(new EmailContentItem.Value(), new EmailContentItem.Value()));
        newItem.getValue().get(0).setType("HTML");
        newItem.getValue().get(0).setValue("<strong>" + UUID.randomUUID() + "<strong>");
        newItem.getValue().get(1).setType("Text");
        newItem.getValue().get(1).setValue(UUID.randomUUID().toString());

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

    @Test(timeout = 3 * 1000, expected = ProcessingException.class)
    public void shouldSupportTimeoutConfiguration() throws Exception {
        MarketoRestClient clientWithTimeout = MarketoRestClient.create(nonRoutableHostUrl, restEndpoint).withConnectionTimeout(1000)
                .withCredentials(clientId, clientSecret);

        clientWithTimeout.listEmails(0, 1);
    }

    @Test
    public void shouldGetFolders() throws Exception {
        List<FolderDetails> folders = marketoClient.getFolders(new FolderId(1, FolderType.FOLDER), 0, 1, 1, null);

        assertThat(folders).hasSize(1);
        assertThat(folders.get(0).getId()).isPositive();
        assertThat(folders.get(0).getName()).isNotEmpty();
        assertThat(folders.get(0).getUpdatedAt()).isNotNull();
        assertThat(folders.get(0).getFolderId()).isNotNull();
        assertThat(folders.get(0).getPath()).isNotNull();
    }
}
