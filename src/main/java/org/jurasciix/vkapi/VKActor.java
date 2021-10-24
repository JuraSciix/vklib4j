package org.jurasciix.vkapi;

import org.jurasciix.vkapi.util.GsonManager;
import org.jurasciix.vkapi.util.HttpRequestFactory;
import org.jurasciix.vkapi.util.JsonManager;
import org.jurasciix.vkapi.util.Request;

public class VKActor {

    public static final String VK_API_VERSION = "5.131";

    protected static final Request.Factory DEFAULT_REQUEST_FACTORY = HttpRequestFactory.getInstance();

    protected static final JsonManager DEFAULT_JSON_MANAGER = GsonManager.getInstance();

    protected static final String DEFAULT_VERSION = VK_API_VERSION;

    protected static final int DEFAULT_TARGET_ID = 0;

    private final Request.Factory requestFactory;

    private final JsonManager jsonManager;

    private final String accessToken;

    private final String version;

    private final int targetId;

    public VKActor(String accessToken) {
        this(DEFAULT_REQUEST_FACTORY, DEFAULT_JSON_MANAGER, accessToken, DEFAULT_VERSION, DEFAULT_TARGET_ID);
    }

    public VKActor(String accessToken, int targetId) {
        this(DEFAULT_REQUEST_FACTORY, DEFAULT_JSON_MANAGER, accessToken, DEFAULT_VERSION, targetId);
    }

    public VKActor(String accessToken, String version) {
        this(DEFAULT_REQUEST_FACTORY, DEFAULT_JSON_MANAGER, accessToken, version, DEFAULT_TARGET_ID);
    }

    public VKActor(String accessToken, String version, int targetId) {
        this(DEFAULT_REQUEST_FACTORY, DEFAULT_JSON_MANAGER, accessToken, version, targetId);
    }

    public VKActor(Request.Factory requestFactory, String accessToken) {
        this(requestFactory, DEFAULT_JSON_MANAGER, accessToken, DEFAULT_VERSION, DEFAULT_TARGET_ID);
    }

    public VKActor(Request.Factory requestFactory, String accessToken, int targetId) {
        this(requestFactory, DEFAULT_JSON_MANAGER, accessToken, DEFAULT_VERSION, targetId);
    }

    public VKActor(Request.Factory requestFactory, String accessToken, String version, int targetId) {
        this(requestFactory, DEFAULT_JSON_MANAGER, accessToken, version, targetId);
    }

    public VKActor(JsonManager jsonManager, String accessToken) {
        this(DEFAULT_REQUEST_FACTORY, jsonManager, accessToken, DEFAULT_VERSION, DEFAULT_TARGET_ID);
    }

    public VKActor(JsonManager jsonManager, String accessToken, int targetId) {
        this(DEFAULT_REQUEST_FACTORY, jsonManager, accessToken, DEFAULT_VERSION, targetId);
    }

    public VKActor(JsonManager jsonManager, String accessToken, String version, int targetId) {
        this(DEFAULT_REQUEST_FACTORY, jsonManager, accessToken, version, targetId);
    }

    public VKActor(Request.Factory requestFactory, JsonManager jsonManager, String accessToken) {
        this(requestFactory, jsonManager, accessToken, DEFAULT_VERSION, DEFAULT_TARGET_ID);
    }

    public VKActor(Request.Factory requestFactory, JsonManager jsonManager, String accessToken, int targetId) {
        this(requestFactory, jsonManager, accessToken, DEFAULT_VERSION, targetId);
    }

    public VKActor(Request.Factory requestFactory, JsonManager jsonManager, String accessToken, String version, int targetId) {
        this.requestFactory = requestFactory;
        this.jsonManager = jsonManager;
        this.accessToken = accessToken;
        this.version = version;
        this.targetId = targetId;
    }

    public static Request.Factory getDefaultRequestFactory() {
        return DEFAULT_REQUEST_FACTORY;
    }

    public static JsonManager getDefaultJsonManager() {
        return DEFAULT_JSON_MANAGER;
    }

    public static String getDefaultVersion() {
        return DEFAULT_VERSION;
    }

    public static int getDefaultTargetId() {
        return DEFAULT_TARGET_ID;
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

    public int getTargetId() {
        return targetId;
    }
}
