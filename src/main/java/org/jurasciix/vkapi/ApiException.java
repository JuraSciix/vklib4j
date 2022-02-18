package org.jurasciix.vkapi;

public class ApiException extends Exception {

    private final Error error;

    public ApiException(Error error) {
        this.error = error;
    }

    public Error getError() {
        return error;
    }

    public int getErrorCode() {
        return getError().getCode();
    }

    public String getErrorMessage() {
        return getError().getMessage();
    }

    @Override
    public String getMessage() {
        return String.format("[%d] %s", getErrorCode(), getErrorMessage());
    }
}
