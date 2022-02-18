package org.jurasciix.vkapi.util;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URIBuilder;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;

public class HttpRequest implements Request {

    private final HttpClient httpClient;

    private final URIBuilder builder;

    protected HttpRequest(HttpClient httpClient, URIBuilder builder) {
        this.httpClient = Objects.requireNonNull(httpClient, "http client");
        this.builder = Objects.requireNonNull(builder, "uri builder");
    }

    protected final HttpClient getHttpClient() {
        return httpClient;
    }

    protected final URIBuilder getBuilder() {
        return builder;
    }

    @Override
    public Request setScheme(String scheme) {
        builder.setScheme(scheme);
        return this;
    }

    @Override
    public Request setUserInfo(String userInfo) {
        builder.setUserInfo(userInfo);
        return this;
    }

    @Override
    public Request setHost(String host) {
        builder.setHost(host);
        return this;
    }

    @Override
    public Request setPort(int port) {
        builder.setPort(port);
        return this;
    }

    @Override
    public Request setPath(String path) {
        builder.setPath(path);
        return this;
    }

    @Override
    public Request setPathSegments(String... pathSegments) {
        builder.setPathSegments(pathSegments);
        return this;
    }

    @Override
    public Request setPathSegments(List<String> pathSegments) {
        builder.setPathSegments(pathSegments);
        return this;
    }

    @Override
    public Request addParameter(String name, String value) {
        builder.addParameter(name, value);
        return this;
    }

    @Override
    public Request setParameter(String name, String value) {
        builder.addParameter(name, value);
        return this;
    }

    @Override
    public Request addParameters(List<NameValuePair> parameters) {
        builder.addParameters(parameters);
        return this;
    }

    @Override
    public Request setParameters(List<NameValuePair> parameters) {
        builder.setParameters(parameters);
        return this;
    }

    @Override
    public Request setFragment(String fragment) {
        builder.setFragment(fragment);
        return this;
    }

    @Override
    public Response execute() {
        String content;

        try {
            content = executeInternal();
        } catch (IOException e) {
            throw new RequestException("I/O troubles", e);
        }
        return new BasicResponse(content);
    }

    protected String executeInternal() throws IOException {
        HttpRequestBase request = new HttpGet(toURI());
        HttpResponse response = httpClient.execute(request);

        int statusCode = response.getStatusLine().getStatusCode();
        if (statusCode != HttpStatus.SC_OK) {
            throw new RequestException("server returned a wrong status code - " + statusCode);
        }
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        response.getEntity().writeTo(stream);
        return stream.toString();
    }

    @Override
    public URI toURI() {
        try {
            return builder.build();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        HttpRequest r = (HttpRequest) o;
        // httpClient and builder should not be null
        return httpClient.equals(r.httpClient) && builder.equals(r.builder);
    }

    @Override
    public int hashCode() {
        // httpClient and builder should not be null
        return httpClient.hashCode() * 31 + builder.hashCode();
    }

    @Override
    public String toString() {
        return builder.toString();
    }

    protected static class BasicResponse implements Response {

        private final String content;

        protected BasicResponse(String content) {
            this.content = content;
        }

        @Override
        public String getContent() {
            return content;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o)
                return true;
            if (o == null || getClass() != o.getClass())
                return false;
            // content should not be null
            return content.equals(((BasicResponse) o).content);
        }

        @Override
        public int hashCode() {
            // content should not be null
            return content.hashCode();
        }

        @Override
        public String toString() {
            return getContent();
        }
    }
}
