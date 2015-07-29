package com.smartling.marketo.sdk.rest.transport;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Preconditions;
import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import com.smartling.marketo.sdk.HasToBeMappedToJson;
import com.smartling.marketo.sdk.rest.Command;
import com.smartling.marketo.sdk.MarketoApiException;
import org.glassfish.jersey.client.ClientProperties;
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
import java.io.UnsupportedEncodingException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

public class HttpCommandExecutor {
    private final String identityUrl;
    private final String restUrl;
    private final String clientId;
    private final String clientSecret;

    private final Client client;

    private volatile Supplier<String> tokenSupplier = Suppliers.ofInstance(null);
    private final static ObjectMapper objectMapper = new ObjectMapper();

    public HttpCommandExecutor(String identityUrl, String restUrl, String clientId, String clientSecret) {
        this.identityUrl = identityUrl;
        this.restUrl = restUrl;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.client = ClientBuilder.newClient().register(JacksonFeature.class).register(ObjectMapperProvider.class);
    }

    public void setConnectionTimeout(int timeout) {
        client.property(ClientProperties.CONNECT_TIMEOUT, timeout);
    }

    public void setSocketReadTimeout(int timeout) {
        client.property(ClientProperties.READ_TIMEOUT, timeout);
    }

    public <T> T execute(final Command<T> command) throws MarketoApiException {
        String token = getToken();

        WebTarget target = buildWebTarget(client, command);
        Invocation.Builder invocationBuilder = target.request(MediaType.APPLICATION_JSON_TYPE).header("Authorization", "Bearer " + token);
        MarketoResponse<T> marketoResponse = execute(invocationBuilder, command);

        if (marketoResponse.isSuccess()) {
            return marketoResponse.getResult();
        } else {
            MarketoResponse.Error firstError = marketoResponse.getErrors().get(0);
            throw new MarketoApiException(firstError.getCode(), firstError.getMessage());
        }
    }

    private <T> MarketoResponse<T> execute(Invocation.Builder invocationBuilder, Command<T> command) throws MarketoApiException {
        GenericType<MarketoResponse<T>> typeToken = new GenericType<MarketoResponse<T>>(createReturnType(command)) {
        };

        MarketoResponse<T> marketoResponse;
        if ("POST".equalsIgnoreCase(command.getMethod())) {
            Form form = toForm(processParameters(command.getParameters(), false));
            Entity<?> entity = Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED_TYPE.withCharset("UTF-8"));
            marketoResponse = invocationBuilder.post(entity, typeToken);
        } else {
            marketoResponse = invocationBuilder.method(command.getMethod(), typeToken);
        }

        return marketoResponse;
    }

    private Map<String, Object> processParameters(Map<String, Object> parameters, boolean needUrlEncode) throws MarketoApiException {

        try {
            Map<String, Object> processedParameters = new HashMap<>(parameters);
            for (String key : processedParameters.keySet()) {
                Object value = processedParameters.get(key);
                Object newValue = value;
                if (value instanceof HasToBeMappedToJson) {
                    newValue = objectMapper.writeValueAsString(newValue);
                    if (needUrlEncode) {
                        newValue = URLEncoder.encode((String) newValue, "UTF-8");
                    }
                }
                processedParameters.put(key, newValue);
            }
            return processedParameters;
        } catch (UnsupportedEncodingException | JsonProcessingException e) {
            throw new MarketoApiException(e.getMessage());
        }
    }

    private <T> WebTarget buildWebTarget(Client client, Command<T> command) throws MarketoApiException {
        WebTarget target = client.target(restUrl).path(command.getPath());
        if ("GET".equalsIgnoreCase(command.getMethod())) {
            for (Entry<String, Object> param : processParameters(command.getParameters(), true).entrySet()) {
                target = target.queryParam(param.getKey(), param.getValue());
            }
        }

        return target;
    }

    private static Form toForm(Map<String, Object> parameters) {
        Form form = new Form();
        for (Entry<String, Object> param : parameters.entrySet()) {
            form.param(param.getKey(), param.getValue().toString());
        }

        return form;
    }

    private static <T> ParameterizedType createReturnType(final Command<T> command) {
        return new ParameterizedType() {
            @Override
            public Type[] getActualTypeArguments() {
                return new Type[] { command.getResultType() };
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

            long expirationInterval = authenticationResponse.getExpirationInterval();
            Preconditions.checkState(expirationInterval > 0, "Expiration interval should be > 0, but equal to %s", expirationInterval);

            tokenSupplier = Suppliers
                    .memoizeWithExpiration(new OneTimeSupplier<>(authenticationResponse.getAccessToken()), expirationInterval,
                            TimeUnit.SECONDS);
            token = tokenSupplier.get();
        }

        return token;
    }

    private AuthenticationResponse authenticate() throws MarketoApiException {
        Response response = client.target(identityUrl).path("/oauth/token").queryParam("grant_type", "client_credentials")
                .queryParam("client_id", clientId).queryParam("client_secret", clientSecret).request(MediaType.APPLICATION_JSON_TYPE).get();

        AuthenticationResponse authenticationResponse = response.readEntity(AuthenticationResponse.class);
        if (response.getStatus() == 200) {
            return authenticationResponse;
        } else {
            throw new MarketoApiException(String.valueOf(response.getStatus()),
                    String.format("%s: %s", authenticationResponse.getError(), authenticationResponse.getErrorDescription()));
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
