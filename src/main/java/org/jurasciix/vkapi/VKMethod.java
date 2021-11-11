package org.jurasciix.vkapi;

import com.google.gson.JsonElement;
import com.google.gson.JsonSyntaxException;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.jurasciix.vkapi.util.JsonManager;
import org.jurasciix.vkapi.util.LombokToStringStyle;
import org.jurasciix.vkapi.util.Request;
import org.jurasciix.vkapi.util.Requests;

import java.lang.reflect.Type;
import java.util.*;

public class VKMethod {

    @FunctionalInterface
    public interface Action<T> {

        T execute() throws ApiException;
    }

    protected static final String HTTP_API_SCHEME = "https";
    protected static final String HTTP_API_HOST = "api.vk.com";
    protected static final String HTTP_API_PATH = "method";

    protected static final String PARAM_ACCESS_TOKEN = "access_token";
    protected static final String PARAM_VERSION = "v";

    protected static final String JSON_API_ERROR = "error";
    protected static final String JSON_API_RESPONSE = "response";

    protected static final int API_ERROR_INTERNAL_SERVER_ERROR_CODE = 10;

    protected static void checkParam(String name) {
        Objects.requireNonNull(name, "name");

        if (name.equalsIgnoreCase(PARAM_ACCESS_TOKEN) || name.equalsIgnoreCase(PARAM_VERSION)) {
            throw new IllegalArgumentException(name);
        }
    }

    protected static String stringOf(Object value) {
        if (value != null) {
            Class<?> clazz = value.getClass();

            if (clazz == Integer.class) {
                return stringOf((int) value);
            }
            if (clazz == Long.class) {
                return stringOf((long) value);
            }
            if (clazz == Double.class) {
                return stringOf((double) value);
            }
            if (clazz == Float.class) {
                return stringOf((float) value);
            }
            if (clazz == Character.class) {
                return stringOf((char) value);
            }
            if (clazz == Boolean.class) {
                return stringOf((boolean) value);
            }
        }
        return String.valueOf(value);
    }

    protected static String stringOf(int value) {
        return Integer.toString(value);
    }

    protected static String stringOf(long value) {
        return Long.toString(value);
    }

    protected static String stringOf(double value) {
        return Double.toString(value);
    }

    protected static String stringOf(float value) {
        return Float.toString(value);
    }

    protected static String stringOf(char value) {
        return Character.toString(value);
    }

    protected static String stringOf(boolean value) {
        return value ? "1" : "0";
    }

    public static JsonElement performAction(VKMethod method, VKActor actor) {
        return performAction(() -> method.execute(actor));
    }

    public static <T> T performActionAs(VKMethod method, VKActor actor, Type type) {
        return performAction(() -> method.executeAs(actor, type));
    }

    public static <T> T performActionAs(VKMethod method, VKActor actor, Class<T> clazz) {
        return performAction(() -> method.executeAs(actor, clazz));
    }

    public static JsonElement performActionRaw(VKMethod method, VKActor actor) {
        return performAction(() -> method.executeRaw(actor));
    }

    public static <T> T performActionRawAs(VKMethod method, VKActor actor, Type type) {
        return performAction(() -> method.executeRawAs(actor, type));
    }

    public static <T> T performActionRawAs(VKMethod method, VKActor actor, Class<T> clazz) {
        return performAction(() -> method.executeRawAs(actor, clazz));
    }

    public static <T> T performAction(Action<T> action) {
        try {
            return action.execute();
        } catch (ApiException e) {
            if (e.getErrorCode() != API_ERROR_INTERNAL_SERVER_ERROR_CODE) {
                throw wrapApiException(e);
            }
            try {
                return action.execute();
            } catch (ApiException e1) {
                throw wrapApiException(e1);
            }
        }
    }

    private static IllegalStateException wrapApiException(ApiException e) {
        return new IllegalStateException("VK API returned an error: " + e.getMessage(), e);
    }

    private static Error parseApiErrorJSON(JsonManager jsonManager, JsonElement json) {
        try {
            return jsonManager.fromJson(json, Error.class);
        } catch (JsonSyntaxException e) {
            throw wrapJsonException(e);
        }
    }

    private static IllegalStateException wrapJsonException(JsonSyntaxException e) {
        Throwable cause = e;
        if (cause.getCause() != null && StringUtils.trimToNull(e.getMessage()) == null) {
            cause = e.getCause();
        }
        return new IllegalStateException("incorrect JSON representation", cause);
    }

    private final String method;

    private final Map<String, String> params;

    public VKMethod(String method) {
        this(method, new LinkedHashMap<>());
    }

    public VKMethod(VKMethod method) {
        this(method.method, new LinkedHashMap<>(method.params));
    }

    protected VKMethod(String method, Map<String, String> params) {
        this.method = method;
        this.params = params;
    }

    public String getMethod() {
        return method;
    }

    public Map<String, String> getParams() {
        return Collections.unmodifiableMap(params);
    }

    public VKMethod param(String name, String value) {
        checkParam(name);
        params.put(name, value);
        return this;
    }

    public VKMethod param(String name, Object value) {
        checkParam(name);
        params.put(name, stringOf(value));
        return this;
    }

    public VKMethod param(String name, int value) {
        checkParam(name);
        params.put(name, stringOf(value));
        return this;
    }

    public VKMethod param(String name, long value) {
        checkParam(name);
        params.put(name, stringOf(value));
        return this;
    }

