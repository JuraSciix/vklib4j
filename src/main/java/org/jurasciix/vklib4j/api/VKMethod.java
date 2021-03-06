package org.jurasciix.vklib4j.api;

import com.google.gson.JsonElement;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.jurasciix.vklib4j.exception.ApiException;
import org.jurasciix.vklib4j.exception.UncheckedApiException;
import org.jurasciix.vklib4j.api.model.common.Error;
import org.jurasciix.vklib4j.api.model.common.BoolInt;
import org.jurasciix.vklib4j.api.model.common.Lang;
import org.jurasciix.vklib4j.util.Request;

import java.lang.reflect.Type;
import java.util.*;

public class VKMethod {

    @FunctionalInterface
    public interface Action<T> {

        T perform() throws ApiException;
    }

    @Deprecated
    public interface Param {

        String value();
    }

    private static final String PARAM_ACCESS_TOKEN = "access_token";
    private static final String PARAM_VERSION = "v";

    private static final String JSON_API_ERROR = "error";
    private static final String JSON_API_RESPONSE = "response";

    private static final int API_ERROR_INTERNAL_SERVER_ERROR_CODE = 10;

    private static void checkParam(String name) {
        if (name == null) {
            throw new IllegalArgumentException("name must be not null");
        }
        if (name.equalsIgnoreCase(PARAM_ACCESS_TOKEN) || name.equalsIgnoreCase(PARAM_VERSION)) {
            throw new IllegalArgumentException(name);
        }
    }

    public static <T> T performAs(VKMethod method, VKActor actor, Type type) {
        return perform(() -> method.executeAs(actor, type));
    }

    public static <T> T performAs(VKMethod method, VKActor actor, Class<T> clazz) {
        return perform(() -> method.executeAs(actor, clazz));
    }

    public static JsonElement perform(VKMethod method, VKActor actor) {
        return perform(() -> method.execute(actor));
    }

    public static <T> T performRawAs(VKMethod method, VKActor actor, Type type) {
        return perform(() -> method.executeRawAs(actor, type));
    }

    public static <T> T performRawAs(VKMethod method, VKActor actor, Class<T> clazz) {
        return perform(() -> method.executeRawAs(actor, clazz));
    }

    public static JsonElement performRaw(VKMethod method, VKActor actor) {
        return perform(() -> method.executeRaw(actor));
    }

    public static <T> T perform(Action<T> action) {
        try {
            return action.perform();
        } catch (ApiException e) {
            if (e.getErrorCode() != API_ERROR_INTERNAL_SERVER_ERROR_CODE) {
                throw new UncheckedApiException(e.getError());
            }
        }
        try {
            return action.perform();
        } catch (ApiException e) {
            throw new UncheckedApiException(e.getError());
        }
    }

    private final String name;

    // todo: replace it with URIBuilder
    private final List<NameValuePair> params;

    public VKMethod(String name) {
        this(name, new ArrayList<>());
    }

    public VKMethod(VKMethod method) {
        this(method.name, new ArrayList<>(method.params));
    }

    protected VKMethod(String name, List<NameValuePair> params) {
        this.name = Objects.requireNonNull(name, "method name");
        this.params = Objects.requireNonNull(params, "method params");
    }

    public final String getName() {
        return name;
    }

    public final List<NameValuePair> getParams() {
        return Collections.unmodifiableList(params);
    }

    public VKMethod lang(Lang lang) {
        return param("lang", lang);
    }

    public VKMethod captchaSid(long sid) {
        return param("captcha_sid", sid);
    }

    public VKMethod captchaKey(String key) {
        return param("captcha_ley", key);
    }

    public VKMethod confirm() {
        return param("confirm", BoolInt.TRUE);
    }

    public VKMethod param(String name, int value) {
        checkParam(name);
        return unsafeParam(name, Integer.toString(value));
    }

    public VKMethod param(String name, long value) {
        checkParam(name);
        return unsafeParam(name, Long.toString(value));
    }

    public VKMethod param(String name, double value) {
        checkParam(name);
        return unsafeParam(name, Double.toString(value));
    }

    public VKMethod param(String name, float value) {
        checkParam(name);
        return unsafeParam(name, Float.toString(value));
    }

    public VKMethod param(String name, char value) {
        checkParam(name);
        return unsafeParam(name, Character.toString(value));
    }

    public VKMethod param(String name, boolean value) {
        checkParam(name);
        return unsafeParam(name, Boolean.toString(value));
    }

    public VKMethod param(String name, JsonElement value) {
        checkParam(name);
        return unsafeParam(name, value == null ? null : value.toString());
    }

    public VKMethod param(String name, Param value) {
        checkParam(name);
        return unsafeParam(name, value == null ? null : value.value());
    }

    public VKMethod param(String name, String value) {
        checkParam(name);
        return unsafeParam(name, value);
    }

    public VKMethod param(String name, int... values) {
        checkParam(name);
        StringJoiner joiner = new StringJoiner(",");
        for (int value : values) {
            joiner.add(Integer.toString(value));
        }
        return unsafeParam(name, joiner.toString());
    }

