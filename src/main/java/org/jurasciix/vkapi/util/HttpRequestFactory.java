package org.jurasciix.vkapi.util;

import org.apache.http.client.HttpClient;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClientBuilder;

import java.net.URI;

public class HttpRequestFactory implements Request.Factory {

    private static HttpRequestFactory instance;

    public static HttpRequestFactory getInstance() {
        if (instance != null) {
            return instance;
        }
        instance = new HttpRequestFactory();
        return instance;
    }

    private static HttpClient createHttpClient() {
        return HttpClientBuilder.create()
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setCookieSpec(CookieSpecs.STANDARD)
                        .build())
                .build();
    }

    private final HttpClient httpClient;

    protected HttpRequestFactory() {
        this(createHttpClient());
    }

    protected HttpRequestFactory(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    protected HttpClient getHttpClient() {
        return httpClient;
    }

    @Override
    public Request newRequest() {
        return new HttpRequest(new URIBuilder(), httpClient);
    }

    @Override
    public Request newRequest(String uri) {
        return new HttpRequest(new URIBuilder(URI.create(uri)), httpClient);
    }

    @Override
    public Request newRequest(URI uri) {
        return new HttpRequest(new URIBuilder(uri), httpClient);
    }
}
