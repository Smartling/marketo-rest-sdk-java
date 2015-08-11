package com.smartling.marketo.sdk.rest.transport;

import com.smartling.marketo.sdk.MarketoApiException;

public interface TokenProvider {
    Token authenticate(ClientConnectionData clientConnectionData) throws MarketoApiException;
}
