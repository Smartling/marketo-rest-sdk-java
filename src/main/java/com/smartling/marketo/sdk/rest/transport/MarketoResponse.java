package com.smartling.marketo.sdk.rest.transport;


import java.util.Collections;
import java.util.List;

final class MarketoResponse<T> {
    private boolean success;
    private List<Error> errors = Collections.emptyList();
    private T result;

    boolean isSuccess() {
        return success;
    }

    List<Error> getErrors() {
        return errors;
    }

    public T getResult() {
        return result;
    }

    static class Error {
        private String code;
        private String message;

        String getCode() {
            return code;
        }

        String getMessage() {
            return message;
        }
    }
}
