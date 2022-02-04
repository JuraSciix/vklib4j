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

package org.jurasciix.vkapi.util;

import com.google.gson.JsonElement;

import java.io.Reader;
import java.lang.reflect.Type;

public interface JsonManager {

    JsonElement parseJson(String json);

    JsonElement parseJson(Reader json);

    <T> T fromJson(String json, Type type);

    <T> T fromJson(String json, Class<T> clazz);

    <T> T fromJson(Reader json, Type type);

    <T> T fromJson(Reader json, Class<T> clazz);

    <T> T fromJson(JsonElement json, Type type);

    <T> T fromJson(JsonElement json, Class<T> clazz);

    String toJson(Object obj);
}
