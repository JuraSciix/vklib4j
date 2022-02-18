package org.jurasciix.vkapi;

public class CheckedApiException extends RuntimeException {

    private final Error error;

    public CheckedApiException(Error error) {
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
