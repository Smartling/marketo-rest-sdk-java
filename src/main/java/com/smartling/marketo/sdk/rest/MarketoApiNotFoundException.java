package com.smartling.marketo.sdk.rest;

import com.smartling.marketo.sdk.MarketoApiException;

public class MarketoApiNotFoundException extends MarketoApiException {
    public MarketoApiNotFoundException(String errorDescription) {
        super(errorDescription);
    }

    public MarketoApiNotFoundException(String errorCode, String errorDescription) {
        super(errorCode, errorDescription);
    }
}
