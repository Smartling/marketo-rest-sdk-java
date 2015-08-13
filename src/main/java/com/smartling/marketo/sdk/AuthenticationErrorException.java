package com.smartling.marketo.sdk;

public class AuthenticationErrorException extends MarketoApiException {
    public AuthenticationErrorException(String errorCode, String errorDescription) {
        super(errorCode, errorDescription);
    }
}
