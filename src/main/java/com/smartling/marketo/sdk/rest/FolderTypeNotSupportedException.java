package com.smartling.marketo.sdk.rest;

import com.smartling.marketo.sdk.MarketoApiException;

public class FolderTypeNotSupportedException extends MarketoApiException {
    public FolderTypeNotSupportedException(String errorDescription) {
        super(errorDescription);
    }

    public FolderTypeNotSupportedException(String errorCode, String errorDescription) {
        super(errorCode, errorDescription);
    }
}
