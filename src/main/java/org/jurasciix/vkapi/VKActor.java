package org.jurasciix.vkapi;

import org.jurasciix.vkapi.util.GsonManager;
import org.jurasciix.vkapi.util.HttpRequestFactory;
import org.jurasciix.vkapi.util.JsonManager;
import org.jurasciix.vkapi.util.Request;

public class VKActor {

    public static final String ACTUAL_API_VERSION = "5.131";

    protected static final Request.Factory DEFAULT_REQUEST_FACTORY = HttpRequestFactory.getInstance();

    protected static final JsonManager DEFAULT_JSON_MANAGER = GsonManager.getInstance();

    protected static final String DEFAULT_VERSION = ACTUAL_API_VERSION;

    private final Request.Factory requestFactory;

    private final JsonManager jsonManager;

    private final String accessToken;

    private final String version;

    public VKActor(String accessToken) {
        this(DEFAULT_REQUEST_FACTORY, DEFAULT_JSON_MANAGER, accessToken, DEFAULT_VERSION);
    }

    public VKActor(String accessToken, String version) {
        this(DEFAULT_REQUEST_FACTORY, DEFAULT_JSON_MANAGER, accessToken, version);
    }

    public VKActor(Request.Factory requestFactory, String accessToken) {
        this(requestFactory, DEFAULT_JSON_MANAGER, accessToken, DEFAULT_VERSION);
    }

    public VKActor(Request.Factory requestFactory, String accessToken, String version) {
        this(requestFactory, DEFAULT_JSON_MANAGER, accessToken, version);
    }

    public VKActor(JsonManager jsonManager, String accessToken) {
        this(DEFAULT_REQUEST_FACTORY, jsonManager, accessToken, DEFAULT_VERSION);
    }

    public VKActor(JsonManager jsonManager, String accessToken, String version) {
        this(DEFAULT_REQUEST_FACTORY, jsonManager, accessToken, version);
    }

    public VKActor(Request.Factory requestFactory, JsonManager jsonManager, String accessToken, String version) {
        this.requestFactory = requestFactory;
        this.jsonManager = jsonManager;
        this.accessToken = accessToken;
        this.version = version;
    }

    public Request.Factory getRequestFactory() {
        return requestFactory;
    }

    public JsonManager getJsonManager() {
        return jsonManager;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getVersion() {
        return version;
    }
}
