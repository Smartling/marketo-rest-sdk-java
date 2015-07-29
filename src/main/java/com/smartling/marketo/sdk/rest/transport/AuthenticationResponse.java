package com.smartling.marketo.sdk.rest.transport;

import com.fasterxml.jackson.annotation.JsonProperty;

class AuthenticationResponse {
    @JsonProperty("access_token")
    private String accessToken;

    @JsonProperty("expires_in")
    private long expirationInterval;

    @JsonProperty("error")
    private String error;

    @JsonProperty("error_description")
    private String errorDescription;

    public String getAccessToken() {
        return accessToken;
    }

    public String getErrorDescription() {
        return errorDescription;
    }

    public long getExpirationInterval() {
        return expirationInterval;
    }

    public String getError() {
        return error;
    }
}
