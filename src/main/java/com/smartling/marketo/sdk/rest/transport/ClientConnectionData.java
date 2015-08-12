package com.smartling.marketo.sdk.rest.transport;

import javax.ws.rs.client.Client;

class ClientConnectionData {
    private final Client wsClient;
    private final String identityUrl;
    private final String clientId;
    private final String clientSecret;

    public ClientConnectionData(Client wsClient, String identityUrl, String clientId, String clientSecret) {
        this.wsClient = wsClient;
        this.identityUrl = identityUrl;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
    }

    public Client getWsClient() {
        return wsClient;
    }

    public String getIdentityUrl() {
        return identityUrl;
    }

    public String getClientId() {
        return clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }
}