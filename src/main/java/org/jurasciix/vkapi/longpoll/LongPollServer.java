/*
 * Copyright 2022-2022, JuraSciix.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jurasciix.vkapi.longpoll;

import com.google.gson.JsonElement;
import com.google.gson.annotations.SerializedName;
import org.jurasciix.vkapi.util.Request;
import org.jurasciix.vkapi.util.RequestFactory;

import java.net.URI;
import java.util.Objects;

public class LongPollServer {

    public static final String JSON_SERVER = "server";

    public static final String JSON_KEY = "key";

    public static final String JSON_TIMESTAMP = "ts";

    public static final String JSON_PERSISTENT_TIMESTAMP = "pts";

    @SerializedName(JSON_SERVER)
    private URI server;

    @SerializedName(JSON_KEY)
    private String key;

    @SerializedName(JSON_TIMESTAMP)
    private Long timestamp;

    @SerializedName(JSON_PERSISTENT_TIMESTAMP)
    private Long persistentTimestamp;

    public final URI getServer() {
        return server;
    }

    public final String getKey() {
        return key;
    }

    public final Long getTimestamp() {
        return timestamp;
    }

    public final Long getPersistentTimestamp() {
        return persistentTimestamp;
    }

    public Request createRequest(RequestFactory requestFactory) {
        if (server == null)
            throw new IllegalStateException("server is null");
        if (key == null)
            throw new IllegalStateException("key is null");
        if (timestamp == null)
            throw new IllegalStateException("timestamp is null");
        // todo
        Request request = server.getHost() == null ?
                requestFactory.create().setHost(server.getPath()).setScheme("https") :
                requestFactory.create(server);
        request.addParameter("act", "a_check");
        request.addParameter("key", key);
        request.addParameter("ts", timestamp.toString());
        return request;
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
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        LongPollServer s = (LongPollServer) o;
        return (Objects.equals(server, s.server) &&
                Objects.equals(key, s.key) &&
                Objects.equals(timestamp, s.timestamp) &&
                Objects.equals(persistentTimestamp, s.persistentTimestamp));
    }

    @Override
    public int hashCode() {
        return (((Objects.hashCode(server)) * 31 +
                Objects.hashCode(key)) * 31 +
                Objects.hashCode(timestamp)) * 31 +
                Objects.hashCode(persistentTimestamp);
    }

    @Override
    public String toString() {
        return getClass().getName() + '(' +
                "server" + server + ',' +
                "key" + key + ',' +
                "timestamp" + timestamp + ',' +
                "persistentTimestamp" + persistentTimestamp + ',' +
                ')';
    }
}
