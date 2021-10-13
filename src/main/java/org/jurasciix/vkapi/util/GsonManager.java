package org.jurasciix.vkapi.util;

import com.google.gson.*;

import java.io.Reader;
import java.lang.reflect.Type;

public class GsonManager implements JsonManager {

    private static GsonManager instance;

    public static GsonManager getInstance() {
        GsonManager value = instance;
        if (value == null) {
            value = new GsonManager();
            instance = value;
        }
        return value;
    }

    private final JsonParser parser;

    private final Gson gson;

    protected GsonManager() {
        this(new JsonParser(), new Gson());
    }

    protected GsonManager(JsonParser parser, Gson gson) {
        this.parser = parser;
        this.gson = gson;
    }

    protected JsonParser getParser() {
        return parser;
    }

    protected Gson getGson() {
        return gson;
    }

    @Override
    public JsonElement parseJson(String json) throws JsonParseException {
        return parser.parse(json);
    }

    @Override
    public JsonElement parseJson(Reader json) throws JsonParseException {
        return parser.parse(json);
    }

    @Override
    public <T> T fromJson(String json, Type type) throws JsonSyntaxException {
        return gson.fromJson(json, type);
    }

    @Override
    public <T> T fromJson(Reader json, Type type) throws JsonSyntaxException {
        return gson.fromJson(json, type);
    }

    @Override
    public <T> T fromJson(String json, Class<T> clazz) throws JsonSyntaxException {
        return gson.fromJson(json, clazz);
    }

    @Override
    public <T> T fromJson(Reader json, Class<T> clazz) throws JsonSyntaxException {
        return gson.fromJson(json, clazz);
    }

    @Override
    public <T> T fromJson(JsonElement json, Type type) throws JsonSyntaxException {
        return gson.fromJson(json, type);
    }

    @Override
    public <T> T fromJson(JsonElement json, Class<T> clazz) throws JsonSyntaxException {
        return gson.fromJson(json, clazz);
    }

    @Override
    public String toJson(Object obj) {
        return gson.toJson(obj);
    }
}
