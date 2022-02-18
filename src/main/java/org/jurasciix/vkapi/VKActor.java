package org.jurasciix.vkapi;

import org.jurasciix.vkapi.util.GsonManager;
import org.jurasciix.vkapi.util.HttpRequestFactory;
import org.jurasciix.vkapi.util.JsonManager;
import org.jurasciix.vkapi.util.RequestFactory;

import java.util.NoSuchElementException;
import java.util.Objects;

public final class VKActor {

    public static final class Builder {

        private RequestFactory requestFactory = null;

        private JsonManager jsonManager = null;

        private String accessToken = null;

        private String version = null;

        private long id;

        private boolean idPresent = false;

        Builder() {
            super();
        }

        public Builder requestFactory(RequestFactory requestFactory) {
            this.requestFactory = Objects.requireNonNull(requestFactory);
            return this;
        }

        public Builder jsonManager(JsonManager jsonManager) {
            this.jsonManager = Objects.requireNonNull(jsonManager);
            return this;
        }

        public Builder accessToken(String accessToken) {
            this.accessToken = Objects.requireNonNull(accessToken);
            return this;
        }

        public Builder version(String version) {
            this.version = Objects.requireNonNull(version);
            return this;
        }

        public Builder id(long id) {
            if (id == 0L)
                throw new IllegalArgumentException("id must be not equal to zero");
            this.id = id;
            this.idPresent = true;
            return this;
        }

        public VKActor build() {
            checkFields();
            return new VKActor(requestFactory, jsonManager, accessToken, version, id, idPresent);
        }

        private void checkFields() {
            if (requestFactory == null) {
                throw new IllegalStateException("request factory must be not null");
            }
            if (jsonManager == null) {
                throw new IllegalStateException("json manager must be not null");
            }
            if (accessToken == null) {
                throw new IllegalStateException("access token must be not null");
            }
            if (version == null) {
                throw new IllegalStateException("version must be not null");
            }
        }
    }

    /**
     * Current supported version of the VK API.
     */
    public static final String API_VERSION = "5.131";

    private static final RequestFactory DEFAULT_REQUEST_FACTORY = HttpRequestFactory.getInstance();

    private static final JsonManager DEFAULT_JSON_MANAGER = GsonManager.getInstance();

    private static final String DEFAULT_VERSION = API_VERSION;

    public static Builder builder() {
        return new Builder();
    }

    public static Builder defaultBuilder() {
        Builder builder = builder();
        builder.requestFactory(DEFAULT_REQUEST_FACTORY);
        builder.jsonManager(DEFAULT_JSON_MANAGER);
        builder.version(DEFAULT_VERSION);
        return builder;
    }

    public static VKActor withAccessToken(String accessToken) {
        return defaultBuilder().accessToken(accessToken).build();
    }

    public static VKActor withAccessTokenAndId(String accessToken, long id) {
        return defaultBuilder().accessToken(accessToken).id(id).build();
    }

    private final RequestFactory requestFactory;

    private final JsonManager jsonManager;

    private final String accessToken;

    private final String version;

    private final long id;

    private final boolean idPresent;

    VKActor(RequestFactory requestFactory, JsonManager jsonManager, String accessToken, String version, long id, boolean idPresent) {
        this.requestFactory = requestFactory;
        this.jsonManager = jsonManager;
        this.accessToken = accessToken;
        this.version = version;
        this.id = id;
        this.idPresent = idPresent;
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

    public long getId() {
        if (idPresent)
            return id;
        else
            throw new NoSuchElementException("id not present");
    }

    public boolean isIdPresent() {
        return idPresent;
    }

    public Builder toBuilder() {
        Builder builder = builder();
        builder.requestFactory(requestFactory);
        builder.jsonManager(jsonManager);
        builder.accessToken(accessToken);
        builder.version(version);
        if (idPresent)
            builder.id(id);
        return builder;
    }
}
