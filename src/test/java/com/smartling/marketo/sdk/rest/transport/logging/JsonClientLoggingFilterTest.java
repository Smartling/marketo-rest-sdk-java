package com.smartling.marketo.sdk.rest.transport.logging;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientResponseContext;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;

import java.io.ByteArrayInputStream;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.net.URI;
import java.util.Arrays;
import java.util.List;

import static com.smartling.marketo.sdk.rest.transport.logging.JsonClientLoggingFilter.REQUEST_BODY_PROPERTY;
import static com.smartling.marketo.sdk.rest.transport.logging.JsonClientLoggingFilter.REQUEST_START_TIME;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class JsonClientLoggingFilterTest {

    @Mock
    private RequestResponseLogger logger;

    @Mock
    private ClientRequestContext requestContext;

    @Mock
    private ClientResponseContext responseContext;

    @InjectMocks
    private JsonClientLoggingFilter target;

    @Test
    public void filterRequestContextWithoutBody() throws Exception{
        when(logger.enabled()).thenReturn(true);
        when(requestContext.hasEntity()).thenReturn(false);

        target.filter(requestContext);

        verify(requestContext, never()).setEntityStream(any(OutputStream.class));
        verify(requestContext).setProperty(anyString(), any());
    }

    @Test
    public void dontFilterRequestContextForAuthRequest() throws Exception{
        when(logger.enabled()).thenReturn(true);
        when(requestContext.hasEntity()).thenReturn(true);
        when(requestContext.getUri()).thenReturn(URI.create("http://some.com/oauth/request"));
        target.filter(requestContext);

        verify(requestContext, never()).setEntityStream(any(OutputStream.class));
        verify(requestContext).setProperty(anyString(), any());
    }

    @Test
    public void dontFilterRequestContextWhenLoggingDisabled() throws Exception{
        when(logger.enabled()).thenReturn(false);
        when(requestContext.hasEntity()).thenReturn(true);
        when(requestContext.getUri()).thenReturn(URI.create("http://some.com/emails"));
        target.filter(requestContext);

        verify(requestContext, never()).setEntityStream(any(OutputStream.class));
        verify(requestContext).setProperty(anyString(), any());
    }

    @Test
    public void filterRequestContext() throws Exception{
        when(logger.enabled()).thenReturn(true);
        when(requestContext.hasEntity()).thenReturn(true);
        when(requestContext.getUri()).thenReturn(URI.create("http://some.com/emails"));

        target.filter(requestContext);

        verify(requestContext).setEntityStream(any(PipedOutputStream.class));
        verify(requestContext).setProperty(eq(REQUEST_BODY_PROPERTY), any(PipedInputStream.class));
        verify(requestContext).setProperty(eq(REQUEST_START_TIME), anyLong());
    }

    @Test
    public void dontFilterResponseContextForAuthRequest() throws Exception{
        when(logger.enabled()).thenReturn(true);
        when(requestContext.getUri()).thenReturn(URI.create("http://some.com/oauth/request"));

        target.filter(requestContext, responseContext);

        verify(logger, never()).log(anyString(), anyList());
    }

    @Test
    public void dontFilterResponseContextWhenLoggingDisabled() throws Exception{
        when(logger.enabled()).thenReturn(false);
        when(requestContext.getUri()).thenReturn(URI.create("http://some.com/emails"));

        target.filter(requestContext, responseContext);

        verify(logger, never()).log(anyString(), anyList());
    }

    @Test
    public void filterRequestAndResponse() throws Exception{
        ByteArrayInputStream requestBody = new ByteArrayInputStream("request-body".getBytes());
        ByteArrayInputStream responseBody = new ByteArrayInputStream("response-body".getBytes());

        MultivaluedMap<String, Object> requestHeaders = new MultivaluedHashMap<>();
        requestHeaders.putSingle("Content-Type", "application/json");
        requestHeaders.putSingle(HttpHeaders.AUTHORIZATION, "Some token");

        MultivaluedMap<String, String> responseHeaders = new MultivaluedHashMap<>();
        responseHeaders.putSingle("Content-Type", "application/json");
        responseHeaders.putSingle(HttpHeaders.AUTHORIZATION, "Some token");

        when(logger.enabled()).thenReturn(true);
        when(requestContext.getProperty(REQUEST_BODY_PROPERTY)).thenReturn(requestBody);
        when(requestContext.getProperty(REQUEST_START_TIME)).thenReturn(System.currentTimeMillis() - 2000);
        when(requestContext.hasEntity()).thenReturn(true);
        when(requestContext.getMethod()).thenReturn("POST");
        when(requestContext.getUri()).thenReturn(URI.create("http://some.com/emails"));
        when(requestContext.getHeaders()).thenReturn(requestHeaders);

        when(responseContext.getHeaders()).thenReturn(responseHeaders);
        when(responseContext.hasEntity()).thenReturn(true);
        when(responseContext.getEntityStream()).thenReturn(responseBody);
        when(responseContext.getStatus()).thenReturn(200);

        target.filter(requestContext, responseContext);

        ArgumentCaptor<String> logMsgCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<List> logArgsCaptor = ArgumentCaptor.forClass(List.class);

        verify(logger, times(2)).log(logMsgCaptor.capture(), logArgsCaptor.capture());

        assertThat(logMsgCaptor.getAllValues().get(0)).contains(Arrays.asList("Content-Type", "request-body"));
        assertThat(logMsgCaptor.getAllValues().get(0)).doesNotContain(Arrays.asList(HttpHeaders.AUTHORIZATION, "Some token"));

        assertThat(logMsgCaptor.getAllValues().get(1)).contains(Arrays.asList("Content-Type", "response-body"));
        assertThat(logMsgCaptor.getAllValues().get(1)).doesNotContain(Arrays.asList(HttpHeaders.AUTHORIZATION, "Some token"));
    }
}