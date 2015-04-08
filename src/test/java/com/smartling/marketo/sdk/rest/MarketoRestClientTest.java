package com.smartling.marketo.sdk.rest;

import com.smartling.marketo.sdk.Email;
import com.smartling.marketo.sdk.EmailContentItem;
import com.smartling.marketo.sdk.MarketoApiException;
import com.smartling.marketo.sdk.rest.command.CloneEmail;
import com.smartling.marketo.sdk.rest.command.GetEmailsCommand;
import com.smartling.marketo.sdk.rest.command.LoadEmailById;
import com.smartling.marketo.sdk.rest.command.LoadEmailByName;
import com.smartling.marketo.sdk.rest.command.LoadEmailContent;
import com.smartling.marketo.sdk.rest.command.UpdateEmailContent;
import com.smartling.marketo.sdk.rest.transport.HttpCommandExecutor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.fest.assertions.api.Assertions.entry;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.isA;
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
    public void shouldReturnEmptyEmailListIfNoEmailCanBeFound() throws Exception {
        given(executor.execute(any(Command.class))).willThrow(new MarketoApiException("702", ""));

        List<Email> emails = testedInstance.listEmails(0, 10);

        assertThat(emails).isEmpty();
    }

    @Test(expected = MarketoApiException.class)
    public void shouldRethrowApiErrorsDifferentFromPaginationIssues() throws Exception {
        given(executor.execute(any(Command.class))).willThrow(new MarketoApiException("100", ""));

        testedInstance.listEmails(0, 10);
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
        testedInstance.updateEmailContent(42, "blah", Collections.<EmailContentItem>emptyList());

        verify(executor).execute(isA(UpdateEmailContent.class));
    }

    @Test
    public void shouldMergeEmailContentItems() throws Exception {
        ArgumentCaptor<UpdateEmailContent> captor = ArgumentCaptor.forClass(UpdateEmailContent.class);
        EmailContentItem first = createContentItem("first", "<h1>Title</h1>", "TITLE");
        EmailContentItem second = createContentItem("second", "<p>text</p>", "text");

        testedInstance.updateEmailContent(42, "blah", Arrays.asList(first, second));

        verify(executor).execute(captor.capture());
        assertThat(captor.getValue().getParameters()).contains(entry("htmlContent", ""
                        + "<div class='mktEditable' id='first'><h1>Title</h1></div>"
                        + "<div class='mktEditable' id='second'><p>text</p></div>"));
        assertThat(captor.getValue().getParameters()).contains(entry("textContent", ""
                + "<div class='mktEditable' id='first'>TITLE</div>"
                + "<div class='mktEditable' id='second'>text</div>"));
    }

    private static EmailContentItem createContentItem(String id, String htmlContent, String textContent) {
        EmailContentItem emailContentItem = new EmailContentItem();
        emailContentItem.setHtmlId(id);
        emailContentItem.setHtmlContent(htmlContent);
        emailContentItem.setTextContent(textContent);

        return emailContentItem;
    }
}