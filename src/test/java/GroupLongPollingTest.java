/*
 * Copyright 2022-2022, JuraSciix.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import com.google.gson.JsonObject;
import org.jurasciix.vkapi.ApiException;
import org.jurasciix.vkapi.VKActor;
import org.jurasciix.vkapi.VKMethod;
import org.jurasciix.vkapi.longpoll.GroupLongPolling;
import org.jurasciix.vkapi.longpoll.LongPollServer;
import org.jurasciix.vkapi.longpoll.LongPolling;

public class GroupLongPollingTest {

    private static final String ACCESS_TOKEN = "";

    public static void main(String[] args) throws ApiException, InterruptedException {
        VKActor actor = VKActor.withAccessToken(ACCESS_TOKEN);
        System.out.println("Resolving group id...");
        long groupId = new VKMethod("groups.getById").execute(actor)
                .getAsJsonArray().get(0).getAsJsonObject().get("id").getAsLong();
        actor = VKActor.withAccessTokenAndId(ACCESS_TOKEN, groupId);
        LongPolling longPolling = new GroupLongPolling(actor) {
            @Override
            public LongPollServer getServer() throws ApiException {
                LongPollServer server = super.getServer();
                System.out.println("Long Polling started");
                return server;
            }

            @Override
            protected void onMessageNew(JsonObject data) {
                JsonObject message = data.getAsJsonObject("message");
                System.out.println("New message: " + message);
                if (message.get("text").getAsString().equalsIgnoreCase("!test")) {
                    try {
                        new VKMethod("messages.send")
                                .param("random_id", 0)
                                .param("peer_id", message.get("peer_id"))
                                .param("message", "Ok!")
                                .execute(getActor());
                    } catch (ApiException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        System.out.println("Starting Long Polling...");
        longPolling.start();
        longPolling.join();
    }
}
