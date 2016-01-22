package com.smartling.marketo.sdk.rest;

import com.smartling.marketo.sdk.Asset;
import com.smartling.marketo.sdk.EmailTextContentItem;
import com.smartling.marketo.sdk.SnippetContentItem;
import com.smartling.marketo.sdk.Email;
import com.smartling.marketo.sdk.EmailContentItem;
import com.smartling.marketo.sdk.FolderDetails;
import com.smartling.marketo.sdk.FolderId;
import com.smartling.marketo.sdk.FolderType;
import com.smartling.marketo.sdk.MarketoApiException;
import com.smartling.marketo.sdk.Snippet;
import com.smartling.marketo.sdk.rest.command.*;
import com.smartling.marketo.sdk.rest.transport.HttpCommandExecutor;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class MarketoRestClientTest {

    @Rule
    public final ExpectedException thrown = ExpectedException.none();

    @Mock
    private HttpCommandExecutor executor;

    @InjectMocks
    private MarketoRestClient testedInstance;

    @Test
    public void shouldRequestEmailList() throws Exception {
        Email email = new Email();
        given(executor.execute(isA(GetEmailsCommand.class))).willReturn(Collections.singletonList(email));

        List<Email> emails = testedInstance.listEmails(0, 10);

        assertThat(emails).contains(email);
    }

    @Test
    public void shouldRequestEmailListWithFilter() throws Exception {
        Email email = new Email();
        given(executor.execute(isA(GetEmailsCommand.class))).willReturn(Collections.singletonList(email));

        List<Email> emails = testedInstance.listEmails(0, 10, new FolderId(1, FolderType.FOLDER), Email.Status.APPROVED);

        assertThat(emails).contains(email);
    }

    @Test
    public void shouldReturnEmptyEmailListOnNullResult() throws Exception {
        given(executor.execute(any(Command.class))).willReturn(null);

        List<Email> emails = testedInstance.listEmails(0, 10, new FolderId(1, FolderType.FOLDER), Email.Status.APPROVED);

        assertThat(emails).isEmpty();
    }

    @Test
    public void shouldReturnEmptyEmailListByNameOnNullResult() throws Exception {
        given(executor.execute(any(Command.class))).willReturn(null);

        List<Email> emails = testedInstance.getEmailsByName("name", new FolderId(1, FolderType.FOLDER), Email.Status.APPROVED);

        assertThat(emails).isEmpty();
    }

    @Test(expected = MarketoApiException.class)
    public void shouldRethrowApiErrorsDifferentFromPaginationIssues() throws Exception {
        given(executor.execute(any(Command.class))).willThrow(new MarketoApiException("100", ""));

        testedInstance.listEmails(0, 10, new FolderId(1, FolderType.FOLDER), Email.Status.APPROVED);
    }

    @Test
    public void shouldLoadEmailById() throws Exception {
        Email email = new Email();
        given(executor.execute(isA(LoadEmailById.class))).willReturn(Collections.singletonList(email));

        Email result = testedInstance.loadEmailById(42);

        assertThat(result).isEqualTo(email);
    }

    @Test
    public void shouldThrowMarketoApiExceptionIfEmailNotFound() throws Exception
    {
        int nonExistingId = 42;

        given(executor.execute(isA(LoadEmailById.class))).willReturn(null);

        thrown.expect(MarketoApiException.class);
        thrown.expectMessage("Email[id = 42] not found");

        testedInstance.loadEmailById(nonExistingId);
    }

    @Test
    public void shouldGetEmailsByName() throws Exception {
        Email expected = new Email();
        given(executor.execute(isA(GetEmailsByName.class))).willReturn(Collections.singletonList(expected));

        List<Email> result = testedInstance.getEmailsByName("name", new FolderId(42, FolderType.FOLDER), Email.Status.APPROVED);

        assertThat(result.get(0)).isEqualTo(expected);
    }

    @Test
    public void shouldLoadEmailContent() throws Exception {
        EmailContentItem contentItem = new EmailTextContentItem();
        given(executor.execute(isA(LoadEmailContent.class))).willReturn(Collections.singletonList(contentItem));

        List<EmailContentItem> result = testedInstance.loadEmailContent(42);

        assertThat(result).contains(contentItem);
    }

    @Test
    public void shouldCloneEmail() throws Exception {
        Email clone = new Email();
        given(executor.execute(isA(CloneEmail.class))).willReturn(Collections.singletonList(clone));

        Email result = testedInstance.cloneEmail(42, "blah", new FolderId(999, FolderType.FOLDER));

        assertThat(result).isSameAs(clone);
    }

    @Test
    public void shouldCloneExistingEmail() throws Exception {
        Email clone = new Email();
        given(executor.execute(isA(CloneEmail.class))).willReturn(Collections.singletonList(clone));

        Email result = testedInstance.cloneEmail(new Email(), "blah");

        assertThat(result).isSameAs(clone);
    }

    @Test
    public void shouldUpdateEmailContent() throws Exception {
        testedInstance.updateEmailContent(42, Arrays.asList(new EmailTextContentItem(), new EmailTextContentItem()));

        verify(executor, times(2)).execute(isA(UpdateEmailEditableSection.class));
    }

    @Test
    public void shouldReturnFolder() throws Exception {
        FolderDetails folder = new FolderDetails();
        given(executor.execute(isA(GetFoldersCommand.class))).willReturn(Collections.singletonList(folder));

        List<FolderDetails> folders = testedInstance.getFolders(new FolderId(1, FolderType.FOLDER), 0, 1, 10, null);

        assertThat(folders).contains(folder);
    }

    @Test
    public void shouldRequestSnippetListWithFilter() throws Exception {
        Snippet snippet= new Snippet();
        given(executor.execute(isA(GetSnippets.class))).willReturn(Collections.singletonList(snippet));

        List<Snippet> snippets = testedInstance.listSnippets(0, 10, Asset.Status.APPROVED);

        assertThat(snippets).contains(snippet);
    }

    @Test
    public void shouldLoadSnippetById() throws Exception {
        Snippet snippet = new Snippet();
        given(executor.execute(isA(LoadSnippetById.class))).willReturn(Collections.singletonList(snippet));

        Snippet result = testedInstance.loadSnippetById(42);

        assertThat(result).isEqualTo(snippet);
    }

    @Test
    public void shouldLoadSnippetContent() throws Exception {
        SnippetContentItem contentItem = new SnippetContentItem();
        given(executor.execute(isA(LoadSnippetContent.class))).willReturn(Collections.singletonList(contentItem));

        List<SnippetContentItem> result = testedInstance.loadSnippetContent(42);

        assertThat(result).contains(contentItem);
    }

    @Test
    public void shouldCloneSnippet() throws Exception {
        Snippet clone = new Snippet();
        given(executor.execute(isA(CloneSnippet.class))).willReturn(Collections.singletonList(clone));

        Snippet result = testedInstance.cloneSnippet(42, "blah", new FolderId(999, FolderType.FOLDER));

        assertThat(result).isSameAs(clone);
    }

    @Test
    public void shouldUpdateSnippetContent() throws Exception {
        testedInstance.updateSnippetContent(42, new SnippetContentItem());

        verify(executor).execute(isA(UpdateSnippetContent.class));
    }

    @Test
    public void shouldReturnEmptySnippetListOnNullResult() throws Exception {
        given(executor.execute(any(Command.class))).willReturn(null);

        List<Snippet> snippets = testedInstance.listSnippets(0, 10, Email.Status.APPROVED);

        assertThat(snippets).isEmpty();
    }
}