package org.jurasciix.jvkapi.util;

public interface RequestFactory {

    Request create();

    Request create(String uri);
}
