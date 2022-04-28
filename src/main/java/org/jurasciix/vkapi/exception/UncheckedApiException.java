package org.jurasciix.vkapi.exception;

import org.jurasciix.vkapi.model.Error;

@Deprecated
public class UncheckedApiException extends RuntimeException {

    private final Error error;

    public UncheckedApiException(Error error) {
        this.error = error;
    }

    public Error getError() {
        return error;
    }

    public int getErrorCode() {
        return error.getCode();
    }

    public String getErrorMessage() {
        return error.getMessage();
    }

    @Override
    public String getMessage() {
        return getErrorCode() + ": \"" + getErrorMessage() + "\"";
    }
}
