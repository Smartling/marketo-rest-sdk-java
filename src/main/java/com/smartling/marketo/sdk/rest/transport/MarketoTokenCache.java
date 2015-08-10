package com.smartling.marketo.sdk.rest.transport;

import com.google.common.base.Preconditions;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.smartling.marketo.sdk.MarketoApiException;

import java.time.LocalDateTime;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static com.smartling.marketo.sdk.rest.transport.AuthManager.authenticate;
import static com.smartling.marketo.sdk.rest.transport.HttpCommandExecutor.ClientConnectionData;

import static java.time.LocalDateTime.now;

public class MarketoTokenCache {

    private final Lock lock = new ReentrantLock();

    private final Cache<String, Token> tokenCache = CacheBuilder.newBuilder().build();

    static final byte APPROXIMATE_RESPONSE_TIME = 2;

    public String getAccessToken(final ClientConnectionData clientConnectionData) throws MarketoApiException {

        Token token = getFromCacheOrLoad(clientConnectionData);

        if (isNotValid(token)) {
            token = invalidateAndLoadNewToken(clientConnectionData);
        }

        return token.getAccessToken();
    }

    private Token invalidateAndLoadNewToken(final ClientConnectionData clientConnectionData) throws MarketoApiException {
        Token token;
        lock.lock();
        try {
            token = getFromCacheOrLoad(clientConnectionData);
            if (isNotValid(token)) {
                tokenCache.invalidate(clientConnectionData.getClientId());
                token = getFromCacheOrLoad(clientConnectionData);
            }
        } finally {
            lock.unlock();
        }
        return token;
    }

    private Token getFromCacheOrLoad(final ClientConnectionData clientConnectionData) throws MarketoApiException {
        try {
            return tokenCache.get(clientConnectionData.getClientId(), new Callable<Token>() {

                @Override
                public Token call() throws Exception {
                    return extractToken(authenticate(clientConnectionData));
                }
            });
        } catch (ExecutionException e) {
            if (e.getCause().getClass() == MarketoApiException.class) throw (MarketoApiException) e.getCause();
            else throw new RuntimeException(e.getCause());
        }
    }

    private boolean isNotValid(Token token) {
        return token.getValidDateTime().isBefore(now());
    }

    private Token extractToken(AuthenticationResponse response) {

        long expirationInterval = response.getExpirationInterval();
        Preconditions.checkState(expirationInterval > 0, "Expiration interval should be > 0, but equal to %s", expirationInterval);

        LocalDateTime validDateTime = now().plusSeconds(expirationInterval).minusSeconds(APPROXIMATE_RESPONSE_TIME);
        return new Token(validDateTime, response.getAccessToken());
    }

    private static final class Token {
        private final LocalDateTime validDateTime;
        private final String accessToken;

        private Token(LocalDateTime validDateTime, String accessToken) {
            this.validDateTime = validDateTime;
            this.accessToken = accessToken;
        }

        public LocalDateTime getValidDateTime() {
            return validDateTime;
        }

        public String getAccessToken() {
            return accessToken;
        }
    }

}
