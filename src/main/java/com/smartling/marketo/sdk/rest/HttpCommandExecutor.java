package com.smartling.marketo.sdk.rest;

import com.smartling.marketo.sdk.MarketoApiException;

public interface HttpCommandExecutor {
    <T> T execute(Command<T> command) throws MarketoApiException;
}
