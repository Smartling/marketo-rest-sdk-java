package com.smartling.marketo.sdk.rest;


import com.smartling.marketo.sdk.MarketoApiException;

public interface RetryPolicy {
    RetryPolicy NONE = () -> (Execution) exception -> {
        throw exception;
    };

    Execution newExecution();

    interface Execution {
        void waitOrRethrow(MarketoApiException exception) throws MarketoApiException;
    }
}
