package com.smartling.marketo.sdk.rest.retry;


import com.smartling.marketo.sdk.rest.RequestLimitExceededException;
import com.smartling.marketo.sdk.rest.RetryPolicy;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.Stopwatch;

import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.sameInstance;

public class ExponentialBackOffRetryPolicyTest {

    @Rule
    public final Stopwatch stopwatch = new Stopwatch() {};
    @Rule
    public final ExpectedException expectedException = ExpectedException.none();

    @Test(expected = IllegalArgumentException.class)
    public void shouldValidateNumberOfRetries() throws Exception {
        new ExponentialBackOffRetryPolicy(0, 1000L);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldValidateInitialInterval() throws Exception {
        new ExponentialBackOffRetryPolicy(1, 0L);
    }

    @Test
    public void shouldReturnExecution() throws Exception {
        ExponentialBackOffRetryPolicy retryPolicy = new ExponentialBackOffRetryPolicy(1, 1000L);

        RetryPolicy.Execution execution = retryPolicy.newExecution();

        assertThat(execution).isNotNull();
    }

    @Test
    public void shouldRethrowRequestLimitExceptionAfterMaxAttempts() throws Exception {
        RetryPolicy.Execution execution = new ExponentialBackOffRetryPolicy(1, 1000L).newExecution();
        RequestLimitExceededException secondException = new RequestLimitExceededException("001", "Error");

        expectedException.expect(sameInstance(secondException));

        execution.waitOrRethrow(new RequestLimitExceededException("001", "Error"));
        execution.waitOrRethrow(secondException);
    }

    @Test
    public void shouldSleepAfterException() throws Exception {
        RetryPolicy.Execution execution = new ExponentialBackOffRetryPolicy(10, 500L).newExecution();

        execution.waitOrRethrow(new RequestLimitExceededException("001", "Error"));
        execution.waitOrRethrow(new RequestLimitExceededException("001", "Error"));
        execution.waitOrRethrow(new RequestLimitExceededException("001", "Error"));

        assertThat(stopwatch.runtime(TimeUnit.MILLISECONDS)).isGreaterThanOrEqualTo(500 + 750 + 1125);
    }

    @Test
    public void shouldRethrowExceptionIfInterrupted() throws Exception {
        RequestLimitExceededException exception = new RequestLimitExceededException("001", "Error");
        RetryPolicy.Execution execution = new ExponentialBackOffRetryPolicy(10, 500L).newExecution();
        Thread.currentThread().interrupt();

        expectedException.expect(sameInstance(exception));

        execution.waitOrRethrow(exception);
    }
}