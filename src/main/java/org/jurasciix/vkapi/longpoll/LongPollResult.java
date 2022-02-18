package org.jurasciix.vkapi.longpoll;

import com.google.gson.JsonArray;
import com.google.gson.annotations.SerializedName;

import java.util.Objects;

public class LongPollResult {

    public static final String JSON_FAILED = "failed";

    public static final String JSON_MIN_VERSION = "min_version";

    public static final String JSON_MAX_VERSION = "max_version";

    public static final String JSON_TIMESTAMP = "ts";

    public static final String JSON_PERSISTENT_TIMESTAMP = "pts";

    public static final String JSON_UPDATES = "updates";

    @SerializedName(JSON_FAILED)
    private Integer failed;

    @SerializedName(JSON_MIN_VERSION)
    private Integer minVersion;

    @SerializedName(JSON_MAX_VERSION)
    private Integer maxVersion;

    @SerializedName(JSON_TIMESTAMP)
    private Long timestamp;

    @SerializedName(JSON_PERSISTENT_TIMESTAMP)
    private Long persistentTimestamp;

    @SerializedName(JSON_UPDATES)
    private JsonArray updates;

    public final boolean isFailed() {
        return failed != null;
    }

    public final Integer getFailed() {
        return failed;
    }

    public final Integer getMinVersion() {
        return minVersion;
    }

    public final Integer getMaxVersion() {
        return maxVersion;
    }

    public final Long getTimestamp() {
        return timestamp;
    }

    public final boolean hasPersistentTimestamp() {
        return persistentTimestamp != null;
    }

    public final Long getPersistentTimestamp() {
        return persistentTimestamp;
    }

    public final JsonArray getUpdates() {
        return updates;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        LongPollResult r = (LongPollResult) o;
        return (Objects.equals(failed, r.failed) &&
                Objects.equals(minVersion, r.minVersion) &&
                Objects.equals(maxVersion, r.maxVersion) &&
                Objects.equals(timestamp, r.timestamp) &&
                Objects.equals(persistentTimestamp, r.persistentTimestamp) &&
                Objects.equals(updates, r.updates));
    }

    @Override
    public int hashCode() {
        return (((((Objects.hashCode(failed)) * 31 +
                Objects.hashCode(minVersion)) * 31 +
                Objects.hashCode(maxVersion)) * 31 +
                Objects.hashCode(timestamp)) * 31 +
                Objects.hashCode(persistentTimestamp)) * 31 +
                Objects.hashCode(updates);
    }

    @Override
    public String toString() {
        return getClass().getName() + '(' +
                "failed=" + failed + ',' +
                "minVersion=" + minVersion + ',' +
                "maxVersion=" + maxVersion + ',' +
                "timestamp=" + timestamp + ',' +
                "persistentTimestamp=" + persistentTimestamp + ',' +
                "updates=" + updates + ',' +
                ')';
    }
}
