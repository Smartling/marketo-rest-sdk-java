package com.smartling.marketo.sdk.rest;

import com.smartling.marketo.sdk.MarketoApiException;

import java.io.Closeable;
import java.io.IOException;
import java.util.Objects;

final class RetryingHttpCommandExecutor implements HttpCommandExecutor, Closeable {
    private final HttpCommandExecutor executor;
    private final RetryPolicy retryPolicy;

    RetryingHttpCommandExecutor(HttpCommandExecutor executor, RetryPolicy retryPolicy) {
        this.executor = Objects.requireNonNull(executor, "Executor should not be null");
        this.retryPolicy = Objects.requireNonNull(retryPolicy, "Retry policy should not be null");
    }

    @Override
    public void close() throws IOException {
        if (executor instanceof Closeable) {
            ((Closeable) executor).close();
        }
    }

    @Override
    public <T> T execute(Command<T> command) throws MarketoApiException {
        RetryPolicy.Execution execution = retryPolicy.newExecution();

        while (true) {
            try {
                return executor.execute(command);
            } catch (RequestLimitExceededException e) {
                execution.waitOrRethrow(e);
            }
        }
    }
}
