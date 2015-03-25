package com.smartling.marketo.sdk.rest.transport;


import java.util.Collections;
import java.util.List;

class MarketoResponse<T> {
    private boolean success;
    private List<Error> errors = Collections.emptyList();
    private T result;

    public boolean isSuccess() {
        return success;
    }

    public List<Error> getErrors() {
        return errors;
    }

    public T getResult() {
        return result;
    }

    static class Error {
        private String code;
        private String message;

        public String getCode() {
            return code;
        }

        public String getMessage() {
            return message;
        }
    }
}
