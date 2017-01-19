package com.smartling.it.marketo.sdk;

import com.smartling.marketo.sdk.MarketoApiException;
import com.smartling.marketo.sdk.MarketoLandingPageTemplateClient;
import com.smartling.marketo.sdk.domain.Asset.Status;
import com.smartling.marketo.sdk.domain.landingpagetemplate.LandingPageTemplate;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static com.smartling.marketo.sdk.domain.landingpagetemplate.LandingPageTemplate.TemplateType.*;
import static org.fest.assertions.api.Assertions.assertThat;

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
    public void shouldGetLandingPageById() throws Exception {
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
    public void shouldGetLandingPageByIdAndStatus() throws Exception {
        LandingPageTemplate landingPageTemplate = marketoLandingPageTemplateClient
                .getLandingPageTemplateById(TEST_LANDING_PAGE_TEMPLATE_WITH_DRAFT_ID, Status.DRAFT);

        assertThat(landingPageTemplate).isNotNull();
        assertThat(landingPageTemplate.getId()).isEqualTo(TEST_LANDING_PAGE_TEMPLATE_WITH_DRAFT_ID);
        assertThat(landingPageTemplate.getStatus()).isEqualTo(Status.DRAFT);
        assertThat(landingPageTemplate.getTemplateType()).isEqualTo(FREEFORM);
    }

    @Test
    public void shouldThrowMarketoApiExceptionWhenCouldNotFindLandingPageById() throws Exception {

        thrown.expect(MarketoApiException.class);
        thrown.expectMessage("LandingPageTemplate[id = 42] not found");

        marketoLandingPageTemplateClient.getLandingPageTemplateById(42);
    }
}
