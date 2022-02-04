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
        builder.append("Long Poll server returned a failed - ");
        builder.append(failed);
        if (minVersion != null && maxVersion != null) {
            builder.append(" (");
            builder.append("min version: ");
            builder.append(minVersion);
            builder.append(", max version: ");
            builder.append(maxVersion);
            builder.append(")");
        }
        return builder.toString();
    }
}
