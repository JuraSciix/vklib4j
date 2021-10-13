# VKApi

A small and flexible library for easier integration with the VK API in Java.

## Prerequisites

* [Java JDK](https://www.oracle.com/java/technologies/downloads/) version 1.8
* [Maven](https://maven.apache.org/) version 3.6.3

## Dependencies

* [Google Gson](https://github.com/google/gson) version 2.8.5 (builtin)
* [Apache Commons-Lang](https://commons.apache.org/proper/commons-lang/) version 3.4 (builtin)
* [Apache Http Client](https://hc.apache.org/httpcomponents-client-4.5.x/) version 4.5.13
* [Apache Commons-IO](https://commons.apache.org/proper/commons-io/) version 2.4

## Examples

#### Single API method call (send a message)

```java
import org.jurasciix.vkapi.ApiException;
import org.jurasciix.vkapi.VKActor;
import org.jurasciix.vkapi.VKMethod;

public class SendingMessageExample {

    public static void main(String[] args) {
        VKActor actor = new VKActor("Your access token");

        try {
            new VKMethod("messages.send")
                    .param("chat_id", 1)
                    .param("random_id", 0)
                    .param("message", "Hello, world!")
                    .execute(actor);
        } catch (ApiException e) {
            e.printStackTrace();
        }
    }
}
```

#### Group Long Polling

```java
import com.google.gson.JsonObject;
import org.jurasciix.vkapi.VKActor;
import org.jurasciix.vkapi.longpoll.GroupLongPolling;
import org.jurasciix.vkapi.longpoll.LongPolling;

public class GroupLongPollingExample {

    public static void main(String[] args) throws InterruptedException {
        VKActor actor = new VKActor("Your access token");
        LongPolling longPolling = new GroupLongPolling(actor, 1) {
            @Override
            protected void onMessageNew(int groupId, JsonObject data) {
                JsonObject message = data.getAsJsonObject("message");
                System.out.printf("New message in peer %s: %s%n", message.get("peer_id"), message.get("text"));
            }
            @Override
            protected void onGroupJoin(int groupId, JsonObject data) {
                System.out.printf("User with id %s joined the group%n", data.get("user_id"));
            }
            @Override
            protected void onGroupLeave(int groupId, JsonObject data) {
                System.out.printf("User with id %s has %s the group%n", data.get("user_id"),
                        data.get("self").toString().equals("1") ? "left" : "kicked");
            }
        };
        longPolling.start();
        longPolling.join();
    }
}

```

#### User Long Polling

```java
import com.google.gson.JsonArray;
import org.jurasciix.vkapi.VKActor;
import org.jurasciix.vkapi.longpoll.LongPolling;
import org.jurasciix.vkapi.longpoll.UserLongPolling;

public class UserLongPollingExample {

    public static void main(String[] args) throws InterruptedException {
        VKActor actor = new VKActor("Your access token");
        LongPolling longPolling = new UserLongPolling(actor) {
            @Override
            protected void onFriendOnline(JsonArray data) {
                System.out.printf("Friend with id %s became online%n", data.get(1));
            }
            @Override
            protected void onFriendOffline(JsonArray data) {
                System.out.printf("Friend with id %s became offline%n", data.get(1));
            }
            @Override
            protected void onMessageNew(JsonArray data) {
                System.out.printf("New message in peer %s: %s%n", data.get(3), data.get(5));
            }
        };
        longPolling.start();
        longPolling.join();
    }
}

```

#### Complex calling API methods (getting messages history for the last 5 minutes)

```java
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.jurasciix.vkapi.ApiException;
import org.jurasciix.vkapi.VKActor;
import org.jurasciix.vkapi.VKMethod;

import java.util.Arrays;

public class GettingMessagesHistoryFor5MinutesExample {

    private static class MessageItem {
        @SerializedName("from_id")
        public long fromId;

        @SerializedName("text")
        public String text;

        @Override
        public String toString() {
            return new ToStringBuilder(this, ToStringStyle.JSON_STYLE)
                    .append("fromId", fromId)
                    .append("text", text)
                    .toString();
        }
    }

    public static void main(String[] args) {
        VKActor actor = new VKActor("Your access token");
        try {
            MessageItem[] items = new VKMethod("execute")
                    .param("code", "var a=API.utils.getServerTime()-(60*5);var b=API.messages.getHistory({peer_id:2000000001}).items;var c=[];var d=0;while(d<b.length){if(b[d].date>a)c.push(b[d]);d=d+1;}return c;")
                    .executeAs(actor, MessageItem[].class);
            System.out.println(Arrays.toString(items));
        } catch (ApiException e) {
            e.printStackTrace();
        }
    }
}
```