package com.smartling.marketo.sdk.rest;

import com.smartling.marketo.sdk.MarketoEmailClient;
import com.smartling.marketo.sdk.MarketoEmailTemplateClient;
import com.smartling.marketo.sdk.MarketoFolderClient;
import com.smartling.marketo.sdk.MarketoLandingPageClient;
import com.smartling.marketo.sdk.MarketoLandingPageTemplateClient;
import com.smartling.marketo.sdk.MarketoProgramClient;
import com.smartling.marketo.sdk.MarketoSnippetClient;
import com.smartling.marketo.sdk.MarketoTokenClient;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class MarketoRestClientManagerTest {

    private MarketoRestClientManager testedInstance;

    @Before
    public void setUp() {
        testedInstance = MarketoRestClientManager.create("IdentityURL", "RestURL").withCredentials("ClientId", "ClientSecret");
    }

    @Test(expected = NullPointerException.class)
    public void shouldValidateRetryPolicy() throws Exception {
        MarketoRestClientManager.create("IdentityURL", "RestURL")
                .withRetryPolicy(null);
    }

    @Test
    public void shouldReturnFolderRestClient() throws Exception {
        final MarketoFolderClient client = testedInstance.getMarketoFolderClient();

        assertThat(client).isNotNull();
        assertThat(client).isInstanceOf(MarketoFolderRestClient.class);
    }

    @Test
    public void shouldReturnEmailRestClient() throws Exception {
        final MarketoEmailClient client = testedInstance.getMarketoEmailClient();

        assertThat(client).isNotNull();
        assertThat(client).isInstanceOf(MarketoEmailRestClient.class);
    }

    @Test
    public void shouldReturnEmailTemplateRestClient() throws Exception {
        final MarketoEmailTemplateClient client = testedInstance.getMarketoEmailTemplateClient();

        assertThat(client).isNotNull();
        assertThat(client).isInstanceOf(MarketoEmailTemplateRestClient.class);
    }

    @Test
    public void shouldReturnSnippetRestClient() throws Exception {
        final MarketoSnippetClient client = testedInstance.getMarketoSnippetClient();

        assertThat(client).isNotNull();
        assertThat(client).isInstanceOf(MarketoSnippetRestClient.class);
    }

    @Test
    public void shouldReturnLandingPageRestClient() throws Exception {
        final MarketoLandingPageClient client = testedInstance.getMarketoLandingPageClient();

        assertThat(client).isNotNull();
        assertThat(client).isInstanceOf(MarketoLandingPageRestClient.class);
    }

    @Test
    public void shouldReturnLandingPageTemplateRestClient() throws Exception {
        final MarketoLandingPageTemplateClient client = testedInstance.getMarketoLandingPageTemplateClient();

        assertThat(client).isNotNull();
        assertThat(client).isInstanceOf(MarketoLandingPageTemplateRestClient.class);
    }

    @Test
    public void shouldReturnFormRestClient() throws Exception {
        final MarketoLandingPageClient client = testedInstance.getMarketoLandingPageClient();

        assertThat(client).isNotNull();
        assertThat(client).isInstanceOf(MarketoLandingPageRestClient.class);
    }

    @Test
    public void shouldReturnProgramRestClient() throws Exception {
        final MarketoProgramClient client = testedInstance.getMarketoProgramClient();

        assertThat(client).isNotNull();
        assertThat(client).isInstanceOf(MarketoProgramRestClient.class);
    }

    @Test
    public void shouldReturnTokenRestClient() throws Exception {
        final MarketoTokenClient client = testedInstance.getMarketoTokenClient();

        assertThat(client).isNotNull();
        assertThat(client).isInstanceOf(MarketoTokenRestClient.class);
    }
}
