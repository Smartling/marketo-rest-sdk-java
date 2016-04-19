package com.smartling.it.marketo.sdk;

import com.smartling.marketo.sdk.AuthenticationErrorException;
import com.smartling.marketo.sdk.MarketoClientManager;
import com.smartling.marketo.sdk.rest.MarketoRestClientManager;
import org.junit.Test;

public class RestIntegrationTest extends BaseIntegrationTest{

    @Test(expected = AuthenticationErrorException.class)
    public void shouldThrowAuthenticationError() throws Exception {
        MarketoClientManager invalid = MarketoRestClientManager.create(identityEndpoint, restEndpoint).withCredentials("notCachedClientId", "invalid");
        invalid.getMarketoEmailClient().listEmails(0, 1);
    }
}
