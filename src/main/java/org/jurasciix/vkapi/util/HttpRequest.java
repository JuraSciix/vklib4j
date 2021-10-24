package org.jurasciix.vkapi.util;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.FileEntity;
import org.apache.http.entity.StringEntity;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class HttpRequest implements Request {

    private static final Object PRESENT = new Object();

    private final HttpClient httpClient;

    private final RequestBuilder requestBuilder;

    private final URIBuilder uriBuilder;

    // I don't want to create a HashSet and wrap it into synchronizedSet, I think it's pretty inefficient.
    private final Map<HttpUriRequest, Object> activeRequests = new ConcurrentHashMap<>();

    protected HttpRequest(HttpClient httpClient, RequestBuilder requestBuilder, URIBuilder uriBuilder) {
        this.httpClient = httpClient;
        this.requestBuilder = requestBuilder;
        this.uriBuilder = uriBuilder;
    }

    protected HttpClient getHttpClient() {
        return httpClient;
    }

    protected RequestBuilder getRequestBuilder() {
        return requestBuilder;
    }

    protected URIBuilder getUriBuilder() {
        return uriBuilder;
    }

    @Override
    public String getMethod() {
        return requestBuilder.getMethod();
    }

    @Override
    public Request setCharset(Charset charset) {
        uriBuilder.setCharset(charset);
        return this;
    }

    @Override
    public Charset getCharset() {
        return uriBuilder.getCharset();
    }

    @Override
    public Request setScheme(String scheme) {
        uriBuilder.setScheme(scheme);
        return this;
    }

    @Override
    public String getScheme() {
        return uriBuilder.getScheme();
    }

    @Override
    public Request setUserInfo(String userInfo) {
        uriBuilder.setUserInfo(userInfo);
        return this;
    }

    @Override
    public String getUserInfo() {
        return uriBuilder.getUserInfo();
    }

    @Override
    public Request setHost(String host) {
        uriBuilder.setHost(host);
        return this;
    }

    @Override
    public String getHost() {
        return uriBuilder.getHost();
    }

    @Override
    public Request setPort(int port) {
        uriBuilder.setPort(port);
        return this;
    }

    @Override
    public int getPort() {
        return uriBuilder.getPort();
    }

    @Override
    public Request setPath(String path) {
        uriBuilder.setPath(path);
        return this;
    }

    @Override
    public Request setPathSegments(String... pathSegments) {
        uriBuilder.setPathSegments(pathSegments);
        return this;
    }

    @Override
    public Request setPathSegments(List<String> pathSegments) {
        uriBuilder.setPathSegments(pathSegments);
        return this;
    }

    @Override
    public String getPath() {
        return uriBuilder.getPath();
    }

    @Override
    public Request addParameter(String name, String value) {
        uriBuilder.addParameter(name, value);
        return this;
    }

    @Override
    public Request setParameter(String name, String value) {
        uriBuilder.setParameter(name, value);
        return this;
    }

    @Override
    public Request addParameters(Map<String, String> parameters) {
        putParameters(parameters);
        return this;
    }

    @Override
    public Request setParameters(Map<String, String> parameters) {
        uriBuilder.clearParameters();
        putParameters(parameters);
        return this;
    }

    private void putParameters(Map<String, String> parameters) {
        if ((parameters == null) || parameters.isEmpty()) {
            return;
        }
        for (Map.Entry<String, String> parameterEntry : parameters.entrySet()) {
            uriBuilder.addParameter(parameterEntry.getKey(), parameterEntry.getValue());
        }
    }

    @Override
    public Map<String, String> getParameters() {
        List<NameValuePair> queryParams = uriBuilder.getQueryParams();

        if (queryParams.isEmpty()) {
            return Collections.emptyMap();
        }
        Map<String, String> parameters = new LinkedHashMap<>(queryParams.size());

        for (NameValuePair queryParam : queryParams) {
            parameters.put(queryParam.getName(), queryParam.getValue());
        }
        return parameters;
    }

    @Override
    public Request setFragment(String fragment) {
        uriBuilder.setFragment(fragment);
        return this;
    }

    @Override
    public String getFragment() {
        return uriBuilder.getFragment();
    }

    @Override
    public Request addHeader(String name, String value) {
        requestBuilder.addHeader(name, value);
        return this;
    }

    @Override
    public Request setHeader(String name, String value) {
        requestBuilder.setHeader(name, value);
        return this;
    }

    @Override
    public Request removeHeaders(String name) {
        requestBuilder.removeHeaders(name);
        return this;
    }

    @Override
    public Request setEntityString(String string) {
        requestBuilder.setEntity(new StringEntity(string, ContentType.DEFAULT_TEXT)); // damn checked exception
        return this;
    }

    @Override
    public Request setEntityString(String string, String mimeType) {
        requestBuilder.setEntity(new StringEntity(string, ContentType.create(mimeType)));
        return this;
    }

    @Override
    public Request setEntityString(String string, String mimeType, Charset charset) {
        requestBuilder.setEntity(new StringEntity(string, ContentType.create(mimeType, charset)));
        return this;
    }

    @Override
    public Request setEntityFile(File file) {
        requestBuilder.setEntity(new FileEntity(file));
        return this;
    }

    @Override
    public Request setEntityFile(File file, String mimeType) {
        requestBuilder.setEntity(new FileEntity(file, ContentType.create(mimeType)));
        return this;
    }

    @Override
    public Request setEntityFile(File file, String mimeType, Charset charset) {
        requestBuilder.setEntity(new FileEntity(file, ContentType.create(mimeType, charset)));
        return this;
    }

    @Override
    public Response execute() throws IOException {
        RequestBuilder builder = requestBuilder;
        builder.setUri(toUri(uriBuilder));
        HttpUriRequest request = builder.build();
        HttpResponse response;
        try {
            activeRequests.put(request, PRESENT);
            response = httpClient.execute(request);
        } finally {
            activeRequests.remove(request);
        }
        int statusCode = response.getStatusLine().getStatusCode();
        String contentType = getContentType(response);
        String content;
        try (InputStream contentStream = response.getEntity().getContent()) {
            content = IOUtils.toString(contentStream);
        }
        return new BasicResponse(statusCode, contentType, content);
    }

    @Override
    public void abort() {
        for (HttpUriRequest request : activeRequests.keySet()) {
            request.abort();
        }
    }

    @Override
    public URI toUri() {
        return toUri(uriBuilder);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (null == o || getClass() != o.getClass()) return false;
        HttpRequest another = (HttpRequest) o;
        return new EqualsBuilder()
                .append(httpClient, another.httpClient)
                .append(requestBuilder, another.requestBuilder)
                .append(uriBuilder, another.uriBuilder).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(httpClient)
                .append(requestBuilder)
                .append(uriBuilder).toHashCode();
    }

    @Override
    public String toString() {
        return uriBuilder.toString();
    }

    private static URI toUri(URIBuilder builder) {
        try {
            return builder.build();
        } catch (URISyntaxException e) { // should never happen
            throw new IllegalStateException(e);
        }
    }

    private static String getContentType(HttpResponse response) {
        Header header = response.getEntity().getContentType();
        return (header == null) ? "" : header.getValue();
    }

    public static class BasicResponse implements Response {

        private final int statusCode;

        private final String contentType;

        private final String content;

        protected BasicResponse(int statusCode, String contentType, String content) {
            this.statusCode = statusCode;
            this.contentType = contentType;
            this.content = content;
        }

        @Override
        public int getStatusCode() {
            return statusCode;
        }

        @Override
        public String getContentType() {
            return contentType;
        }

        @Override
        public String getContent() {
            return content;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (null == o || getClass() != o.getClass()) return false;
            BasicResponse another = (BasicResponse) o;
            return new EqualsBuilder()
                    .append(statusCode, another.statusCode)
                    .append(contentType, another.contentType)
                    .append(content, another.content).isEquals();
        }

        @Override
        public int hashCode() {
            return new HashCodeBuilder()
                    .append(statusCode)
                    .append(contentType)
                    .append(content).toHashCode();
        }

        @Override
        public String toString() {
            return LombokToStringStyle.getToStringBuilder(this)
                    .append("statusCode", statusCode)
                    .append("contentType", contentType)
                    .append("content", content).toString();
        }
    }
}
