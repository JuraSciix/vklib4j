package org.jurasciix.vkapi.longpoll;

import com.google.gson.JsonArray;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class LongPollResult {

    public static final String JSON_FAILED = "failed";
    public static final String JSON_TIMESTAMP = "ts";
    public static final String JSON_UPDATES = "updates";

    @SerializedName(JSON_FAILED)
    private Integer failed;

    @SerializedName(JSON_TIMESTAMP)
    private String timestamp;

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

    public JsonArray getUpdates() {
        return updates;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LongPollResult)) return false;
        LongPollResult another = (LongPollResult) o;
        EqualsBuilder builder = new EqualsBuilder();
        builder.append(failed, another.failed);
        builder.append(timestamp, another.timestamp);
        builder.append(updates, another.updates);
        return builder.isEquals();
    }

    @Override
    public int hashCode() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(failed);
        builder.append(timestamp);
        builder.append(updates);
        return builder.toHashCode();
    }

    @Override
    public String toString() {
        ToStringBuilder builder = new ToStringBuilder(this);
        builder.append("failed", failed);
        builder.append("timestamp", timestamp);
        builder.append("updates", updates);
        return builder.toString();
    }
}
