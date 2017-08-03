package com.smartling.marketo.sdk.rest.retry;

import com.google.common.base.Preconditions;
import com.smartling.marketo.sdk.MarketoApiException;
import com.smartling.marketo.sdk.rest.RetryPolicy;

public class ExponentialBackOffRetryPolicy implements RetryPolicy {
    private final int maxRetries;
    private final long initialIntervalMillis;

    public ExponentialBackOffRetryPolicy(int maxRetries, long initialIntervalMillis) {
        Preconditions.checkArgument(maxRetries > 0, "Max. number of retries should be greater then zero");
        Preconditions.checkArgument(initialIntervalMillis > 0, "interval between retries should be greater then zero");

        this.maxRetries = maxRetries;
        this.initialIntervalMillis = initialIntervalMillis;
    }

    @Override
    public Execution newExecution() {
        return new ExponentialBackOffExecution();
    }

    private final class ExponentialBackOffExecution implements Execution {
        private int retries;
        private long sleepInterval = initialIntervalMillis;

        @Override
        public void waitOrRethrow(MarketoApiException exception) throws MarketoApiException {
            retries++;

            if (retries <= maxRetries) {
                try {
                    Thread.sleep(sleepInterval);
                } catch (InterruptedException e) {
                    throw exception;
                }

                sleepInterval *= 1.5;
            } else {
                throw exception;
            }
        }
    }
}
