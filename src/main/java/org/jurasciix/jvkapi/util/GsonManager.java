package org.jurasciix.jvkapi.util;

import com.google.gson.*;

import java.io.Reader;
import java.lang.reflect.Type;

public class GsonManager implements JsonManager {

    private static volatile GsonManager instance = null;

    public static GsonManager getInstance() {
        GsonManager value = instance;
        if (value == null) {
            value = ensureInstance();
        }
        return value;
    }

    private static synchronized GsonManager ensureInstance() {
        GsonManager value = instance;
        if (value == null) {
            value = new GsonManager();
            instance = value;
        }
        return value;
    }

    private static IllegalArgumentException wrapJsonParseException(JsonParseException e) {
        return new IllegalArgumentException("given invalid JSON", e);
    }

    private static IllegalArgumentException wrapJsonSyntaxException(JsonSyntaxException e) {
        return new IllegalArgumentException("given invalid JSON representation", e);
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

    protected final JsonParser getParser() {
        return parser;
    }

    protected final Gson getGson() {
        return gson;
    }

    @Override
    public JsonElement parseJson(String json) {
        try {
            return parser.parse(json);
        } catch (JsonParseException e) {
            throw wrapJsonParseException(e);
        }
    }

    @Override
    public JsonElement parseJson(Reader json) {
        try {
            return parser.parse(json);
        } catch (JsonParseException e) {
            throw wrapJsonParseException(e);
        }
    }

    @Override
    public <T> T fromJson(String json, Type type) {
        try {
            return gson.fromJson(json, type);
        } catch (JsonSyntaxException e) {
            throw wrapJsonSyntaxException(e);
        } catch (JsonParseException e) {
            throw wrapJsonParseException(e);
        }
    }

    @Override
    public <T> T fromJson(Reader json, Type type) {
        try {
            return gson.fromJson(json, type);
        } catch (JsonSyntaxException e) {
            throw wrapJsonSyntaxException(e);
        } catch (JsonParseException e) {
            throw wrapJsonParseException(e);
        }
    }

    @Override
    public <T> T fromJson(String json, Class<T> clazz) {
        try {
            return gson.fromJson(json, clazz);
        } catch (JsonSyntaxException e) {
            throw wrapJsonSyntaxException(e);
        } catch (JsonParseException e) {
            throw wrapJsonParseException(e);
        }
    }

    @Override
    public <T> T fromJson(Reader json, Class<T> clazz) {
        try {
            return gson.fromJson(json, clazz);
        } catch (JsonSyntaxException e) {
            throw wrapJsonSyntaxException(e);
        } catch (JsonParseException e) {
            throw wrapJsonParseException(e);
        }
    }

    @Override
    public <T> T fromJson(JsonElement json, Type type) {
        try {
            return gson.fromJson(json, type);
        } catch (JsonSyntaxException e) {
            throw wrapJsonSyntaxException(e);
        } catch (JsonParseException e) {
            throw wrapJsonParseException(e);
        }
    }

    @Override
    public <T> T fromJson(JsonElement json, Class<T> clazz) {
        try {
            return gson.fromJson(json, clazz);
        } catch (JsonSyntaxException e) {
            throw wrapJsonSyntaxException(e);
        } catch (JsonParseException e) {
            throw wrapJsonParseException(e);
        }
    }

    @Override
    public String toJson(Object obj) {
        return gson.toJson(obj);
    }
}
