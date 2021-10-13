package org.jurasciix.vkapi.longpoll;

public class LongPollServerException extends Exception {

    private final int failed;

    public LongPollServerException(int failed) {
        this.failed = failed;
    }

    public int getFailed() {
        return failed;
    }

    @Override
    public String getMessage() {
        return "Long Poll server returned failed - " + failed;
    }
}
