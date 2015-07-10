package com.smartling.marketo.sdk.rest;

import com.smartling.marketo.sdk.Email;
import com.smartling.marketo.sdk.EmailContentItem;
import com.smartling.marketo.sdk.FolderDetails;
import com.smartling.marketo.sdk.FolderId;
import com.smartling.marketo.sdk.FolderType;
import com.smartling.marketo.sdk.MarketoApiException;
import com.smartling.marketo.sdk.rest.command.*;
import com.smartling.marketo.sdk.rest.transport.HttpCommandExecutor;
import org.junit.Test;
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
    public void shouldReturnEmptyEmailListIfNoEmailCanBeFound() throws Exception {
        given(executor.execute(any(Command.class))).willThrow(new MarketoApiException("702", ""));

        List<Email> emails = testedInstance.listEmails(0, 10, new FolderId(1, FolderType.FOLDER), Email.Status.APPROVED);

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
    public void shouldLoadEmailByName() throws Exception {
        Email expected = new Email();
        given(executor.execute(isA(LoadEmailByName.class))).willReturn(Collections.singletonList(expected));

        Email result = testedInstance.loadEmailByName("name");

        assertThat(result).isEqualTo(expected);
    }

    @Test
    public void shouldLoadEmailContent() throws Exception {
        EmailContentItem contentItem = new EmailContentItem();
        given(executor.execute(isA(LoadEmailContent.class))).willReturn(Collections.singletonList(contentItem));

        List<EmailContentItem> result = testedInstance.loadEmailContent(42);

        assertThat(result).contains(contentItem);
    }

    @Test
    public void shouldCloneEmail() throws Exception {
        Email clone = new Email();
        given(executor.execute(isA(CloneEmail.class))).willReturn(Collections.singletonList(clone));

        Email result = testedInstance.cloneEmail(42, "blah", 999);

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
        testedInstance.updateEmailContent(42, Arrays.asList(new EmailContentItem(), new EmailContentItem()));

        verify(executor, times(2)).execute(isA(UpdateEmailEditableSection.class));
    }

    @Test
    public void shouldReturnFolder() throws Exception {
        FolderDetails folder = new FolderDetails();
        given(executor.execute(isA(GetFoldersCommand.class))).willReturn(Collections.singletonList(folder));

        List<FolderDetails> folders = testedInstance.getFolders(new FolderId(1, FolderType.FOLDER), 0, 1, 10, null);

        assertThat(folders).contains(folder);
    }
}