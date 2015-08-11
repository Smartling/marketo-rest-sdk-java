package com.smartling.marketo.sdk.rest.transport;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.verify;

@RunWith(MockitoJUnitRunner.class)
public class CacheableTokenProviderTest extends BaseTransportTest {

    @Rule
    public final WireMockRule wireMockRule = new WireMockRule(PORT);

    private final CacheableTokenProvider testedInstance = new CacheableTokenProvider(new BasicTokenProvider());

    @Before
    public void setUp() throws Exception {
        stubFor(get(urlStartingWith("/identity")).willReturn(aJsonResponse("{\"access_token\": \"\",\"expires_in\": 100000}")));
    }

    @Test
    public void shouldSendOnlyOneRequestForTheSameClient() throws Exception {

        testedInstance.authenticate(getClientConnectionData(CLIENT_ID));
        testedInstance.authenticate(getClientConnectionData(CLIENT_ID));

        verify(1, getRequestedFor(urlStartingWith("/identity")));
    }

    @Test
    public void shouldSendSeparateRequestsForDifferentClients() throws Exception {

        testedInstance.authenticate(getClientConnectionData(CLIENT_ID));
        testedInstance.authenticate(getClientConnectionData("another_client"));

        verify(2, getRequestedFor(urlStartingWith("/identity")));
    }

}