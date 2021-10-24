package org.jurasciix.vkapi.util;

import org.apache.http.client.HttpClient;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClientBuilder;

import java.net.URI;
import java.net.URISyntaxException;

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
    public Request newGet() {
        return new HttpRequest(httpClient, RequestBuilder.get(), new URIBuilder());
    }

    @Override
    public Request newGet(String uri) {
        URIBuilder uriBuilder;
        try {
            uriBuilder = new URIBuilder(uri);
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException("uri", e);
        }
        return new HttpRequest(httpClient, RequestBuilder.get(), uriBuilder);
    }

    @Override
    public Request newGet(URI uri) {
        return new HttpRequest(httpClient, RequestBuilder.get(), new URIBuilder(uri));
    }

    @Override
    public Request newPost() {
        return new HttpRequest(httpClient, RequestBuilder.post(), new URIBuilder());
    }

    @Override
    public Request newPost(String uri) {
        URIBuilder uriBuilder;
        try {
            uriBuilder = new URIBuilder(uri);
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException("uri", e);
        }
        return new HttpRequest(httpClient, RequestBuilder.post(), uriBuilder);
    }

    @Override
    public Request newPost(URI uri) {
        return new HttpRequest(httpClient, RequestBuilder.post(), new URIBuilder(uri));
    }

    @Override
    public Request newRequest(String method) {
        return new HttpRequest(httpClient, RequestBuilder.create(method), new URIBuilder());
    }

    @Override
    public Request newRequest(String method, String uri) {
        URIBuilder uriBuilder;
        try {
            uriBuilder = new URIBuilder(uri);
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException("uri", e);
        }
        return new HttpRequest(httpClient, RequestBuilder.create(method), uriBuilder);
    }

    @Override
    public Request newRequest(String method, URI uri) {
        return new HttpRequest(httpClient, RequestBuilder.create(method), new URIBuilder(uri));
    }
}
