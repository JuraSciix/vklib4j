package org.jurasciix.vkapi.util;

import java.net.URI;

public interface RequestFactory {

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
