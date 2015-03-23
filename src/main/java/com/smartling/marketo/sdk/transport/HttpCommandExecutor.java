package com.smartling.marketo.sdk.transport;

import com.smartling.marketo.sdk.Command;
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

public class HttpCommandExecutor {
    private final String identityUrl;
    private final String restUrl;
    private final String clientId;
    private final String clientSecret;

    public HttpCommandExecutor(String identityUrl, String restUrl, String clientId, String clientSecret) {
        this.identityUrl = identityUrl;
        this.restUrl = restUrl;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
    }

    public <T> T execute(final Command<T> command) throws MarketoApiException {
        Client client = ClientBuilder.newClient().register(JacksonFeature.class).register(ObjectMapperProvider.class);

        String token = getToken(client);

        ParameterizedType parameterizedType = createReturnType(command);

        WebTarget target = client.target(restUrl).path(command.getPath());
        if ("GET".equalsIgnoreCase(command.getMethod())) {
            for (Map.Entry<String, Object> param : command.getParameters().entrySet()) {
                target = target.queryParam(param.getKey(), param.getValue());
            }
        }

        Invocation.Builder invocationBuilder = target.request(MediaType.APPLICATION_JSON_TYPE)
                .header("Authorization", "Bearer " + token);
        GenericType<MarketoResponse<T>> typeToken = new GenericType<MarketoResponse<T>>(parameterizedType) {};

        MarketoResponse<T> marketoResponse;
        if ("POST".equalsIgnoreCase(command.getMethod())) {
            marketoResponse = invocationBuilder.post(Entity.form(toForm(command.getParameters())), typeToken);
        } else {
            marketoResponse = invocationBuilder.method(command.getMethod(), typeToken);
        }

        if (marketoResponse.isSuccess()) {
            return marketoResponse.getResult();
        } else {
            throw new MarketoApiException(marketoResponse.getErrors().get(0).getMessage());
        }
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

    private String getToken(Client client) throws MarketoApiException {
        Response response = client.target(identityUrl).path("/oauth/token")
                .queryParam("grant_type", "client_credentials")
                .queryParam("client_id", clientId)
                .queryParam("client_secret", clientSecret)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get();

        AuthenticationResponse authenticationResponse = response.readEntity(AuthenticationResponse.class);
        if (response.getStatus() == 200) {
            return authenticationResponse.getAccessToken();
        } else {
            throw new MarketoApiException(authenticationResponse.getErrorDescription());
        }

    }
}
