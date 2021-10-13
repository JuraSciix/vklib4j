package org.jurasciix.vkapi.util;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class HttpRequest implements Request {

    private final URIBuilder builder;

    private final HttpClient httpClient;

    protected HttpRequest(URIBuilder builder, HttpClient httpClient) {
        this.builder = builder;
        this.httpClient = httpClient;
    }

    protected URIBuilder getBuilder() {
        return builder;
    }

    protected HttpClient getHttpClient() {
        return httpClient;
    }

    @Override
    public Request setCharset(Charset charset) {
        builder.setCharset(charset);
        return this;
    }

    @Override
    public Charset getCharset() {
        return builder.getCharset();
    }

    @Override
    public Request setScheme(String scheme) {
        builder.setScheme(scheme);
        return this;
    }

    @Override
    public String getScheme() {
        return builder.getScheme();
    }

    @Override
    public Request setUserInfo(String userInfo) {
        builder.setUserInfo(userInfo);
        return this;
    }

    @Override
    public String getUserInfo() {
        return builder.getUserInfo();
    }

    @Override
    public Request setHost(String host) {
        builder.setHost(host);
        return this;
    }

    @Override
    public String getHost() {
        return builder.getHost();
    }

    @Override
    public Request setPath(String path) {
        builder.setPath(path);
        return this;
    }

    @Override
    public String getPath() {
        return builder.getPath();
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
        builder.setParameter(name, value);
        return this;
    }

    @Override
    public Request addParameters(Map<String, String> parameters) {
        putParameters(parameters);
        return this;
    }

    @Override
    public Request setParameters(Map<String, String> parameters) {
        builder.clearParameters();
        putParameters(parameters);
        return this;
    }

    private void putParameters(Map<String, String> parameters) {
        if (parameters != null && !parameters.isEmpty()) {
            for (Map.Entry<String, String> entry: parameters.entrySet()) {
                builder.addParameter(entry.getKey(), entry.getValue());
            }
        }
    }

    @Override
    public Map<String, String> getParameters() {
        List<NameValuePair> params = builder.getQueryParams();
        if (params.isEmpty()) {
            return Collections.emptyMap();
        }
        Map<String, String> parameters = new LinkedHashMap<>(params.size());

        for (NameValuePair param: params) {
            parameters.put(param.getName(), param.getValue());
        }
        return parameters;
    }

    @Override
    public Request setFragment(String fragment) {
        builder.setFragment(fragment);
        return this;
    }

    @Override
    public String getFragment() {
        return builder.getFragment();
    }

    @Override
    public Response execute() throws IOException {
        URI uri;

        try {
            uri = builder.build();
        } catch (URISyntaxException e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
        HttpResponse resp = httpClient.execute(new HttpGet(uri));

        try (InputStream content = resp.getEntity().getContent()) {
            return new SimpleResponse(resp.getStatusLine().getStatusCode(), IOUtils.toString(content));
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof HttpRequest)) return false;
        HttpRequest another = (HttpRequest) o;
        EqualsBuilder builder = new EqualsBuilder();
        builder.append(builder, another.builder);
        builder.append(httpClient, another.httpClient);
        return builder.isEquals();
    }

    @Override
    public int hashCode() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(builder);
        builder.append(httpClient);
        return builder.toHashCode();
    }

    @Override
    public String toString() {
        return builder.toString();
    }

    protected static class SimpleResponse implements Response {

        private final int statusCode;

        private final String content;

        protected SimpleResponse(int statusCode, String content) {
            this.statusCode = statusCode;
            this.content = content;
        }

        @Override
        public int getStatusCode() {
            return statusCode;
        }

        @Override
        public String getContent() {
            return content;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof SimpleResponse)) return false;
            SimpleResponse another = (SimpleResponse) o;
            EqualsBuilder builder = new EqualsBuilder();
            builder.append(statusCode, another.statusCode);
            builder.append(content, another.content);
            return builder.isEquals();
        }

        @Override
        public int hashCode() {
            HashCodeBuilder builder = new HashCodeBuilder();
            builder.append(statusCode);
            builder.append(content);
            return builder.toHashCode();
        }

        @Override
        public String toString() {
            ToStringBuilder builder = new ToStringBuilder(this);
            builder.append("statusCode", statusCode);
            builder.append("content", content);
            return builder.toString();
        }
    }
}
