package org.jurasciix.vkapi.util;

import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;

import java.io.Reader;
import java.lang.reflect.Type;

public interface JsonManager {

    JsonElement parseJson(String json) throws JsonParseException;

    JsonElement parseJson(Reader json) throws JsonParseException;

    <T> T fromJson(String json, Type type) throws JsonSyntaxException;

    <T> T fromJson(Reader json, Type type) throws JsonSyntaxException;

    <T> T fromJson(String json, Class<T> clazz) throws JsonSyntaxException;

    <T> T fromJson(Reader json, Class<T> clazz) throws JsonSyntaxException;

    <T> T fromJson(JsonElement json, Type type) throws JsonSyntaxException;

    <T> T fromJson(JsonElement json, Class<T> clazz) throws JsonSyntaxException;

    String toJson(Object obj);
}
