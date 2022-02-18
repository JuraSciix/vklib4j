package org.jurasciix.vkapi;

import org.jurasciix.vkapi.util.JsonManager;
import org.jurasciix.vkapi.util.Request;
import org.jurasciix.vkapi.util.RequestFactory;

import java.util.Objects;

public final class VKApi {

    public static final class Builder {

        private RequestFactory requestFactory;

        private JsonManager jsonManager;

        private String version;

        public Builder requestFactory(RequestFactory requestFactory) {
            this.requestFactory = Objects.requireNonNull(requestFactory, "request factory");
            return this;
        }

        public Builder jsonManager(JsonManager jsonManager) {
            this.jsonManager = Objects.requireNonNull(jsonManager, "json manager");
            return this;
        }

        public Builder version(String version) {
            this.version = Objects.requireNonNull(version, "version");
            return this;
        }

        private void checkFields() {
            if (requestFactory == null) {
                throw new IllegalStateException("request factory was not set");
            } else if (jsonManager == null) {
                throw new IllegalStateException("json manager was not set");
            } else if (version == null) {
                throw new IllegalStateException("version was not set");
            }
        }

        public VKApi build() {
            checkFields();
            return new VKApi(requestFactory, jsonManager, version);
        }
    }

    private static final String URI_SCHEME = "https";
    private static final String URI_HOST = "api.vk.com";
    private static final String URI_PATH = "method";

    public static Builder builder() {
        return new Builder();
    }

    private final RequestFactory requestFactory;

    private final JsonManager jsonManager;

    private final String version;

    VKApi(RequestFactory requestFactory, JsonManager jsonManager, String version) {
        this.requestFactory = requestFactory;
        this.jsonManager = jsonManager;
        this.version = version;
    }

    public RequestFactory getRequestFactory() {
        return requestFactory;
    }

    public JsonManager getJsonManager() {
        return jsonManager;
    }

    public String getVersion() {
        return version;
    }

    public Request createApiRequest() {
        return requestFactory.create()
                .setScheme(URI_SCHEME)
                .setHost(URI_HOST)
                .setPath(URI_PATH);
    }

    public Builder toBuilder() {
        return builder()
                .requestFactory(requestFactory)
                .jsonManager(jsonManager)
                .version(version);
    }
}
