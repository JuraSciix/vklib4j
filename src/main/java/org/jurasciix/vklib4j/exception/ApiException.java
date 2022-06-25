package org.jurasciix.vklib4j.exception;

import org.jurasciix.vklib4j.api.model.common.Error;

public class ApiException extends Exception {

    private final Error error;

    public ApiException(Error error) {
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
