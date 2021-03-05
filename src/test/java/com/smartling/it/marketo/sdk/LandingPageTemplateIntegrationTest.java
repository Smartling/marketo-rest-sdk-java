package com.smartling.it.marketo.sdk;

import com.smartling.marketo.sdk.MarketoApiException;
import com.smartling.marketo.sdk.MarketoLandingPageTemplateClient;
import com.smartling.marketo.sdk.domain.Asset.Status;
import com.smartling.marketo.sdk.domain.folder.FolderId;
import com.smartling.marketo.sdk.domain.folder.FolderType;
import com.smartling.marketo.sdk.domain.landingpagetemplate.LandingPageTemplate;
import com.smartling.marketo.sdk.domain.landingpagetemplate.LandingPageTemplateContent;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static com.smartling.marketo.sdk.domain.landingpagetemplate.TemplateType.FREEFORM;
import static com.smartling.marketo.sdk.domain.landingpagetemplate.TemplateType.GUIDED;

public class LandingPageTemplateIntegrationTest extends BaseIntegrationTest {
    private static final int TEST_LANDING_PAGE_TEMPLATE_ID = 1010;
    private static final int TEST_LANDING_PAGE_TEMPLATE_WITH_DRAFT_ID = 1012;

    @Rule
    public final ExpectedException thrown = ExpectedException.none();
    private MarketoLandingPageTemplateClient marketoLandingPageTemplateClient;

    @Before
    public void setUp() {
        marketoLandingPageTemplateClient = marketoClientManager.getMarketoLandingPageTemplateClient();
    }

    @Test
    public void shouldGetAllLandingPageTemplates() throws Exception {
        List<LandingPageTemplate> landingPageTemplates = marketoLandingPageTemplateClient.getLandingPageTemplates(0, 200, null, null);

        assertThat(landingPageTemplates).isNotNull();
        assertThat(landingPageTemplates).isNotEmpty();
        assertThat(landingPageTemplates).haveAtLeast(1, new EntityWithName("Blank Template"));
    }

    @Test
    public void shouldGetAllLandingPagesTemplateFromFolder() throws Exception {
        List<LandingPageTemplate> landingPageTemplates = marketoLandingPageTemplateClient.getLandingPageTemplates(0, 200, new FolderId(12, FolderType.FOLDER), null);

        assertThat(landingPageTemplates).isNotNull();
        assertThat(landingPageTemplates).isNotEmpty();
        assertThat(landingPageTemplates).haveAtLeast(1, new EntityWithName("Blank Template"));
    }

    @Test
    public void shouldGetLandingPageTemplateById() throws Exception {
        LandingPageTemplate landingPageTemplate = marketoLandingPageTemplateClient.getLandingPageTemplateById(TEST_LANDING_PAGE_TEMPLATE_ID);

        assertThat(landingPageTemplate).isNotNull();
        assertThat(landingPageTemplate.getId()).isEqualTo(TEST_LANDING_PAGE_TEMPLATE_ID);
        assertThat(landingPageTemplate.getName()).isEqualTo("Guided template for integration tests");
        assertThat(landingPageTemplate.getUrl()).isNotEmpty();
        assertThat(landingPageTemplate.getFolder()).isNotNull();
        assertThat(landingPageTemplate.getTemplateType()).isEqualTo(GUIDED);
        assertThat(landingPageTemplate.getStatus()).isEqualTo(Status.APPROVED);
    }

    @Test
    public void shouldGetLandingPageTemplateByIdAndStatus() throws Exception {
        LandingPageTemplate landingPageTemplate = marketoLandingPageTemplateClient
                .getLandingPageTemplateById(TEST_LANDING_PAGE_TEMPLATE_WITH_DRAFT_ID, Status.DRAFT);

        assertThat(landingPageTemplate).isNotNull();
        assertThat(landingPageTemplate.getId()).isEqualTo(TEST_LANDING_PAGE_TEMPLATE_WITH_DRAFT_ID);
        assertThat(landingPageTemplate.getStatus()).isEqualTo(Status.DRAFT);
        assertThat(landingPageTemplate.getTemplateType()).isEqualTo(FREEFORM);
    }

    @Test
    public void shouldThrowMarketoApiExceptionWhenCouldNotFindLandingPageTemplateById() throws Exception {

        thrown.expect(MarketoApiException.class);
        thrown.expectMessage("LandingPageTemplate[id = 42] not found");

        marketoLandingPageTemplateClient.getLandingPageTemplateById(42);
    }

    @Test
    public void shouldReadLandingPageTemplateContent() throws Exception {
        List<LandingPageTemplateContent> contentItems = marketoLandingPageTemplateClient.getLandingPageTemplateContent(TEST_LANDING_PAGE_TEMPLATE_ID);

        assertThat(contentItems).hasSize(1);
        LandingPageTemplateContent textContentItem = contentItems.get(0);
        assertThat(textContentItem.getId()).isEqualTo(TEST_LANDING_PAGE_TEMPLATE_ID);
        assertThat(textContentItem.getContent()).containsIgnoringCase("Alice's Adventures in Wonderland");
        assertThat(textContentItem.getTemplateType()).isEqualTo(GUIDED);
    }
}
