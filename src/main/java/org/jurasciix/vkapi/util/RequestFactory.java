package org.jurasciix.vkapi.util;

import java.net.URI;

public interface RequestFactory {

    Request create();

    Request create(URI uri);

    Request create(String uri);
}
