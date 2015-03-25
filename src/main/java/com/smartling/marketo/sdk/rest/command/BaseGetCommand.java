package com.smartling.marketo.sdk.rest.command;


public abstract class BaseGetCommand<T> extends BaseMarketoCommand<T> {
    BaseGetCommand(Class<T> type) {
        super(type);
    }

    @Override
    public String getMethod() {
        return "GET";
    }
}
