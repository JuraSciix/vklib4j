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

import org.apache.http.client.HttpClient;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClientBuilder;

import java.net.URI;

public class HttpRequestFactory implements RequestFactory {

    private static HttpRequestFactory instance = null;

    public static HttpRequestFactory getInstance() {
        HttpRequestFactory value = instance;
        if (value == null) {
            value = ensureInstance();
        }
        return value;
    }

    private static synchronized HttpRequestFactory ensureInstance() {
        HttpRequestFactory value = instance;
        if (value == null) {
            value = new HttpRequestFactory();
            instance = value;
        }
        return value;
    }

    private static HttpClient createDefaultHttpClient() {
        HttpClientBuilder builder = HttpClientBuilder.create();
        builder.setDefaultRequestConfig(RequestConfig.custom()
                .setCookieSpec(CookieSpecs.STANDARD).build());
        return builder.build();
    }

    private final HttpClient httpClient;

    protected HttpRequestFactory() {
        this(createDefaultHttpClient());
    }

    protected HttpRequestFactory(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    protected final HttpClient getHttpClient() {
        return httpClient;
    }

    @Override
    public Request create() {
        return new HttpRequest(httpClient, new URIBuilder());
    }

    @Override
    public Request create(URI uri) {
        return new HttpRequest(httpClient, new URIBuilder(uri));
    }

    @Override
    public Request create(String uri) {
        return new HttpRequest(httpClient, new URIBuilder(URI.create(uri)));
    }
}
