package org.jurasciix.vklib4j.util;

import com.google.gson.JsonElement;

import java.io.Reader;
import java.lang.reflect.Type;

public interface JsonManager {

    JsonElement parseJson(String json);

    JsonElement parseJson(Reader json);

    <T> T fromJson(String json, Type type);

    <T> T fromJson(String json, Class<T> clazz);

    <T> T fromJson(Reader json, Type type);

    <T> T fromJson(Reader json, Class<T> clazz);

    <T> T fromJson(JsonElement json, Type type);

    <T> T fromJson(JsonElement json, Class<T> clazz);

    String toJson(Object obj);
}
