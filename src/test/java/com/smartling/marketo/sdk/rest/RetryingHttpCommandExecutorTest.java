package com.smartling.marketo.sdk.rest;

import com.smartling.marketo.sdk.MarketoApiException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RetryingHttpCommandExecutorTest {
    private RetryingHttpCommandExecutor testedInstance;

    @Rule
    public final ExpectedException expectedException = ExpectedException.none();

    @Mock
    private HttpCommandExecutor internalExecutor;
    @Mock
    private RetryPolicy retryPolicy;
    @Mock
    private Command<Object> command;
    @Mock
    private RetryPolicy.Execution execution;

    @Before
    public void setUp() throws Exception {
        when(retryPolicy.newExecution()).thenReturn(execution);

        testedInstance = new RetryingHttpCommandExecutor(internalExecutor, retryPolicy);
    }

    @Test(expected = NullPointerException.class)
    public void shouldValidateExecutorForNull() throws Exception {
        new RetryingHttpCommandExecutor(null, retryPolicy);
    }

    @Test(expected = NullPointerException.class)
    public void shouldValidatePolicyForNull() throws Exception {
        new RetryingHttpCommandExecutor(internalExecutor, null);
    }

    @Test
    public void shouldDelegateToInternalExecutor() throws Exception {
        Object internalExecutorResult = new Object();
        given(internalExecutor.execute(command)).willReturn(internalExecutorResult);

        Object result = testedInstance.execute(command);

        assertThat(result).isSameAs(internalExecutorResult);
    }

    @Test
    public void shouldRetryWhileRequired() throws Exception {
        given(internalExecutor.execute(anyCommand()))
                .willThrow(new RequestLimitExceededException("", ""))
                .willThrow(new RequestLimitExceededException("", ""))
                .willReturn(new Object());

        testedInstance.execute(command);

        verify(internalExecutor, times(3)).execute(command);
    }

    @Test
    public void shouldDelegateFailuresToRetryPolicy() throws Exception {
        RequestLimitExceededException exception = new RequestLimitExceededException("", "");
        given(internalExecutor.execute(anyCommand())).willThrow(exception).willReturn(new Object());

        testedInstance.execute(command);

        verify(execution).waitOrRethrow(exception);
    }

    @Test
    public void shouldIgnoreOtherExceptions() throws Exception {
        MarketoApiException exception = new MarketoApiException("");
        given(internalExecutor.execute(anyCommand())).willThrow(exception).willReturn(new Object());

        expectedException.expect(sameInstance(exception));

        testedInstance.execute(command);
    }

    @SuppressWarnings("unchecked")
    private static Command<Object> anyCommand() {
        return any(Command.class);
    }
}