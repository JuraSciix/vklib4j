package org.jurasciix.vkapi;

import org.jurasciix.vkapi.util.GsonManager;
import org.jurasciix.vkapi.util.HttpRequestFactory;
import org.jurasciix.vkapi.util.JsonManager;
import org.jurasciix.vkapi.util.RequestFactory;

public final class VKActor {

    public static final class Builder {

        private RequestFactory requestFactory;

        private JsonManager jsonManager;

        private String accessToken;

        private String version;

        private Integer targetId;

        Builder() {
            super();
        }

        public Builder requestFactory(RequestFactory requestFactory) {
            this.requestFactory = requestFactory;
            return this;
        }

        public Builder jsonManager(JsonManager jsonManager) {
            this.jsonManager = jsonManager;
            return this;
        }

        public Builder accessToken(String accessToken) {
            this.accessToken = accessToken;
            return this;
        }

        public Builder version(String version) {
            this.version = version;
            return this;
        }

        public Builder targetId(Integer targetId) {
            this.targetId = targetId;
            return this;
        }

        public VKActor build() {
            return new VKActor(requestFactory, jsonManager, accessToken, version, targetId);
        }
    }

    private static final RequestFactory DEFAULT_REQUEST_FACTORY = HttpRequestFactory.getInstance();
    private static final JsonManager DEFAULT_JSON_MANAGER = GsonManager.getInstance();

    public static final String API_VERSION = "5.131";

    public static Builder builder() {
        return new Builder();
    }

    public static Builder default_() {
        Builder builder = builder();
        builder.requestFactory(DEFAULT_REQUEST_FACTORY);
        builder.jsonManager(DEFAULT_JSON_MANAGER);
        builder.version(API_VERSION);
        return builder;
    }

    public static VKActor fromAccessToken(String accessToken) {
        return default_().accessToken(accessToken).build();
    }

    private final RequestFactory requestFactory;

    private final JsonManager jsonManager;

    private final String accessToken;

    private final String version;

    private final Integer targetId;

    VKActor(RequestFactory requestFactory, JsonManager jsonManager, String accessToken, String version,
            Integer targetId) {
        this.requestFactory = requestFactory;
        this.jsonManager = jsonManager;
        this.accessToken = accessToken;
        this.version = version;
        this.targetId = targetId;
    }

    public RequestFactory getRequestFactory() {
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

    public Integer getTargetId() {
        return targetId;
    }

    public Builder toBuilder() {
        Builder builder = builder();
        builder.requestFactory(requestFactory);
        builder.jsonManager(jsonManager);
        builder.accessToken(accessToken);
        builder.version(version);
        builder.targetId(targetId);
        return builder;
    }
}
