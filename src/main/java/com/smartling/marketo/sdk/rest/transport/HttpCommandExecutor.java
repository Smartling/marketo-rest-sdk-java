package com.smartling.marketo.sdk.rest.transport;

import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import com.smartling.marketo.sdk.rest.Command;
import com.smartling.marketo.sdk.MarketoApiException;
import org.glassfish.jersey.jackson.JacksonFeature;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class HttpCommandExecutor {
    private final String identityUrl;
    private final String restUrl;
    private final String clientId;
    private final String clientSecret;

    private final Client client;

    private volatile Supplier<String> tokenSupplier = Suppliers.ofInstance(null);

    public HttpCommandExecutor(String identityUrl, String restUrl, String clientId, String clientSecret) {
        this.identityUrl = identityUrl;
        this.restUrl = restUrl;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.client = ClientBuilder.newClient().register(JacksonFeature.class).register(ObjectMapperProvider.class);
    }

    public <T> T execute(final Command<T> command) throws MarketoApiException {
        String token = getToken();

        WebTarget target = buildWebTarget(client, command);
        Invocation.Builder invocationBuilder = target.request(MediaType.APPLICATION_JSON_TYPE)
                .header("Authorization", "Bearer " + token);
        MarketoResponse<T> marketoResponse = execute(invocationBuilder, command);

        if (marketoResponse.isSuccess()) {
            return marketoResponse.getResult();
        } else {
            MarketoResponse.Error firstError = marketoResponse.getErrors().get(0);
            throw new MarketoApiException(firstError.getCode(), firstError.getMessage());
        }
    }

    private <T> MarketoResponse<T> execute(Invocation.Builder invocationBuilder, Command<T> command) {
        GenericType<MarketoResponse<T>> typeToken = new GenericType<MarketoResponse<T>>(createReturnType(command)) {};

        MarketoResponse<T> marketoResponse;
        if ("POST".equalsIgnoreCase(command.getMethod())) {
            Form form = toForm(command.getParameters());
            Entity<?> entity = Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED_TYPE.withCharset("UTF-8"));
            marketoResponse = invocationBuilder.post(entity, typeToken);
        } else {
            marketoResponse = invocationBuilder.method(command.getMethod(), typeToken);
        }

        return marketoResponse;
    }

    private <T> WebTarget buildWebTarget(Client client, Command<T> command) {
        WebTarget target = client.target(restUrl).path(command.getPath());
        if ("GET".equalsIgnoreCase(command.getMethod())) {
            for (Map.Entry<String, Object> param : command.getParameters().entrySet()) {
                target = target.queryParam(param.getKey(), param.getValue());
            }
        }

        return target;
    }

    private static Form toForm(Map<String, Object> parameters) {
        Form form = new Form();
        for (Map.Entry<String, Object> param : parameters.entrySet()) {
            form.param(param.getKey(), param.getValue().toString());
        }

        return form;
    }

    private static <T> ParameterizedType createReturnType(final Command<T> command) {
        return new ParameterizedType() {
                @Override
                public Type[] getActualTypeArguments() {
                    return new Type[] {command.getResultType()};
                }

                @Override
                public Type getRawType() {
                    return MarketoResponse.class;
                }

                @Override
                public Type getOwnerType() {
                    return MarketoResponse.class;
                }
            };
    }

    private String getToken() throws MarketoApiException {
        String token = tokenSupplier.get();
        if (token == null) {
            AuthenticationResponse authenticationResponse = authenticate();

            tokenSupplier = Suppliers.memoizeWithExpiration(new OneTimeSupplier<>(authenticationResponse.getAccessToken()),
                    authenticationResponse.getExpirationInterval(), TimeUnit.SECONDS);
            token = tokenSupplier.get();
        }

        return token;
    }

    private AuthenticationResponse authenticate() throws MarketoApiException {
        Response response = client.target(identityUrl).path("/oauth/token")
                .queryParam("grant_type", "client_credentials")
                .queryParam("client_id", clientId)
                .queryParam("client_secret", clientSecret)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get();

        AuthenticationResponse authenticationResponse = response.readEntity(AuthenticationResponse.class);
        if (response.getStatus() == 200) {
            return authenticationResponse;
        } else {
            throw new MarketoApiException(authenticationResponse.getErrorDescription());
        }
    }

    private static final class OneTimeSupplier<T> implements Supplier<T> {
        private T value;

        private OneTimeSupplier(T value) {
            this.value = value;
        }

        @Override
        public T get() {
            T copy = value;
            value = null;

            return copy;
        }
    }
}
