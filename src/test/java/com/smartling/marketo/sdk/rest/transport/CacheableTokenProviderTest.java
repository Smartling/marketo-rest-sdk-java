package com.smartling.marketo.sdk.rest.transport;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.smartling.marketo.sdk.MarketoApiException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import javax.ws.rs.ProcessingException;


import static java.time.LocalDateTime.now;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CacheableTokenProviderTest extends BaseTransportTest {

    private static final String ACCESS_TOKEN = "accessToken";

    private ClientConnectionData clientConnectionData = getClientConnectionData(CLIENT_ID);

    @Rule
    public final WireMockRule wireMockRule = new WireMockRule(PORT);

    @Rule
    public final ExpectedException thrown = ExpectedException.none();

    private BasicTokenProvider basicTokenProvider = mock(BasicTokenProvider.class);

    private CacheableTokenProvider testedInstance;

    @Before
    public void setUp() throws Exception {
        testedInstance = new CacheableTokenProvider(basicTokenProvider);
        when(basicTokenProvider.authenticate(clientConnectionData)).thenReturn(new Token(now().plusHours(1), ACCESS_TOKEN));
    }

    @Test
    public void shouldCallBasicTokenProviderAuthenticateOnceForOneClient() throws Exception {

        Token token = testedInstance.authenticate(clientConnectionData);
        Token token2 = testedInstance.authenticate(clientConnectionData);

        Mockito.verify(basicTokenProvider, times(1)).authenticate(clientConnectionData);

        assertThat(token.getAccessToken()).isEqualTo(token2.getAccessToken());
    }

    @Test
    public void shouldCallBasicTokenProviderAuthenticateEachTimeForNewClient() throws Exception {
        String anotherToken = "accessToken2";
        String anotherClient = "anotherClient";
        ClientConnectionData anotherClientConnectionData = getClientConnectionData(anotherClient);
        when(basicTokenProvider.authenticate(anotherClientConnectionData)).thenReturn(new Token(now().plusHours(1), anotherToken));

        testedInstance.authenticate(clientConnectionData);
        testedInstance.authenticate(anotherClientConnectionData);

        Mockito.verify(basicTokenProvider, times(1)).authenticate(clientConnectionData);
        Mockito.verify(basicTokenProvider, times(1)).authenticate(anotherClientConnectionData);
    }

    @Test
    public void shouldHandleMarketoException() throws Exception {

        when(basicTokenProvider.authenticate(clientConnectionData)).thenThrow(new MarketoApiException("401", "unauthorized: Error!"));

        thrown.expect(MarketoApiException.class);
        thrown.expectMessage("unauthorized: Error!");
        thrown.expect(exceptionWithCode("401"));

        testedInstance.authenticate(clientConnectionData);
    }

    @Test
    public void shouldHandleOtherException() throws Exception {

        when(basicTokenProvider.authenticate(clientConnectionData)).thenThrow(new ProcessingException("Socket timeout exception"));

        thrown.expect(ProcessingException.class);
        thrown.expectMessage("Socket timeout exception");

        testedInstance.authenticate(clientConnectionData);
    }

    @Test
    public void tokenShouldBeInvalidatedAndLoadedAgainIfTokenIsNotValid() throws Exception {

        String anotherToken = "anotherToken";
        when(basicTokenProvider.authenticate(clientConnectionData))
                .thenReturn(new Token(now().minusMinutes(1), ACCESS_TOKEN))
                .thenReturn(new Token(now().plusHours(1), anotherToken));

        testedInstance.authenticate(clientConnectionData);

        verify(basicTokenProvider, times(2)).authenticate(clientConnectionData);

    }

}