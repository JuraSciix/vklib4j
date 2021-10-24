package org.jurasciix.vkapi.util;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

public interface Request {

    interface Factory {

        Request newGet();

        Request newGet(String uri);

        Request newGet(URI uri);

        Request newPost();

        Request newPost(String uri);

        Request newPost(URI uri);

        Request newRequest(String method);

        Request newRequest(String method, String uri);

        Request newRequest(String method, URI uri);
    }

    interface Response {

        int getStatusCode();

        String getContentType();

        String getContent();
    }

    String getMethod();

    Request setCharset(Charset charset);

    Charset getCharset();

    Request setScheme(String scheme);

    String getScheme();

    Request setUserInfo(String userInfo);

    String getUserInfo();

    Request setHost(String host);

    String getHost();

    Request setPort(int port);

    int getPort();

    Request setPath(String path);

    Request setPathSegments(String... pathSegments);

    Request setPathSegments(List<String> pathSegments);

    String getPath();

    Request addParameter(String name, String value);

    Request setParameter(String name, String value);

    Request addParameters(Map<String, String> parameters);

    Request setParameters(Map<String, String> parameters);

    Map<String, String> getParameters();

    Request setFragment(String fragment);

    String getFragment();

    Request addHeader(String name, String value);

    Request setHeader(String name, String value);

    Request removeHeaders(String name);

    Request setEntityString(String string);

    Request setEntityString(String string, String mimeType);

    Request setEntityString(String string, String mimeType, Charset charset);

    Request setEntityFile(File file);

    Request setEntityFile(File file, String mimeType);

    Request setEntityFile(File file, String mimeType, Charset charset);

    Response execute() throws IOException;

    void abort();

    URI toUri();
}
