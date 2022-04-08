package com.smartling.it.marketo.sdk;

import com.smartling.marketo.sdk.MarketoApiException;
import com.smartling.marketo.sdk.MarketoEmailClient;
import com.smartling.marketo.sdk.rest.MarketoRestClientManager;
import org.junit.Test;

/**
 * feel free to use this class for your experiments on live Marketo
 */
public class RealMarketoConnectionTest {

//    @Test
    public void test() throws MarketoApiException {
        MarketoRestClientManager clientManager = MarketoRestClientManager.create(
                        "<your_marketo_host>/identity",
                        "<your_marketo_host>/rest")
                .withCredentials(
                        "<your_client_id>",
                        "<your_client_secret>");
        MarketoEmailClient emailClient = clientManager.getMarketoEmailClient();
        emailClient.unapprove(5132);
        emailClient.delete(5132);
    }

}
