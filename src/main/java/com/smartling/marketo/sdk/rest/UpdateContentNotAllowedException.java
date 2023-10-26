package com.smartling.marketo.sdk.rest;

import com.smartling.marketo.sdk.MarketoApiException;

public class UpdateContentNotAllowedException extends MarketoApiException {
    public UpdateContentNotAllowedException(String errorDescription) {
        super(errorDescription);
    }

    public UpdateContentNotAllowedException(String errorCode, String errorDescription) {
        super(errorCode, errorDescription);
    }
}