    public VKMethod param(String name, double value) {
        checkParam(name);
        params.put(name, stringOf(value));
        return this;
    }

    public VKMethod param(String name, float value) {
        checkParam(name);
        params.put(name, stringOf(value));
        return this;
    }

    public VKMethod param(String name, char value) {
        checkParam(name);
        params.put(name, stringOf(value));
        return this;
    }

    public VKMethod param(String name, boolean value) {
        checkParam(name);
        params.put(name, stringOf(value));
        return this;
    }

    public VKMethod param(String name, Iterable<?> values) {
        checkParam(name);
        StringJoiner joiner = new StringJoiner(",");
        for (Object value : values) {
            joiner.add(stringOf(value));
        }
        params.put(name, joiner.toString());
        return this;
    }

    public VKMethod param(String name, Object... values) {
        checkParam(name);
        StringJoiner joiner = new StringJoiner(",");
        for (Object value : values) {
            joiner.add(stringOf(value));
        }
        params.put(name, joiner.toString());
        return this;
    }

    public VKMethod param(String name, int... values) {
        checkParam(name);
        StringJoiner joiner = new StringJoiner(",");
        for (int value : values) {
            joiner.add(stringOf(value));
        }
        params.put(name, joiner.toString());
        return this;
    }

    public VKMethod param(String name, long... values) {
        checkParam(name);
        StringJoiner joiner = new StringJoiner(",");
        for (long value : values) {
            joiner.add(stringOf(value));
        }
        params.put(name, joiner.toString());
        return this;
    }

    public VKMethod param(String name, double... values) {
        checkParam(name);
        StringJoiner joiner = new StringJoiner(",");
        for (double value : values) {
            joiner.add(stringOf(value));
        }
        params.put(name, joiner.toString());
        return this;
    }

    public VKMethod param(String name, float... values) {
        checkParam(name);
        StringJoiner joiner = new StringJoiner(",");
        for (float value : values) {
            joiner.add(stringOf(value));
        }
        params.put(name, joiner.toString());
        return this;
    }

    public VKMethod param(String name, char... values) {
        checkParam(name);
        StringJoiner joiner = new StringJoiner(",");
        for (char value : values) {
            joiner.add(stringOf(value));
        }
        params.put(name, joiner.toString());
        return this;
    }

    public VKMethod param(String name, boolean... values) {
        checkParam(name);
        StringJoiner joiner = new StringJoiner(",");
        for (boolean value : values) {
            joiner.add(stringOf(value));
        }
        params.put(name, joiner.toString());
        return this;
    }

    public JsonElement execute(VKActor actor) throws ApiException {
        JsonElement json = executeRaw(actor);

        if (json.isJsonObject() && json.getAsJsonObject().has(JSON_API_RESPONSE)) {
            json = json.getAsJsonObject().get(JSON_API_RESPONSE);
        }
        return json;
    }

    public <T> T executeAs(VKActor actor, Type type) throws ApiException {
        JsonElement response = execute(actor);

        try {
            return actor.getJsonManager().fromJson(response, type);
        } catch (JsonSyntaxException e) {
            throw wrapJsonException(e);
        }
    }

    public <T> T executeAs(VKActor actor, Class<T> clazz) throws ApiException {
        JsonElement response = execute(actor);

        try {
            return actor.getJsonManager().fromJson(response, clazz);
        } catch (JsonSyntaxException e) {
            throw wrapJsonException(e);
        }
    }

    public JsonElement executeRaw(VKActor actor) throws ApiException {
        Request.Response response = doRequest(actor);
        JsonElement json = Requests.parseJson(actor.getJsonManager(), response);

        if (json.isJsonObject() && json.getAsJsonObject().has(JSON_API_ERROR)) {
            Error error = parseApiErrorJSON(actor.getJsonManager(),
                    json.getAsJsonObject().get(JSON_API_ERROR));
            throw new ApiException(error);
        }
        return json;
    }

    public <T> T executeRawAs(VKActor actor, Type type) throws ApiException {
        JsonElement response = executeRaw(actor);

        try {
            return actor.getJsonManager().fromJson(response, type);
        } catch (JsonSyntaxException e) {
            throw wrapJsonException(e);
        }
    }

    public <T> T executeRawAs(VKActor actor, Class<T> clazz) throws ApiException {
        JsonElement response = executeRaw(actor);

        try {
            return actor.getJsonManager().fromJson(response, clazz);
        } catch (JsonSyntaxException e) {
            throw wrapJsonException(e);
        }
    }

    protected Request.Response doRequest(VKActor actor) {
        Objects.requireNonNull(actor, "actor");
        Request request = actor.getRequestFactory().newGet();
        request.setScheme(HTTP_API_SCHEME);
        request.setHost(HTTP_API_HOST);
        request.setPathSegments(HTTP_API_PATH, method);
        request.addParameter(PARAM_ACCESS_TOKEN, actor.getAccessToken());
        request.addParameter(PARAM_VERSION, actor.getVersion());
        request.addParameters(params);
        return Requests.performAction(request);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (null == o || getClass() != o.getClass()) return false;
        VKMethod another = (VKMethod) o;
        return new EqualsBuilder()
                .append(method, another.method)
                .append(params, another.params).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(method)
                .append(params).toHashCode();
    }

    @Override
    public String toString() {
        return LombokToStringStyle.getToStringBuilder(this)
                .append("method", method)
                .append("params", params).toString();
    }
}
