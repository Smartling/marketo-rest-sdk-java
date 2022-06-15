package com.smartling.marketo.sdk.rest;

import com.smartling.marketo.sdk.MarketoApiException;
import com.smartling.marketo.sdk.domain.Asset.Status;
import com.smartling.marketo.sdk.domain.email.Email;
import com.smartling.marketo.sdk.domain.email.EmailContentItem;
import com.smartling.marketo.sdk.domain.email.EmailFullContent;
import com.smartling.marketo.sdk.domain.email.EmailSnippetContentItem;
import com.smartling.marketo.sdk.domain.email.EmailTextContentItem;
import com.smartling.marketo.sdk.domain.email.EmailVariable;
import com.smartling.marketo.sdk.domain.folder.FolderId;
import com.smartling.marketo.sdk.domain.folder.FolderType;
import com.smartling.marketo.sdk.rest.command.email.CloneEmail;
import com.smartling.marketo.sdk.rest.command.email.GetEmailFullContent;
import com.smartling.marketo.sdk.rest.command.email.GetEmailVariables;
import com.smartling.marketo.sdk.rest.command.email.GetEmailsByName;
import com.smartling.marketo.sdk.rest.command.email.GetEmailsCommand;
import com.smartling.marketo.sdk.rest.command.email.LoadEmailById;
import com.smartling.marketo.sdk.rest.command.email.LoadEmailContent;
import com.smartling.marketo.sdk.rest.command.email.SendSample;
import com.smartling.marketo.sdk.rest.command.email.UpdateEmailContent;
import com.smartling.marketo.sdk.rest.command.email.UpdateEmailEditableSection;
import com.smartling.marketo.sdk.rest.command.email.UpdateEmailSnippetContent;
import com.smartling.marketo.sdk.rest.command.email.UpdateEmailVariable;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class MarketoEmailRestClientTest {

    @Rule
    public final ExpectedException thrown = ExpectedException.none();

    @Mock
    private HttpCommandExecutor executor;

    @InjectMocks
    private MarketoEmailRestClient testedInstance;

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

        List<Email> emails = testedInstance.listEmails(0, 10, new FolderId(1, FolderType.FOLDER), Status.APPROVED);

        assertThat(emails).contains(email);
    }

    @Test
    public void shouldReturnEmptyEmailListOnNullResult() throws Exception {
        given(executor.execute(any(Command.class))).willReturn(null);

        List<Email> emails = testedInstance.listEmails(0, 10, new FolderId(1, FolderType.FOLDER), Status.APPROVED);

        assertThat(emails).isEmpty();
    }

    @Test
    public void shouldReturnEmptyEmailListByNameOnNullResult() throws Exception {
        given(executor.execute(any(Command.class))).willReturn(null);

        List<Email> emails = testedInstance.getEmailsByName("name", new FolderId(1, FolderType.FOLDER), Status.APPROVED);

        assertThat(emails).isEmpty();
    }

    @Test(expected = MarketoApiException.class)
    public void shouldRethrowApiErrorsDifferentFromPaginationIssues() throws Exception {
        given(executor.execute(any(Command.class))).willThrow(new MarketoApiException("100", ""));

        testedInstance.listEmails(0, 10, new FolderId(1, FolderType.FOLDER), Status.APPROVED);
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

        List<Email> result = testedInstance.getEmailsByName("name", new FolderId(42, FolderType.FOLDER), Status.APPROVED);

        assertThat(result.get(0)).isEqualTo(expected);
    }

    @Test
    public void shouldGetPaginatedEmailsByName() throws Exception {
        Email expected = new Email();
        given(executor.execute(isA(GetEmailsByName.class))).willReturn(Collections.singletonList(expected));

        List<Email> result = testedInstance.getEmailsByName(0, 10, "name", new FolderId(42, FolderType.FOLDER), Status.APPROVED);

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
    public void shouldUpdateEmailContentItem() throws Exception {
        testedInstance.updateEmailContentItem(42, new EmailTextContentItem());

        verify(executor).execute(isA(UpdateEmailEditableSection.class));
    }

    @Test
    public void shouldUpdateEmailSnippetContentItem() throws Exception {
        testedInstance.updateEmailSnippetContentItem(42, new EmailSnippetContentItem());

        verify(executor).execute(isA(UpdateEmailSnippetContent.class));
    }

    @Test
    public void shouldUpdateEmail() throws Exception {
        testedInstance.updateEmail(new Email());

        verify(executor).execute(isA(UpdateEmailContent.class));
    }

    @Test
    public void shouldSendSample() throws Exception {
        testedInstance.sendSample(42, "foo@bar.baz", false);

        verify(executor).execute(isA(SendSample.class));
    }

    @Test
    public void shouldGetEmailVariables() throws Exception {
        EmailVariable variable = new EmailVariable();
        given(executor.execute(isA(GetEmailVariables.class))).willReturn(Collections.singletonList(variable));

        List<EmailVariable> result = testedInstance.getEmailVariables(42);

        assertThat(result).contains(variable).hasSize(1);
    }

    @Test
    public void shouldUpdateEmailVariables() throws Exception {
        EmailVariable variable = new EmailVariable();
        EmailVariable updated = new EmailVariable();
        given(executor.execute(isA(UpdateEmailVariable.class))).willReturn(Collections.singletonList(updated));

        EmailVariable response = testedInstance.updateEmailVariable(42, variable);

        assertThat(response).isEqualTo(updated);
        verify(executor).execute(isA(UpdateEmailVariable.class));
    }

    @Test
    public void shouldGetEmailFullContent() throws Exception {
        EmailFullContent fullContent = new EmailFullContent();
        given(executor.execute(isA(GetEmailFullContent.class))).willReturn(Collections.singletonList(fullContent));

        EmailFullContent result = testedInstance.getEmailFullContent(42, Status.DRAFT);

        assertThat(result).isEqualTo(fullContent);
    }
}
