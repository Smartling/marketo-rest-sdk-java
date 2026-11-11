package com.smartling.marketo.sdk.rest;

import com.smartling.marketo.sdk.MarketoApiException;

/**
 * Marketo answers 60019 ("contentWaveAssetUpdationFailed") when the content of an email update is rejected,
 * for example because of damaged template markers, and also when a published (non-draft) email is updated
 * directly. It is not retried.
 */
public class EmailContentRejectedException extends MarketoApiException {
    public EmailContentRejectedException(String errorDescription) {
        super(errorDescription);
    }

    public EmailContentRejectedException(String errorCode, String errorDescription) {
        super(errorCode, errorDescription);
    }
}