    public VKMethod param(String name, long... values) {
        checkParam(name);
        StringJoiner joiner = new StringJoiner(",");
        for (long value : values) {
            joiner.add(Long.toString(value));
        }
        return unsafeParam(name, joiner.toString());
    }

    public VKMethod param(String name, double... values) {
        checkParam(name);
        StringJoiner joiner = new StringJoiner(",");
        for (double value : values) {
            joiner.add(Double.toString(value));
        }
        return unsafeParam(name, joiner.toString());
    }

    public VKMethod param(String name, float... values) {
        checkParam(name);
        StringJoiner joiner = new StringJoiner(",");
        for (float value : values) {
            joiner.add(Float.toString(value));
        }
        return unsafeParam(name, joiner.toString());
    }

    public VKMethod param(String name, char... values) {
        checkParam(name);
        StringJoiner joiner = new StringJoiner(",");
        for (char value : values) {
            joiner.add(Character.toString(value));
        }
        return unsafeParam(name, joiner.toString());
    }

    public VKMethod param(String name, boolean... values) {
        checkParam(name);
        StringJoiner joiner = new StringJoiner(",");
        for (boolean value : values) {
            joiner.add(Boolean.toString(value));
        }
        return unsafeParam(name, joiner.toString());
    }

    public VKMethod param(String name, JsonElement... values) {
        checkParam(name);
        StringJoiner joiner = new StringJoiner(",");
        for (JsonElement value : values) {
            joiner.add(value == null ? null : value.toString());
        }
        return unsafeParam(name, joiner.toString());
    }

    public VKMethod param(String name, Param... values) {
        checkParam(name);
        StringJoiner joiner = new StringJoiner(",");
        for (Param value : values) {
            joiner.add(value == null ? null : value.value());
        }
        return unsafeParam(name, joiner.toString());
    }

    public VKMethod param(String name, String... values) {
        checkParam(name);
        String value = String.join(",", values);
        return unsafeParam(name, value);
    }

    public VKMethod param(String name, Collection<?> values) {
        checkParam(name);
        StringJoiner joiner = new StringJoiner(",");
        for (Object value : values) {
            joiner.add(value == null ? null : value.toString());
        }
        return unsafeParam(name, joiner.toString());
    }

    protected VKMethod unsafeParam(String name, String value) {
        params.add(new BasicNameValuePair(name, value));
        return this;
    }

    public <T> T executeAs(VKActor actor, Type type) throws ApiException {
        JsonElement result = execute(actor);
        return actor.getApi().getJsonManager().fromJson(result, type);
    }

    public <T> T executeAs(VKActor actor, Class<T> clazz) throws ApiException {
        JsonElement result = execute(actor);
        return actor.getApi().getJsonManager().fromJson(result, clazz);
    }

    public JsonElement execute(VKActor actor) throws ApiException {
        JsonElement json = executeRaw(actor);

        if (json.isJsonObject() && json.getAsJsonObject().has(JSON_API_RESPONSE)) {
            json = json.getAsJsonObject().get(JSON_API_RESPONSE);
        }
        return json;
    }

    public <T> T executeRawAs(VKActor actor, Type type) throws ApiException {
        JsonElement result = executeRaw(actor);
        return actor.getApi().getJsonManager().fromJson(result, type);
    }

    public <T> T executeRawAs(VKActor actor, Class<T> clazz) throws ApiException {
        JsonElement result = executeRaw(actor);
        return actor.getApi().getJsonManager().fromJson(result, clazz);
    }

    public JsonElement executeRaw(VKActor actor) throws ApiException {
        Request.Response response = doExecuteRequest(actor);
        JsonElement json = actor.getApi().getJsonManager().parseJson(response.getContent());

        if (json.isJsonObject() && json.getAsJsonObject().has(JSON_API_ERROR)) {
            JsonElement errorJson = json.getAsJsonObject().get(JSON_API_ERROR);
            Error error = actor.getApi().getJsonManager().fromJson(errorJson, Error.class);
            throw new ApiException(error);
        }
        return json;
    }

    protected Request.Response doExecuteRequest(VKActor actor) {
        if (actor == null) {
            throw new IllegalArgumentException("actor must be not null");
        }
        Request request = actor.getApi().createApiRequest();
        request.addPathSegment(name);
        request.addParameter(PARAM_ACCESS_TOKEN, actor.getAccessToken());
        request.addParameter(PARAM_VERSION, actor.getApi().getVersion());
        request.addParameters(params);
        return request.execute();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        VKMethod m = (VKMethod) o;
        // name and params should not be null
        return name.equals(m.name) && params.equals(m.params);
    }

    @Override
    public int hashCode() {
        // name and params should not be null
        return name.hashCode() * 31 + params.hashCode();
    }

    @Override
    public String toString() {
        return getClass().getName() + '(' +
                "name=" + name + ',' +
                "params=" + params +
                ')';
    }
}
