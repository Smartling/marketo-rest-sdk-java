package com.smartling.marketo.sdk.rest.transport;

import jakarta.ws.rs.client.Client;

class ClientConnectionData {
    private final Client wsClient;
    private final String identityUrl;
    private final String clientId;
    private final String clientSecret;

    ClientConnectionData(Client wsClient, String identityUrl, String clientId, String clientSecret) {
        this.wsClient = wsClient;
        this.identityUrl = identityUrl;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
    }

    Client getWsClient() {
        return wsClient;
    }

    String getIdentityUrl() {
        return identityUrl;
    }

    String getClientId() {
        return clientId;
    }

    String getClientSecret() {
        return clientSecret;
    }
}