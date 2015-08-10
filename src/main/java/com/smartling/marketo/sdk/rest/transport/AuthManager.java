package com.smartling.marketo.sdk.rest.transport;

import com.smartling.marketo.sdk.MarketoApiException;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static com.smartling.marketo.sdk.rest.transport.HttpCommandExecutor.ClientConnectionData;

public class AuthManager {

    public static AuthenticationResponse authenticate(ClientConnectionData client) throws MarketoApiException {
        Response response = client.getWsClient().target(client.getIdentityUrl()).path("/oauth/token").queryParam("grant_type", "client_credentials")
                .queryParam("client_id", client.getClientId()).queryParam("client_secret", client.getClientSecret()).request(MediaType.APPLICATION_JSON_TYPE).get();

        AuthenticationResponse authenticationResponse = response.readEntity(AuthenticationResponse.class);
        if (response.getStatus() == 200) {
            return authenticationResponse;
        } else {
            throw new MarketoApiException(String.valueOf(response.getStatus()),
                    String.format("%s: %s", authenticationResponse.getError(), authenticationResponse.getErrorDescription()));
        }
    }

}
