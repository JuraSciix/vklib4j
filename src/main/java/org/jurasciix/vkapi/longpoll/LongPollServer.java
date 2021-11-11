package org.jurasciix.vkapi.longpoll;

import com.google.gson.JsonElement;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.jurasciix.vkapi.util.LombokToStringStyle;
import org.jurasciix.vkapi.util.Request;
import org.jurasciix.vkapi.util.RequestFactory;

public class LongPollServer {

    protected static final String HTTP_SCHEME = "https";

    protected static final String HTTP_PARAM_ACT = "act";
    protected static final String HTTP_PARAM_KEY = "key";
    protected static final String HTTP_PARAM_TIMESTAMP = "ts";

    protected static final String ACT = "a_check";

    protected static final String JSON_SERVER = "server";
    protected static final String JSON_KEY = "key";
    protected static final String JSON_TIMESTAMP = "ts";
    protected static final String JSON_PERSISTENT_TIMESTAMP = "pts";

    @SerializedName(JSON_SERVER)
    private String server;

    @SerializedName(JSON_KEY)
    private String key;

    @SerializedName(JSON_TIMESTAMP)
    private String timestamp;

    @SerializedName(JSON_PERSISTENT_TIMESTAMP)
    private String persistentTimestamp;

    public String getServer() {
        return server;
    }

    public String getKey() {
        return key;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getPersistentTimestamp() {
        return persistentTimestamp;
    }

    public Request getRequest(RequestFactory requestFactory) {
        String s = server;
        return (s.contains("://")
                ? requestFactory.newGet(s)
                : requestFactory.newGet().setScheme(HTTP_SCHEME).setHost(s))
                .addParameter(HTTP_PARAM_ACT, ACT)
                .addParameter(HTTP_PARAM_KEY, key)
                .addParameter(HTTP_PARAM_TIMESTAMP, timestamp);
    }

    public void onResult(LongPolling longPolling, LongPollResult result) throws LongPollServerException {
        if (result.isFailed()) {
            if (result.getFailed() == LongPolling.FAILED_INCORRECT_TS) {
                timestamp = result.getTimestamp();
                return;
            }
            throw new LongPollServerException(result.getFailed(), result.getMinVersion(), result.getMaxVersion());
        }
        timestamp = result.getTimestamp();
        if (result.hasPersistentTimestamp()) {
            persistentTimestamp = result.getPersistentTimestamp();
        }

        for (JsonElement update : result.getUpdates()) {
            longPolling.onUpdate(update);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LongPollServer)) return false;
        LongPollServer another = (LongPollServer) o;
        return new EqualsBuilder()
                .append(server, another.server)
                .append(key, another.key)
                .append(timestamp, another.timestamp)
                .append(persistentTimestamp, another.persistentTimestamp).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(server)
                .append(key)
                .append(timestamp)
                .append(persistentTimestamp).toHashCode();
    }

    @Override
    public String toString() {
        return LombokToStringStyle.getToStringBuilder(this)
                .append("server", server)
                .append("key", key)
                .append("timestamp", timestamp)
                .append("persistentTimestamp", persistentTimestamp).toString();
    }
}
