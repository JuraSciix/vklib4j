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

package org.jurasciix.vkapi;

public class CheckedApiException extends RuntimeException {

    private final Error error;

    public CheckedApiException(Error error) {
        this.error = error;
    }

    public Error getError() {
        return error;
    }

    public int getErrorCode() {
        return getError().getCode();
    }

    public String getErrorMessage() {
        return getError().getMessage();
    }

    @Override
    public String getMessage() {
        return String.format("[%d] %s", getErrorCode(), getErrorMessage());
    }
}
