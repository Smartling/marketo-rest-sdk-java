package com.smartling.marketo.sdk.rest.transport;

import com.google.common.base.Preconditions;
import com.smartling.marketo.sdk.AuthenticationErrorException;
import com.smartling.marketo.sdk.MarketoApiException;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.time.LocalDateTime;

import static java.time.LocalDateTime.now;

public class BasicTokenProvider implements TokenProvider {

    static final byte APPROXIMATE_RESPONSE_TIME = 2;

    @Override
    public Token authenticate(ClientConnectionData client) throws MarketoApiException {
        Client wsClient = client.getWsClient();
        String clientId = client.getClientId();
        String clientSecret = client.getClientSecret();

        Response response = wsClient.target(client.getIdentityUrl()).path("/oauth/token")
                .queryParam("grant_type", "client_credentials")
                .queryParam("client_id", clientId)
                .queryParam("client_secret", clientSecret)
                .request(MediaType.APPLICATION_JSON_TYPE).get();

        AuthenticationResponse authenticationResponse = response.readEntity(AuthenticationResponse.class);
        if (response.getStatus() == 200) {
            return extractToken(authenticationResponse);
        } else if (response.getStatus() == 401) {
            throw new AuthenticationErrorException(String.valueOf(response.getStatus()),
                    String.format("%s: %s", authenticationResponse.getError(), authenticationResponse.getErrorDescription()));
        } else {
            throw new MarketoApiException(String.valueOf(response.getStatus()),
                    String.format("%s: %s", authenticationResponse.getError(), authenticationResponse.getErrorDescription()));
        }
    }

    private Token extractToken(AuthenticationResponse response) {

        long expirationInterval = response.getExpirationInterval();
        Preconditions.checkState(expirationInterval > 0, "Expiration interval should be > 0, but equal to %s", expirationInterval);

        LocalDateTime validDateTime = now().plusSeconds(expirationInterval).minusSeconds(APPROXIMATE_RESPONSE_TIME);
        return new Token(validDateTime, response.getAccessToken());
    }
}
