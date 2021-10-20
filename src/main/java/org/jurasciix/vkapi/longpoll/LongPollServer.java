package org.jurasciix.vkapi.longpoll;

import com.google.gson.JsonElement;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.jurasciix.vkapi.util.LombokToStringStyle;
import org.jurasciix.vkapi.util.Request;

public class LongPollServer {

    protected static final String HTTP_SCHEME = "https";

    protected static final String HTTP_PARAM_ACT = "act";
    protected static final String HTTP_PARAM_KEY = "key";
    protected static final String HTTP_PARAM_TIMESTAMP = "ts";

    protected static final String ACT = "a_check";

    protected static final String JSON_SERVER = "server";
    protected static final String JSON_KEY = "key";
    protected static final String JSON_TIMESTAMP = "ts";

    @SerializedName(JSON_SERVER)
    private String server;

    @SerializedName(JSON_KEY)
    private String key;

    @SerializedName(JSON_TIMESTAMP)
    private String timestamp;

    public String getServer() {
        return server;
    }

    public String getKey() {
        return key;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public Request getRequest(Request.Factory requestFactory) {
        Request request;
        String s = server;
        if (!s.contains("://")) {
            request = requestFactory.newRequest();
            request.setScheme(HTTP_SCHEME);
            request.setHost(s);
        } else {
            request = requestFactory.newRequest(s);
        }
        request.addParameter(HTTP_PARAM_ACT, ACT);
        request.addParameter(HTTP_PARAM_KEY, key);
        request.addParameter(HTTP_PARAM_TIMESTAMP, timestamp);
        return request;
    }

    public void onResult(LongPolling longPolling, LongPollResult result) throws LongPollServerException {
        if (result.hasFailed()) {
            if (result.getFailed() == LongPolling.FAILED_INCORRECT_TS) {
                timestamp = result.getTimestamp();
                return;
            }
            throw new LongPollServerException(result.getFailed());
        }
        timestamp = result.getTimestamp();

        for (JsonElement update : result.getUpdates()) {
            longPolling.onUpdate(update);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LongPollServer)) return false;
        LongPollServer another = (LongPollServer) o;
        EqualsBuilder builder = new EqualsBuilder();
        builder.append(server, another.server);
        builder.append(key, another.key);
        builder.append(timestamp, another.timestamp);
        return builder.isEquals();
    }

    @Override
    public int hashCode() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(server);
        builder.append(key);
        builder.append(timestamp);
        return builder.toHashCode();
    }

    @Override
    public String toString() {
        ToStringBuilder builder = new ToStringBuilder(this, LombokToStringStyle.STYLE);
        builder.append("server", server);
        builder.append("key", key);
        builder.append("timestamp", timestamp);
        return builder.toString();
    }
}
