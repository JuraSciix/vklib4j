package org.jurasciix.vkapi.longpoll;

import com.google.gson.JsonElement;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.jurasciix.vkapi.exception.ApiException;
import org.jurasciix.vkapi.VKActor;
import org.jurasciix.vkapi.util.Request;
import org.jurasciix.vkapi.util.RequestException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class LongPolling extends Thread {

    public static final int FAILED_INCORRECT_TS = 1;
    public static final int FAILED_INCORRECT_KEY = 2;
    public static final int FAILED_DATA_LOST = 3;
    public static final int FAILED_INCORRECT_VERSION = 4;

    private static final String PARAM_WAIT = "wait";

    protected static final int DEFAULT_WAIT = 25;

    private final VKActor actor;

    private final List<NameValuePair> additionalRequestParams;

    protected LongPolling(VKActor actor) {
        this(actor, DEFAULT_WAIT);
    }

    protected LongPolling(VKActor actor, int waitTime) {
        this(actor, waitTime, new ArrayList<>());
    }

    protected LongPolling(VKActor actor, int waitTime, List<NameValuePair> additionalRequestParams) {
        this.actor = actor;
        additionalRequestParams.add(new BasicNameValuePair(PARAM_WAIT, Integer.toString(waitTime)));
        this.additionalRequestParams = additionalRequestParams;
    }

    public final VKActor getActor() {
        return actor;
    }

    public List<NameValuePair> getAdditionalRequestParams() {
        return Collections.unmodifiableList(additionalRequestParams);
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
            throw new RequestException("Long Polling error occurred", e);
        }
    }

    private void run0() throws ApiException, LongPollServerException {
        LongPollServer server = null;

        while (!isInterrupted()) {
            if (server == null) {
                server = getServer();
            }
            Request.Response response = doRequestServer(server);

            try {
                doHandleResponse(server, response);
            } catch (LongPollServerException e) {
                int failed = e.getFailed();
                if ((failed != FAILED_INCORRECT_KEY) && (failed != FAILED_DATA_LOST)) {
                    throw e;
                }
                // request server again
                server = null;
            }
        }
    }

    protected Request.Response doRequestServer(LongPollServer server) {
        Request request = server.createRequest(actor.getApi().getRequestFactory());
        List<NameValuePair> additionalParameters = additionalRequestParams;
        if (additionalParameters != null) {
            request.addParameters(additionalParameters);
        }
        return request.execute();
    }

    protected void doHandleResponse(LongPollServer server, Request.Response response)
            throws LongPollServerException {
        LongPollResult result = actor.getApi().getJsonManager()
                .fromJson(response.getContent(), LongPollResult.class);
        server.onResult(this, result);
    }
}
