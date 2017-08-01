package com.smartling.marketo.sdk.rest.transport;

import java.time.LocalDateTime;

import static java.time.LocalDateTime.now;

final class Token {
    private final LocalDateTime validDateTime;
    private final String accessToken;

    public Token(LocalDateTime validDateTime, String accessToken) {
        this.validDateTime = validDateTime;
        this.accessToken = accessToken;
    }

    boolean isValid() {
        return validDateTime.isAfter(now());
    }

    String getAccessToken() {
        return accessToken;
    }
}