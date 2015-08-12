package com.smartling.it.marketo.sdk;

import org.junit.BeforeClass;

import static org.fest.assertions.api.Assertions.assertThat;

public abstract class BaseIntegrationTest {

    protected static String identityEndpoint;
    protected static String restEndpoint;
    protected static String clientId;
    protected static String clientSecret;

    protected static String nonRoutableHostUrl;

    @BeforeClass
    public static void checkPreconditions() {
        identityEndpoint = System.getProperty("marketo.identity");
        restEndpoint = System.getProperty("marketo.rest");
        clientId = System.getProperty("marketo.clientId");
        clientSecret = System.getProperty("marketo.clientSecret");

        nonRoutableHostUrl = "http://192.0.2.0:81";

        assertThat(identityEndpoint).overridingErrorMessage("Identity endpoint is missing").isNotEmpty();
        assertThat(restEndpoint).overridingErrorMessage("REST endpoint is missing").isNotEmpty();
        assertThat(clientId).overridingErrorMessage("Client ID is missing").isNotEmpty();
        assertThat(clientSecret).overridingErrorMessage("Client Secret is missing").isNotEmpty();
    }
}
