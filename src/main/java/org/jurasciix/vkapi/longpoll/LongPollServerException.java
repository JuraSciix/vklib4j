package org.jurasciix.vkapi.longpoll;

public class LongPollServerException extends Exception {

    private final int failed;

    private final Integer minVersion;

    private final Integer maxVersion;

    public LongPollServerException(int failed, Integer minVersion, Integer maxVersion) {
        this.failed = failed;
        this.minVersion = minVersion;
        this.maxVersion = maxVersion;
    }

    public int getFailed() {
        return failed;
    }

    public Integer getMinVersion() {
        return minVersion;
    }

    public Integer getMaxVersion() {
        return maxVersion;
    }

    @Override
    public String getMessage() {
        StringBuilder builder = new StringBuilder();
        builder.append("Long Poll server returns the failed - ");
        builder.append(failed);
        if (minVersion != null && maxVersion != null) {
            builder.append(" (");
            builder.append("minVersion: ");
            builder.append(minVersion);
            builder.append(", maxVersion: ");
            builder.append(maxVersion);
            builder.append(")");
        }
        return builder.toString();
    }
}
