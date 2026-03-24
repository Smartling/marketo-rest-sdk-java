package com.smartling.it.marketo.sdk;

import com.smartling.marketo.sdk.AuthenticationErrorException;
import com.smartling.marketo.sdk.MarketoClientManager;
import com.smartling.marketo.sdk.rest.MarketoRestClientManagerFactory;
import org.junit.Test;

public class RestIntegrationTest extends BaseIntegrationTest{

    @Test(expected = AuthenticationErrorException.class)
    public void shouldThrowAuthenticationError() throws Exception {
        MarketoClientManager invalid = new MarketoRestClientManagerFactory.Builder().build().create(identityEndpoint, restEndpoint, "notCachedClientId", "invalid");
        invalid.getMarketoEmailClient().listEmails(0, 1);
    }
}
