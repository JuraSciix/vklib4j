package org.jurasciix.jvkapi.util;

import org.apache.http.NameValuePair;

import java.net.URI;
import java.util.List;

public interface Request {

    interface Response {

        String getContent();
    }

    Request setScheme(String scheme);

    Request setUserInfo(String userInfo);

    Request setHost(String host);

    Request setPort(int port);

    Request setPath(String path);

    Request setPathSegments(String... pathSegments);

    Request setPathSegments(List<String> pathSegments);

    Request addParameter(String name, String value);

    Request setParameter(String name, String value);

    Request addParameters(List<NameValuePair> parameters);

    Request setParameters(List<NameValuePair> parameters);

    Request setFragment(String fragment);

    Response execute();

    URI toURI();
}
