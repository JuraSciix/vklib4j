package org.jurasciix.vkapi.longpoll;

import com.google.gson.JsonElement;
import org.jurasciix.vkapi.ApiException;
import org.jurasciix.vkapi.VKActor;
import org.jurasciix.vkapi.util.Request;
import org.jurasciix.vkapi.util.RequestException;
import org.jurasciix.vkapi.util.Requests;

import java.util.Map;

public abstract class LongPolling extends Thread {

    protected static final int FAILED_INCORRECT_TS = 1;
    protected static final int FAILED_KEY_OUTDATED = 2;
    protected static final int FAILED_DATA_LOST = 3;

    private final VKActor actor;

    private final Map<String, String> additionalRequestParams;

    protected LongPolling(VKActor actor) {
        this(actor, null);
    }

    protected LongPolling(VKActor actor, Map<String, String> additionalRequestParams) {
        this.actor = actor;
        this.additionalRequestParams = additionalRequestParams;
    }

    public VKActor getActor() {
        return actor;
    }

    public abstract LongPollServer getServer() throws ApiException;

    public abstract void onUpdate(JsonElement update);

    @Override
    public void run() {
        if (!isAlive() || isInterrupted()) {
            throw new IllegalStateException();
        }

        try {
            run0();
        } catch (ApiException | LongPollServerException e) {
            throw new RequestException("failed to hold connection with Long Polling", e);
        }
    }

    private void run0() throws ApiException, LongPollServerException {
        LongPollServer server = null;

        while (!isInterrupted()) {
            if (server == null) {
                server = getServer();
            }
            LongPollResult result = requestServer(server);

            try {
                server.onResult(this, result);
            } catch (LongPollServerException e) {
                int failed = e.getFailed();
                if ((failed != FAILED_KEY_OUTDATED) && (failed != FAILED_DATA_LOST)) {
                    throw e;
                }
                server = null;
            }
        }
    }

    private LongPollResult requestServer(LongPollServer server) {
        Request request = server.getRequest(actor.getRequestFactory());
        Map<String, String> additionalParameters = additionalRequestParams;
        if (additionalParameters != null) {
            request.addParameters(additionalParameters);
        }
        Request.Response response = Requests.execute(request);
        return Requests.fromJson(actor.getJsonManager(), response, LongPollResult.class);
    }
}
