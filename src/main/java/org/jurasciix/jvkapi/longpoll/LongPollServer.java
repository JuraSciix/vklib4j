package org.jurasciix.jvkapi.longpoll;

import com.google.gson.JsonElement;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.jurasciix.jvkapi.util.LombokToStringStyle;
import org.jurasciix.jvkapi.util.Request;
import org.jurasciix.jvkapi.util.RequestFactory;

public class LongPollServer {

    private static final String ACT = "a_check";

    private static final String HTTP_SCHEME = "https";

    private static final String HTTP_PARAM_ACT = "act";
    private static final String HTTP_PARAM_KEY = "key";
    private static final String HTTP_PARAM_TIMESTAMP = "ts";

    private static final String JSON_SERVER = "server";
    private static final String JSON_KEY = "key";
    private static final String JSON_TIMESTAMP = "ts";
    private static final String JSON_PERSISTENT_TIMESTAMP = "pts";

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

    public Request createRequest(RequestFactory requestFactory) {
        Request request;
        if (server.contains("://")) {
            request = requestFactory.create(server);
        } else {
            request = requestFactory.create();
            request.setScheme(HTTP_SCHEME);
            request.setHost(server);
        }
        request.addParameter(HTTP_PARAM_ACT, ACT);
        request.addParameter(HTTP_PARAM_KEY, getKey());
        request.addParameter(HTTP_PARAM_TIMESTAMP, getTimestamp());
        return request;
    }

    public void onResult(LongPolling longPolling, LongPollResult result) throws LongPollServerException {
        if (result.isFailed()) {
            if (result.getFailed() == LongPolling.FAILED_INCORRECT_TS) {
                timestamp = result.getTimestamp();
                return;
            }
            throw new LongPollServerException(result.getFailed(),
                    result.getMinVersion(), result.getMaxVersion());
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
        if (o == null || getClass() != o.getClass()) return false;
        LongPollServer another = (LongPollServer) o;
        EqualsBuilder builder = new EqualsBuilder();
        builder.append(server, another.server);
        builder.append(key, another.key);
        builder.append(timestamp, another.timestamp);
        builder.append(persistentTimestamp, another.persistentTimestamp);
        return builder.isEquals();
    }

    @Override
    public int hashCode() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(server);
        builder.append(key);
        builder.append(timestamp);
        builder.append(persistentTimestamp);
        return builder.toHashCode();
    }

    @Override
    public String toString() {
        ToStringBuilder builder = LombokToStringStyle.getToStringBuilder(this);
        builder.append("server", server);
        builder.append("key", key);
        builder.append("timestamp", timestamp);
        builder.append("persistentTimestamp", persistentTimestamp);
        return builder.toString();
    }
}
