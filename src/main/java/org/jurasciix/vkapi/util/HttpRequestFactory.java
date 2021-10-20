package org.jurasciix.vkapi.util;

import org.apache.http.client.HttpClient;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClientBuilder;

import java.net.URI;

public class HttpRequestFactory implements Request.Factory {

    private static volatile HttpRequestFactory instance;

    public static HttpRequestFactory getInstance() {
        HttpRequestFactory value = instance;
        if (value == null) {
            ensureInstance();
            value = instance;
        }
        return value;
    }

    private static synchronized void ensureInstance() {
        HttpRequestFactory value = instance;
        if (value == null) {
            value = new HttpRequestFactory();
            instance = value;
        }
    }

    private static HttpClient createHttpClient() {
        HttpClientBuilder builder = HttpClientBuilder.create();
        builder.setDefaultRequestConfig(RequestConfig.custom().setCookieSpec(CookieSpecs.STANDARD).build());
        return builder.build();
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
