package com.smartling.marketo.sdk;

public class MarketoApiException extends Exception {
    private String errorCode;

    public MarketoApiException(String errorDescription) {
        super(errorDescription);
    }

    public MarketoApiException(String errorCode, String errorDescription) {
        this(errorDescription);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }
}
