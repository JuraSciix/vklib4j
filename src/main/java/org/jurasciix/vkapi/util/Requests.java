package org.jurasciix.vkapi.util;

import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.lang.reflect.Type;

public final class Requests {

    public static final int HTTP_EXPECTED_STATUS_CODE = 200;

    public static Request.Response execute(Request request) {
        Request.Response resp;

        try {
            resp = request.execute();
        } catch (IOException e) {
            throw new RequestException("I/O troubles", e);
        }

        if (resp.getStatusCode() != HTTP_EXPECTED_STATUS_CODE) {
            throw new RequestException("server returned an unexpected status code: " + resp.getStatusCode());
        }

        return resp;
    }

    public static JsonElement parseJson(JsonManager manager, Request.Response response) {
        try {
            return manager.parseJson(response.getContent());
        } catch (JsonParseException e) {
            throw jsonException(e);
        }
    }

    public static <T> T fromJson(JsonManager manager, Request.Response response, Type type) {
        try {
            return manager.fromJson(response.getContent(), type);
        } catch (JsonSyntaxException e) {
            throw jsonException(e);
        }
    }

    public static <T> T fromJson(JsonManager manager, Request.Response response, Class<T> clazz) {
        try {
            return manager.fromJson(response.getContent(), clazz);
        } catch (JsonSyntaxException e) {
            throw jsonException(e);
        }
    }

    private static RequestException jsonException(JsonParseException e) {
        Throwable ex = e;
        if (e.getCause() != null && StringUtils.isEmpty(StringUtils.trim(e.getMessage()))) {
            ex = e.getCause();
        }
        throw new RequestException("server returned not a valid JSON", ex);
    }

    private Requests() {
        throw new UnsupportedOperationException();
    }
}
