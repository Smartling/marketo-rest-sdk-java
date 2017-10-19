package com.smartling.it.marketo.sdk;

import com.smartling.marketo.sdk.MarketoApiException;
import com.smartling.marketo.sdk.MarketoEmailTemplateClient;
import com.smartling.marketo.sdk.domain.Asset.Status;
import com.smartling.marketo.sdk.domain.emailtemplate.EmailTemplate;
import com.smartling.marketo.sdk.domain.emailtemplate.EmailTemplateContent;
import com.smartling.marketo.sdk.domain.folder.FolderId;
import com.smartling.marketo.sdk.domain.folder.FolderType;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.List;

import static org.fest.assertions.api.Assertions.assertThat;

public class EmailTemplateIntegrationTest extends BaseIntegrationTest {
    private static final int TEST_EMAIL_TEMPLATE_ID = 1014;

    @Rule
    public final ExpectedException thrown = ExpectedException.none();
    private MarketoEmailTemplateClient marketoEmailTemplateClient;

    @Before
    public void setUp() {
        marketoEmailTemplateClient = marketoClientManager.getMarketoEmailTemplateClient();
    }

    @Test
    public void shouldGetAllEmailTemplates() throws Exception {
        List<EmailTemplate> emailTemplates = marketoEmailTemplateClient.getEmailTemplates(0, 200, null, null);

        assertThat(emailTemplates).isNotNull();
        assertThat(emailTemplates).isNotEmpty();
        assertThat(emailTemplates).haveAtLeast(1, new EntityWithName("Blank"));
    }

    @Test
    public void shouldGetAllEmailTemplatesFromFolder() throws Exception {
        List<EmailTemplate> emailTemplates = marketoEmailTemplateClient.getEmailTemplates(0, 200, new FolderId(15, FolderType.FOLDER), null);

        assertThat(emailTemplates).isNotNull();
        assertThat(emailTemplates).isNotEmpty();
        assertThat(emailTemplates).haveAtLeast(1, new EntityWithName("Blank"));
    }

    @Test
    public void shouldGetEmailTemplateById() throws Exception {
        EmailTemplate emailTemplate = marketoEmailTemplateClient.getEmailTemplateById(TEST_EMAIL_TEMPLATE_ID);

        assertThat(emailTemplate).isNotNull();
        assertThat(emailTemplate.getId()).isEqualTo(TEST_EMAIL_TEMPLATE_ID);
        assertThat(emailTemplate.getName()).isEqualTo("Template with snippet For Integration Tests");
        assertThat(emailTemplate.getUrl()).isNotEmpty();
        assertThat(emailTemplate.getFolder()).isNotNull();
        assertThat(emailTemplate.getStatus()).isEqualTo(Status.APPROVED);
    }

    @Test
    public void shouldGetEmailTemplateByIdAndStatus() throws Exception {
        EmailTemplate emailTemplate = marketoEmailTemplateClient
                .getEmailTemplateById(TEST_EMAIL_TEMPLATE_ID, Status.DRAFT);

        assertThat(emailTemplate).isNotNull();
        assertThat(emailTemplate.getId()).isEqualTo(TEST_EMAIL_TEMPLATE_ID);
        assertThat(emailTemplate.getStatus()).isEqualTo(Status.DRAFT);
    }

    @Test
    public void shouldThrowMarketoApiExceptionWhenCouldNotFindEmailTemplateById() throws Exception {

        thrown.expect(MarketoApiException.class);
        thrown.expectMessage("EmailTemplate[id = 42] not found");

        marketoEmailTemplateClient.getEmailTemplateById(42);
    }

    @Test
    public void shouldReadEmailTemplateContent() throws Exception {
        List<EmailTemplateContent> contentItems = marketoEmailTemplateClient.getEmailTemplateContent(TEST_EMAIL_TEMPLATE_ID);

        assertThat(contentItems).hasSize(1);
        EmailTemplateContent textContentItem = contentItems.get(0);
        assertThat(textContentItem.getId()).isEqualTo(TEST_EMAIL_TEMPLATE_ID);
        assertThat(textContentItem.getContent()).containsIgnoringCase("Your Email Title");
    }
}
