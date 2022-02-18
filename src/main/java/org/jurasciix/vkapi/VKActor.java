package org.jurasciix.vkapi;

import java.util.NoSuchElementException;
import java.util.Objects;

public final class VKActor {

    public static final class Builder {

        private VKApi api;

        private String accessToken;

        private long id;

        public Builder api(VKApi api) {
            this.api = Objects.requireNonNull(api, "api");
            return this;
        }

        public Builder accessToken(String accessToken) {
            this.accessToken = Objects.requireNonNull(accessToken, "access token");
            return this;
        }

        public Builder id(long id) {
            if (id == 0L) {
                throw new IllegalArgumentException("id must not be 0");
            } else {
                this.id = id;
                return this;
            }
        }

        private void checkFields() {
            if (api == null) {
                throw new IllegalStateException("api was not specified");
            } else if (accessToken == null) {
                throw new IllegalStateException("access token was not specified");
            }
        }

        public VKActor build() {
            checkFields();
            return new VKActor(api, accessToken, id);
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    private final VKApi api;

    private final String accessToken;

    private final long id;

    VKActor(VKApi api, String accessToken, long id) {
        this.api = api;
        this.accessToken = accessToken;
        this.id = id;
    }

    public VKApi getApi() {
        return api;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public boolean isIdPresent() {
        return id != 0L;
    }

    public long getId() {
        if (!isIdPresent()) {
            throw new NoSuchElementException("no id present");
        } else {
            return id;
        }
    }

    public Builder toBuilder() {
        Builder builder = builder()
                .api(api)
                .accessToken(accessToken);
        if (isIdPresent()) {
            return builder.id(id);
        } else {
            return builder;
        }
    }
}
