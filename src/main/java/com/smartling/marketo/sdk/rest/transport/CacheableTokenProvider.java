package com.smartling.marketo.sdk.rest.transport;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.util.concurrent.UncheckedExecutionException;
import com.smartling.marketo.sdk.MarketoApiException;

import javax.ws.rs.ProcessingException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static com.google.common.base.Throwables.propagate;

public class CacheableTokenProvider implements TokenProvider {

    private final TokenProvider tokenProvider;

    private final Lock lock = new ReentrantLock();

    private final Cache<String, Token> tokenCache = CacheBuilder.newBuilder().build();


    public CacheableTokenProvider(TokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    @Override
    public Token authenticate(ClientConnectionData clientConnectionData) throws MarketoApiException {

        Token token = getFromCacheOrLoad(clientConnectionData);

        if (!token.isValid()) {
            token = invalidateAndLoadNewToken(clientConnectionData);
        }

        return token;
    }

    private Token invalidateAndLoadNewToken(final ClientConnectionData clientConnectionData) throws MarketoApiException {
        Token token;
        lock.lock();
        try {
            token = getFromCacheOrLoad(clientConnectionData);
            if (!token.isValid()) {
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
                    return tokenProvider.authenticate(clientConnectionData);
                }
            });
        } catch (ExecutionException | UncheckedExecutionException e) {
            if (e.getCause() instanceof MarketoApiException) {
                throw (MarketoApiException) e.getCause();
            } else if (e.getCause() instanceof ProcessingException) {
                throw (ProcessingException) e.getCause();
            } else {
                throw propagate(e.getCause());
            }
        }
    }

}
