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

import org.apache.http.NameValuePair;

import java.net.URI;
import java.util.List;

public interface Request {

    interface Response {

        String getContent();
    }

    Request setScheme(String scheme);

    Request setUserInfo(String userInfo);

    Request setHost(String host);

    Request setPort(int port);

    Request setPath(String path);

    Request setPathSegments(String... pathSegments);

    Request setPathSegments(List<String> pathSegments);

    Request addParameter(String name, String value);

    Request setParameter(String name, String value);

    Request addParameters(List<NameValuePair> parameters);

    Request setParameters(List<NameValuePair> parameters);

    Request setFragment(String fragment);

    Response execute();

    URI toURI();
}
