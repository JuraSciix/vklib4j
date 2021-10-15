# A small and flexible library for easier integration with the VK API in Java.

This library simplifies integration with [VK API](https://vk.com/dev/manuals) (including [User](https://vk.com/dev/using_longpoll) and [Group](https://vk.com/dev/bots_longpoll) Long Polling) through a few interchangeable tools. 

## Prerequisites

* [Java JDK](https://www.oracle.com/java/technologies/downloads/) version 1.8 or later.
* [Maven](https://maven.apache.org/) version 3.6.3 or later.

## Dependencies

* [Google Gson](https://github.com/google/gson) version 2.8.5 (builtin)
* [Apache Commons-Lang](https://commons.apache.org/proper/commons-lang/) version 3.4 (builtin)
* [Apache Http Client](https://hc.apache.org/httpcomponents-client-4.5.x/) version 4.5.13
* [Apache Commons-IO](https://commons.apache.org/proper/commons-io/) version 2.4

### Maven: non-builtin dependencies

```xml
<dependencies>
    <dependency>
        <groupId>org.apache.httpcomponents</groupId>
        <artifactId>httpclient</artifactId>
        <version>4.5.13</version>
    </dependency>

    <dependency>
        <groupId>commons-io</groupId>
        <artifactId>commons-io</artifactId>
        <version>2.4</version>
    </dependency>
</dependencies>
```

## Examples

#### A single API method call (send a message)

```java
VKActor actor = new VKActor("Your access token");

try {
    new VKMethod("messages.send")
            .param("chat_id", /* your chat id */ 1)
            .param("random_id", 0)
            .param("message", "Hello, world!")
            .execute(actor);
} catch (ApiException e) {
    e.printStackTrace();
}
```

#### Group Long Polling

```java
VKActor actor = new VKActor("Your access token");
LongPolling longPolling = new GroupLongPolling(actor, /* your group id */ 1) {
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
```

#### User Long Polling

```java
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
```

#### Complex calling API methods (getting messages history for the last 5 minutes)

```java
class MessageItem {
    @SerializedName("from_id") long fromId;
    @SerializedName("text") String text;
}

VKActor actor = new VKActor("Your access token");

try {
    MessageItem[] items = new VKMethod("execute")
            .param("code", "var a=API.utils.getServerTime()-(60*5);var b=API.messages.getHistory({peer_id:2000000001}).items;var c=[];var d=0;while(d<b.length){if(b[d].date>a)c.push(b[d]);d=d+1;}return c;")
            .executeAs(actor, MessageItem[].class);
    for (MessageItem item: items){
        System.out.printf("from_id: %d, text: %s%n", item.fromId, item.text);
    }
} catch (ApiException e) {
    e.printStackTrace();
}
```

Issues and Pull Requests are welcome.