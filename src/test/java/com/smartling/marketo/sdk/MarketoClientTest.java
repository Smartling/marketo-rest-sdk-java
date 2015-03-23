package com.smartling.marketo.sdk;

import com.smartling.marketo.sdk.command.CloneEmail;
import com.smartling.marketo.sdk.command.GetEmailsCommand;
import com.smartling.marketo.sdk.command.LoadEmailById;
import com.smartling.marketo.sdk.command.LoadEmailContent;
import com.smartling.marketo.sdk.transport.HttpCommandExecutor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Collections;
import java.util.List;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.isA;

@RunWith(MockitoJUnitRunner.class)
public class MarketoClientTest {
    @Mock
    private HttpCommandExecutor executor;

    @InjectMocks
    private MarketoClient testedInstance;

    @Test
    public void shouldRequestEmailList() throws Exception {
        Email email = new Email();
        given(executor.execute(isA(GetEmailsCommand.class))).willReturn(Collections.singletonList(email));

        List<Email> emails = testedInstance.listEmails(0, 10);

        assertThat(emails).contains(email);
    }

    @Test
    public void shouldLoadEmailById() throws Exception {
        Email email = new Email();
        given(executor.execute(isA(LoadEmailById.class))).willReturn(Collections.singletonList(email));

        Email result = testedInstance.loadEmailById(42);

        assertThat(result).isEqualTo(email);
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
}