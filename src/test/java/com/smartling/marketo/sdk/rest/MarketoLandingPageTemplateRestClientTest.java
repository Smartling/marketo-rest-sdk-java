package com.smartling.marketo.sdk.rest;

import com.smartling.marketo.sdk.MarketoApiException;
import com.smartling.marketo.sdk.domain.Asset.Status;
import com.smartling.marketo.sdk.domain.landingpagetemplate.LandingPageTemplate;
import com.smartling.marketo.sdk.domain.landingpagetemplate.LandingPageTemplateContent;
import com.smartling.marketo.sdk.rest.command.landingpagetemplate.GetLandingPageTemplateById;
import com.smartling.marketo.sdk.rest.command.landingpagetemplate.GetLandingPageTemplateContent;
import com.smartling.marketo.sdk.rest.command.landingpagetemplate.GetLandingPageTemplates;
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
public class MarketoLandingPageTemplateRestClientTest {

    @Rule
    public final ExpectedException thrown = ExpectedException.none();

    @Mock
    private HttpCommandExecutor executor;

    @InjectMocks
    private MarketoLandingPageTemplateRestClient testedInstance;

    @Test
    public void shouldGetlandingPageTemplates() throws Exception {
        LandingPageTemplate landingPageTemplate = new LandingPageTemplate();

        given(executor.execute(isA(GetLandingPageTemplates.class))).willReturn(Collections.singletonList(landingPageTemplate));

        List<LandingPageTemplate> result = testedInstance.getLandingPageTemplates(0, 200, null, null);

        assertThat(result).hasSize(1);
        assertThat(result).contains(landingPageTemplate);
    }

    @Test
    public void shouldGetLandingPageById() throws Exception {
        LandingPageTemplate landingPageTemplate = new LandingPageTemplate();
        given(executor.execute(isA(GetLandingPageTemplateById.class))).willReturn(Collections.singletonList(landingPageTemplate));

        LandingPageTemplate result = testedInstance.getLandingPageTemplateById(42);

        assertThat(result).isEqualTo(landingPageTemplate);
    }

    @Test
    public void shouldGetLandingPageByIdAndStatus() throws Exception {
        LandingPageTemplate landingPageTemplate = new LandingPageTemplate();
        given(executor.execute(isA(GetLandingPageTemplateById.class))).willReturn(Collections.singletonList(landingPageTemplate));

        LandingPageTemplate result = testedInstance.getLandingPageTemplateById(42, Status.DRAFT);

        assertThat(result).isEqualTo(landingPageTemplate);
    }

    @Test
    public void shouldThrowMarketoApiExceptionIfLandingPageNotFound() throws Exception
    {
        int nonExistingId = 42;

        given(executor.execute(isA(GetLandingPageTemplateById.class))).willReturn(null);

        thrown.expect(MarketoApiException.class);
        thrown.expectMessage("LandingPageTemplate[id = 42] not found");

        testedInstance.getLandingPageTemplateById(nonExistingId);
    }

    @Test
    public void shouldGetLandingPageContent() throws Exception {
        LandingPageTemplateContent contentItem = new LandingPageTemplateContent();
        given(executor.execute(isA(GetLandingPageTemplateContent.class))).willReturn(Collections.singletonList(contentItem));

        List<LandingPageTemplateContent> result = testedInstance.getLandingPageTemplateContent(42);

        assertThat(result).contains(contentItem);
    }
}