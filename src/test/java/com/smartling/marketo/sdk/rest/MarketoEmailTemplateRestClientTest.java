package com.smartling.marketo.sdk.rest;

import com.smartling.marketo.sdk.MarketoApiException;
import com.smartling.marketo.sdk.domain.Asset.Status;
import com.smartling.marketo.sdk.domain.emailtemplate.EmailTemplate;
import com.smartling.marketo.sdk.domain.emailtemplate.EmailTemplateContent;
import com.smartling.marketo.sdk.rest.command.emailtemplate.GetEmailTemplateById;
import com.smartling.marketo.sdk.rest.command.emailtemplate.GetEmailTemplateContent;
import com.smartling.marketo.sdk.rest.command.emailtemplate.GetEmailTemplates;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.isA;

@RunWith(MockitoJUnitRunner.class)
public class MarketoEmailTemplateRestClientTest {

    @Rule
    public final ExpectedException thrown = ExpectedException.none();

    @Mock
    private HttpCommandExecutor executor;

    @InjectMocks
    private MarketoEmailTemplateRestClient testedInstance;

    @Test
    public void shouldGetemailtemplates() throws Exception {
        EmailTemplate emailTemplate = new EmailTemplate();

        given(executor.execute(isA(GetEmailTemplates.class))).willReturn(Collections.singletonList(emailTemplate));

        List<EmailTemplate> result = testedInstance.getEmailTemplates(0, 200, null, null);

        assertThat(result).hasSize(1);
        assertThat(result).contains(emailTemplate);
    }

    @Test
    public void shouldGetEmailById() throws Exception {
        EmailTemplate emailTemplate = new EmailTemplate();
        given(executor.execute(isA(GetEmailTemplateById.class))).willReturn(Collections.singletonList(emailTemplate));

        EmailTemplate result = testedInstance.getEmailTemplateById(42);

        assertThat(result).isEqualTo(emailTemplate);
    }

    @Test
    public void shouldGetEmailByIdAndStatus() throws Exception {
        EmailTemplate emailTemplate = new EmailTemplate();
        given(executor.execute(isA(GetEmailTemplateById.class))).willReturn(Collections.singletonList(emailTemplate));

        EmailTemplate result = testedInstance.getEmailTemplateById(42, Status.DRAFT);

        assertThat(result).isEqualTo(emailTemplate);
    }

    @Test
    public void shouldThrowMarketoApiExceptionIfEmailNotFound() throws Exception
    {
        int nonExistingId = 42;

        given(executor.execute(isA(GetEmailTemplateById.class))).willReturn(null);

        thrown.expect(MarketoApiException.class);
        thrown.expectMessage("EmailTemplate[id = 42] not found");

        testedInstance.getEmailTemplateById(nonExistingId);
    }

    @Test
    public void shouldGetEmailContent() throws Exception {
        EmailTemplateContent contentItem = new EmailTemplateContent();
        given(executor.execute(isA(GetEmailTemplateContent.class))).willReturn(Collections.singletonList(contentItem));

        List<EmailTemplateContent> result = testedInstance.getEmailTemplateContent(42);

        assertThat(result).contains(contentItem);
    }
}