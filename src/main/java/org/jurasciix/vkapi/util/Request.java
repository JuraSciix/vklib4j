package org.jurasciix.vkapi.util;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

public interface Request {

    interface Factory {

        Request newRequest();

        Request newRequest(String uri);

        Request newRequest(URI uri);
    }

    interface Response {

        int getStatusCode();

        String getContent();
    }

    Request setCharset(Charset charset);

    Charset getCharset();

    Request setScheme(String scheme);

    String getScheme();

    Request setUserInfo(String userInfo);

    String getUserInfo();

    Request setHost(String host);

    String getHost();

    Request setPath(String path);

    String getPath();

    Request setPathSegments(String... pathSegments);

    Request setPathSegments(List<String> pathSegments);

    Request addParameter(String name, String value);

    Request setParameter(String name, String value);

    Request addParameters(Map<String, String> parameters);

    Request setParameters(Map<String, String> parameters);

    Map<String, String> getParameters();

    Request setFragment(String fragment);

    String getFragment();

    Response execute() throws IOException;
}
