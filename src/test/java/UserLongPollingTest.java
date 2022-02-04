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

import com.google.gson.JsonArray;
import org.jurasciix.vkapi.ApiException;
import org.jurasciix.vkapi.VKActor;
import org.jurasciix.vkapi.VKMethod;
import org.jurasciix.vkapi.longpoll.LongPollServer;
import org.jurasciix.vkapi.longpoll.LongPolling;
import org.jurasciix.vkapi.longpoll.UserLongPolling;

public class UserLongPollingTest {

    private static final String ACCESS_TOKEN = "";

    public static void main(String[] args) throws InterruptedException {
        System.out.println("Starting...");
        VKActor actor = VKActor.withAccessToken(ACCESS_TOKEN);
        LongPolling longPolling = new UserLongPolling(actor, UserLongPolling.options()
                .version(10)
                .mode(UserLongPolling.MODE_GET_ATTACHMENTS)) {
            @Override
            public LongPollServer getServer() throws ApiException {
                LongPollServer server = super.getServer();
                System.out.println("Long Polling started");
                return server;
            }

            @Override
            protected void onMessageNew(JsonArray data) {
                System.out.println("New message: " + data);
                if (data.get(5).getAsString().equalsIgnoreCase("!test")) {
                    try {
                        new VKMethod("messages.edit")
                                .param("message_id", data.get(1))
                                .param("peer_id", data.get(3))
                                .param("message", "Hm")
                                .execute(getActor());
                    } catch (ApiException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        longPolling.start();
        longPolling.join();
    }
}
