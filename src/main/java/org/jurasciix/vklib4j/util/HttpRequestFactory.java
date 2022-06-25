package org.jurasciix.vklib4j.util;

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
