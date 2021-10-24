package org.jurasciix.vkapi.longpoll;

import com.google.gson.JsonArray;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.jurasciix.vkapi.util.LombokToStringStyle;

public class LongPollResult {

    protected static final String JSON_FAILED = "failed";
    protected static final String JSON_TIMESTAMP = "ts";
    protected static final String JSON_PERSISTENT_TIMESTAMP = "pts";
    protected static final String JSON_UPDATES = "updates";

    @SerializedName(JSON_FAILED)
    private Integer failed;

    @SerializedName(JSON_TIMESTAMP)
    private String timestamp;

    @SerializedName(JSON_PERSISTENT_TIMESTAMP)
    private String persistentTimestamp;

    @SerializedName(JSON_UPDATES)
    private JsonArray updates;

    public boolean hasFailed() {
        return failed != null;
    }

    public Integer getFailed() {
        return failed;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getPersistentTimestamp() {
        return persistentTimestamp;
    }

    public JsonArray getUpdates() {
        return updates;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (null == o || getClass() != o.getClass()) return false;
        LongPollResult another = (LongPollResult) o;
        return new EqualsBuilder()
                .append(failed, another.failed)
                .append(timestamp, another.timestamp)
                .append(persistentTimestamp, another.persistentTimestamp)
                .append(updates, another.updates).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(failed)
                .append(timestamp)
                .append(persistentTimestamp)
                .append(updates).toHashCode();
    }

    @Override
    public String toString() {
        return LombokToStringStyle.getToStringBuilder(this)
                .append("failed", failed)
                .append("timestamp", timestamp)
                .append("persistentTimestamp", persistentTimestamp)
                .append("updates", updates).toString();
    }
}
