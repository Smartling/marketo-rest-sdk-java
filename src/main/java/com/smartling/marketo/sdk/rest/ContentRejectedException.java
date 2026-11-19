package com.smartling.marketo.sdk.rest;

import com.smartling.marketo.sdk.MarketoApiException;

/**
 * Marketo answers 60019 ("contentWaveAssetUpdationFailed") when the content of an email update is rejected,
 * for example because of damaged template markers, and also when a published (non-draft) email is updated
 * directly. For fragments the observed cause is the second clause: an update of the published id, with or
 * without a draft (damaged markers were accepted on a draft). It is not retried, and it is thrown for emails
 * and fragments alike.
 */
public class ContentRejectedException extends MarketoApiException {
    public ContentRejectedException(String errorDescription) {
        super(errorDescription);
    }

    public ContentRejectedException(String errorCode, String errorDescription) {
        super(errorCode, errorDescription);
    }
}
