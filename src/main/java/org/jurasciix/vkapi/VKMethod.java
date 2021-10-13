package org.jurasciix.vkapi;

import com.google.gson.JsonElement;
import com.google.gson.JsonSyntaxException;
import org.apache.commons.lang3.StringUtils;
import org.jurasciix.vkapi.util.Request;
import org.jurasciix.vkapi.util.Requests;

import java.lang.reflect.Type;
import java.util.*;

public class VKMethod {

    public static final String HTTP_API_SCHEME = "https";
    public static final String HTTP_API_HOST = "api.vk.com";
    public static final String HTTP_API_PATH = "method";

    public static final String PARAM_ACCESS_TOKEN = "access_token";
    public static final String PARAM_VERSION = "v";

    public static final String JSON_API_ERROR = "error";
    public static final String JSON_API_RESPONSE = "response";

    protected static void checkParam(String name) {
        if (name == null) {
            throw new NullPointerException("name must be not null");
        }
        if (name.equalsIgnoreCase(PARAM_ACCESS_TOKEN) || name.equalsIgnoreCase(PARAM_VERSION)) {
            throw new IllegalArgumentException(name);
        }
    }

    protected static String stringOf(Object value) {
        if (value == null) {
            return null;
        }
        if (value.getClass() == Boolean.class) {
            return stringOf((boolean) value);
        }
        return value.toString();
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

    private static IllegalStateException jsonException(JsonSyntaxException e) {
        Throwable ex = e;
        if (ex.getCause() != null && StringUtils.isBlank(StringUtils.trim(e.getMessage()))) {
            ex = e.getCause();
        }
        throw new IllegalStateException("not a valid JSON representation", ex);
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

    public VKMethod param(String name, Collection<?> values) {
        checkParam(name);
        StringJoiner joiner = new StringJoiner(",");
        for (Object value: values) {
            joiner.add(stringOf(value));
        }
        params.put(name, joiner.toString());
        return this;
    }

    public VKMethod param(String name, Object... values) {
        checkParam(name);
        StringJoiner joiner = new StringJoiner(",");
        for (Object value: values) {
            joiner.add(stringOf(value));
        }
        params.put(name, joiner.toString());
        return this;
    }

    public VKMethod param(String name, int... values) {
        checkParam(name);
        StringJoiner joiner = new StringJoiner(",");
        for (int value: values) {
            joiner.add(stringOf(value));
        }
        params.put(name, joiner.toString());
        return this;
    }

    public VKMethod param(String name, long... values) {
        checkParam(name);
        StringJoiner joiner = new StringJoiner(",");
        for (long value: values) {
            joiner.add(stringOf(value));
        }
        params.put(name, joiner.toString());
        return this;
    }

    public VKMethod param(String name, double... values) {
        checkParam(name);
        StringJoiner joiner = new StringJoiner(",");
        for (double value: values) {
            joiner.add(stringOf(value));
        }
        params.put(name, joiner.toString());
        return this;
    }

    public VKMethod param(String name, float... values) {
        checkParam(name);
        StringJoiner joiner = new StringJoiner(",");
        for (float value: values) {
            joiner.add(stringOf(value));
        }
        params.put(name, joiner.toString());
        return this;
    }

    public VKMethod param(String name, char... values) {
        checkParam(name);
        StringJoiner joiner = new StringJoiner(",");
        for (char value: values) {
            joiner.add(stringOf(value));
        }
        params.put(name, joiner.toString());
        return this;
    }

    public VKMethod param(String name, boolean... values) {
        checkParam(name);
        StringJoiner joiner = new StringJoiner(",");
        for (boolean value: values) {
            joiner.add(stringOf(value));
        }
        params.put(name, joiner.toString());
        return this;
    }

    public <T> T executeAs(VKActor actor, Class<T> clazz) throws ApiException {
        JsonElement response = execute(actor);

        try {
            return actor.getJsonManager().fromJson(response, clazz);
        } catch (JsonSyntaxException e) {
            throw jsonException(e);
        }
    }

    public <T> T executeAs(VKActor actor, Type type) throws ApiException {
        JsonElement response = execute(actor);

        try {
            return actor.getJsonManager().fromJson(response, type);
        } catch (JsonSyntaxException e) {
            throw jsonException(e);
        }
    }

    public JsonElement execute(VKActor actor) throws ApiException {
        JsonElement json = executeRaw(actor);

        if (json.isJsonObject() && json.getAsJsonObject().has(JSON_API_RESPONSE)) {
            json = json.getAsJsonObject().get(JSON_API_RESPONSE);
        }
        return json;
    }

    public <T> T executeRawAs(VKActor actor, Class<T> clazz) throws ApiException {
        JsonElement response = executeRaw(actor);

        try {
            return actor.getJsonManager().fromJson(response, clazz);
        } catch (JsonSyntaxException e) {
            throw jsonException(e);
        }
    }

    public <T> T executeRawAs(VKActor actor, Type type) throws ApiException {
        JsonElement response = executeRaw(actor);

        try {
            return actor.getJsonManager().fromJson(response, type);
        } catch (JsonSyntaxException e) {
            throw jsonException(e);
        }
    }

    public JsonElement executeRaw(VKActor actor) throws ApiException {
        Request.Response response = requestMethod(actor);
        JsonElement json = Requests.parseJson(actor.getJsonManager(), response);

        if (json.isJsonObject() && json.getAsJsonObject().has(JSON_API_ERROR)) {
            Error error;

            try {
                error = actor.getJsonManager().fromJson(json.getAsJsonObject().get(JSON_API_ERROR), Error.class);
            } catch (JsonSyntaxException e) {
                throw new IllegalStateException(e);
            }
            throw new ApiException(error);
        }
        return json;
    }

    protected Request.Response requestMethod(VKActor actor) {
        Request request = actor.getRequestFactory().newRequest();
        request.setScheme(HTTP_API_SCHEME);
        request.setHost(HTTP_API_HOST);
        request.setPathSegments(HTTP_API_PATH, method);
        request.addParameter(PARAM_ACCESS_TOKEN, actor.getAccessToken());
        request.addParameter(PARAM_VERSION, actor.getVersion());
        request.addParameters(params);
        return Requests.execute(request);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof VKMethod)) return false;
        VKMethod another = (VKMethod) o;
        return Objects.equals(method, another.method) && Objects.equals(params, another.params);
    }

    @Override
    public int hashCode() {
        return Objects.hash(method, params);
    }

    @Override
    public String toString() {
        return "VKMethod(method=" + method + ", params=" + params + ')';
    }
}
