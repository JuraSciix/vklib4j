package org.jurasciix.vkapi.util;

import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Objects;

public final class Requests {

    private static final int HTTP_EXPECTED_STATUS_CODE = 200;

    public static Request.Response execute(Request request) {
        Request.Response response;
        Objects.requireNonNull(request, "request must be not null");

        try {
            response = request.execute();
        } catch (IOException e) {
            throw new RequestException("I/O troubles", e);
        }

        if (response.getStatusCode() != HTTP_EXPECTED_STATUS_CODE) {
            throw new RequestException("returned an unexpected status code: " + response.getStatusCode());
        }

        return response;
    }

    public static JsonElement parseJson(JsonManager manager, Request.Response response) {
        Objects.requireNonNull(manager, "manager must be not null");

        try {
            return manager.parseJson(getContentOfNullable(response));
        } catch (JsonParseException e) {
            throw jsonException(e);
        }
    }

    public static <T> T fromJson(JsonManager manager, Request.Response response, Type type) {
        Objects.requireNonNull(manager, "manager must be not null");
        Objects.requireNonNull(type, "type must be not null");

        try {
            return manager.fromJson(getContentOfNullable(response), type);
        } catch (JsonSyntaxException e) {
            throw jsonException(e);
        }
    }

    public static <T> T fromJson(JsonManager manager, Request.Response response, Class<T> clazz) {
        Objects.requireNonNull(manager, "manager must be not null");
        Objects.requireNonNull(clazz, "clazz must be not null");

        try {
            return manager.fromJson(getContentOfNullable(response), clazz);
        } catch (JsonSyntaxException e) {
            throw jsonException(e);
        }
    }

    private static String getContentOfNullable(Request.Response response) {
        return (response == null) ? null : response.getContent();
    }

    private static RequestException jsonException(JsonParseException e) {
        Throwable ex = e;
        if (e.getCause() != null && StringUtils.isEmpty(StringUtils.trim(e.getMessage()))) {
            ex = e.getCause();
        }
        throw new RequestException("returned not a valid JSON", ex);
    }

    private Requests() {
        throw new UnsupportedOperationException();
    }
}
