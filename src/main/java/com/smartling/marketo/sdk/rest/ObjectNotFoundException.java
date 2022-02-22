package com.smartling.marketo.sdk.rest;

import com.smartling.marketo.sdk.MarketoApiException;

public class ObjectNotFoundException extends MarketoApiException {
    public ObjectNotFoundException(String errorDescription) {
        super(errorDescription);
    }

    public ObjectNotFoundException(String errorCode, String errorDescription) {
        super(errorCode, errorDescription);
    }
}
