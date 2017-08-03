package com.smartling.marketo.sdk.rest;

import com.smartling.marketo.sdk.MarketoApiException;

public class RequestLimitExceededException extends MarketoApiException {
    public RequestLimitExceededException(String errorCode, String errorDescription) {
        super(errorCode, errorDescription);
    }
}
