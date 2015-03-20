package com.smartling.marketo.sdk;

public class MarketoApiException extends Exception {
    public MarketoApiException(String errorDescription) {
        super(errorDescription);
    }
}
