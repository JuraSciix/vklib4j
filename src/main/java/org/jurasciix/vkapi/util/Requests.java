package org.jurasciix.vkapi.util;

import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Objects;

public final class Requests {

    private static final int HTTP_STATUS_CODE_OK = 200;

    private static final String HTTP_MIME_TYPE_JSON = "application/json";

    public static Request.Response performAction(Request request) {
        Objects.requireNonNull(request, "request");
        Request.Response response;

        try {
            response = request.execute();
        } catch (IOException e) {
            throw new RequestException("I/O troubles", e);
        }

        if (response.getStatusCode() != HTTP_STATUS_CODE_OK) {
            throw new RequestException("unexpected status code received: " + response.getStatusCode());
        }

        return response;
    }

    public static JsonElement parseJson(JsonManager jsonManager, Request.Response response) {
        Objects.requireNonNull(jsonManager, "jsonManager");
        String content = getJSONContent(response);

        try {
            return jsonManager.parseJson(content);
        } catch (JsonParseException e) {
            throw jsonException(e);
        }
    }

    public static <T> T fromJson(JsonManager jsonManager, Request.Response response, Type type) {
        Objects.requireNonNull(jsonManager, "jsonManager");
        Objects.requireNonNull(type, "type");
        String content = getJSONContent(response);

        try {
            return jsonManager.fromJson(content, type);
        } catch (JsonSyntaxException e) {
            throw jsonException(e);
        }
    }

    public static <T> T fromJson(JsonManager jsonManager, Request.Response response, Class<T> clazz) {
        Objects.requireNonNull(jsonManager, "jsonManager");
        Objects.requireNonNull(clazz, "clazz");
        String content = getJSONContent(response);

        try {
            return jsonManager.fromJson(content, clazz);
        } catch (JsonSyntaxException e) {
            throw jsonException(e);
        }
    }

    private static String getJSONContent(Request.Response response) {
        if (response == null) {
            return null;
        }
        if (!response.getContentType().contains(HTTP_MIME_TYPE_JSON)) {
            throw new RequestException("response does not contain a JSON content: " + response.getContentType());
        }
        return response.getContent();
    }

    private static RequestException jsonException(JsonParseException e) {
        Throwable cause = e;
        if (e.getCause() != null && StringUtils.trimToNull(e.getMessage()) == null) {
            cause = e.getCause();
        }
        return new RequestException("invalid JSON returned", cause);
    }

    public Requests() {
        throw new UnsupportedOperationException();
    }
}
