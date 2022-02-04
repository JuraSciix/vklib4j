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

import com.google.gson.JsonArray;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.jurasciix.vkapi.util.LombokToStringStyle;

public class LongPollResult {

    private static final String JSON_FAILED = "failed";
    private static final String JSON_MIN_VERSION = "min_version";
    private static final String JSON_MAX_VERSION = "max_version";
    private static final String JSON_TIMESTAMP = "ts";
    private static final String JSON_PERSISTENT_TIMESTAMP = "pts";
    private static final String JSON_UPDATES = "updates";

    @SerializedName(JSON_FAILED)
    private Integer failed;

    @SerializedName(JSON_MIN_VERSION)
    private Integer minVersion;

    @SerializedName(JSON_MAX_VERSION)
    private Integer maxVersion;

    @SerializedName(JSON_TIMESTAMP)
    private String timestamp;

    @SerializedName(JSON_PERSISTENT_TIMESTAMP)
    private String persistentTimestamp;

    @SerializedName(JSON_UPDATES)
    private JsonArray updates;

    public boolean isFailed() {
        return failed != null;
    }

    public Integer getFailed() {
        return failed;
    }

    public Integer getMinVersion() {
        return minVersion;
    }

    public Integer getMaxVersion() {
        return maxVersion;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public boolean hasPersistentTimestamp() {
        return persistentTimestamp != null;
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
        if (o == null || getClass() != o.getClass()) return false;
        LongPollResult another = (LongPollResult) o;
        EqualsBuilder builder = new EqualsBuilder();
        builder.append(failed, another.failed);
        builder.append(minVersion, another.minVersion);
        builder.append(maxVersion, another.maxVersion);
        builder.append(timestamp, another.timestamp);
        builder.append(persistentTimestamp, another.persistentTimestamp);
        builder.append(updates, another.updates);
        return builder.isEquals();
    }

    @Override
    public int hashCode() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(failed);
        builder.append(minVersion);
        builder.append(maxVersion);
        builder.append(timestamp);
        builder.append(persistentTimestamp);
        builder.append(updates);
        return builder.toHashCode();
    }

    @Override
    public String toString() {
        ToStringBuilder builder = LombokToStringStyle.getToStringBuilder(this);
        builder.append("failed", failed);
        builder.append("minVersion", minVersion);
        builder.append("maxVersion", maxVersion);
        builder.append("timestamp", timestamp);
        builder.append("persistentTimestamp", persistentTimestamp);
        builder.append("updates", updates);
        return builder.toString();
    }
}
