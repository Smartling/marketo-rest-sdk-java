package com.smartling.marketo.sdk.rest.transport.logging;

import org.glassfish.jersey.message.internal.ReaderWriter;

import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.client.ClientResponseContext;
import javax.ws.rs.client.ClientResponseFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MultivaluedMap;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PipedOutputStream;
import java.io.PipedInputStream;
import java.io.InputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;

public class JsonClientLoggingFilter implements ClientRequestFilter, ClientResponseFilter {

    public static final String REQUEST_BODY_PROPERTY = "requestBodyStream";
    public static final String REQUEST_START_TIME = "requestStartTime";

    private final RequestResponseLogger logger;

    public JsonClientLoggingFilter(RequestResponseLogger logger) {
        this.logger = logger;
    }

    @Override
    public void filter(ClientRequestContext clientRequestContext) throws IOException {
        if (logger.enabled() && clientRequestContext.hasEntity() && !isAuthRequest(clientRequestContext)) {
            PipedOutputStream requestBodyOutputStream = new PipedOutputStream();
            PipedInputStream requestBodyLoggingStream = new PipedInputStream();
            requestBodyOutputStream.connect(requestBodyLoggingStream);

            clientRequestContext.setEntityStream(requestBodyOutputStream);
            clientRequestContext.setProperty(REQUEST_BODY_PROPERTY, requestBodyLoggingStream);
        }
        clientRequestContext.setProperty(REQUEST_START_TIME, System.currentTimeMillis());
    }

    @Override
    public void filter(ClientRequestContext clientRequestContext, ClientResponseContext clientResponseContext) throws IOException {
        if (logger.enabled() && !isAuthRequest(clientRequestContext)) {
            logRequest(clientRequestContext);
            logResponse(clientRequestContext, clientResponseContext);
        }
    }

    private boolean isAuthRequest(ClientRequestContext requestContext) {
        return requestContext.getUri().toString().contains("auth");
    }

    private void logRequest(ClientRequestContext clientRequestContext) throws IOException {
        List<LoggingArgument> requestArguments = new ArrayList<>();
        Formatter message = new Formatter();
        message.format("{} --->\n{} {}\n");

        requestArguments.add(new LoggingArgument("method", clientRequestContext.getMethod()));
        requestArguments.add(new LoggingArgument("url", clientRequestContext.getUri().toString()));
        requestArguments.add(new LoggingArgument("direction", "request"));

        MultivaluedMap<String, Object> headers = clientRequestContext.getHeaders();
        for (String headerName : headers.keySet()) {
            for (Object headerValue : headers.get(headerName)) {
                if (!HttpHeaders.AUTHORIZATION.equalsIgnoreCase(headerName)) {
                    message.format("%s: %s\n", headerName, headerValue);
                }
            }
        }

        if (clientRequestContext.hasEntity()) {
            String requestBody = getRequestBody(clientRequestContext);
            message.format("\n%s\n", requestBody);
            requestArguments.add(new LoggingArgument("bodyLength", requestBody.length()));
        }

        logger.log(message.toString(), requestArguments);
    }

    private String getRequestBody(ClientRequestContext clientRequestContext) throws IOException {
        InputStream requestBody = (InputStream) clientRequestContext.getProperty(REQUEST_BODY_PROPERTY);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ReaderWriter.writeTo(requestBody, out);
        return out.toString();
    }

    private void logResponse(ClientRequestContext requestContext, ClientResponseContext responseContext) throws IOException {
        Long requestStartTime = (Long) requestContext.getProperty(REQUEST_START_TIME);
        List<LoggingArgument> responseArguments = new ArrayList<>();
        Formatter message = new Formatter();
        message.format("{} --->\n{} {}\n");

        responseArguments.add(new LoggingArgument("method", requestContext.getMethod()));
        responseArguments.add(new LoggingArgument("url", requestContext.getUri().toString()));
        responseArguments.add(new LoggingArgument("direction", "response"));
        responseArguments.add(new LoggingArgument("status", responseContext.getStatus()));
        responseArguments.add(new LoggingArgument("elapsedTime", System.currentTimeMillis() - requestStartTime));

        MultivaluedMap<String, String> headers = responseContext.getHeaders();
        for (String headerName : headers.keySet()) {
            for (Object headerValue : headers.get(headerName)) {
                if (!HttpHeaders.AUTHORIZATION.equalsIgnoreCase(headerName)) {
                    message.format("%s: %s\n", headerName, headerValue);
                }
            }
        }

        if (responseContext.hasEntity()) {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ReaderWriter.writeTo(responseContext.getEntityStream(), out);
            responseContext.setEntityStream(new ByteArrayInputStream(out.toByteArray()));
            message.format("\n%s\n", out);
            responseArguments.add(new LoggingArgument("bodyLength", out.toByteArray().length));
        }

        logger.log(message.toString(), responseArguments);
    }
}
