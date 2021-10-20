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

#### Simple checked calling an API method (sending message)

```java
VKActor actor = new VKActor(ACCESS_TOKEN);

try {
    // https://vk.com/dev/messages.send
    JsonElement response = new VKMethod("messages.send")
            .param("chat_id", 1)
            .param("random_id", 0)
            .param("message", "Hello, world!")
            .execute(actor);
    
    System.err.println("Message has been sent, it's ID: " + response);
} catch (ApiException e) {
    // do anything with e.getError()
}
```

#### Simple unchecked calling an API method (getting VK server time)

```java
VKActor actor = new VKActor(ACCESS_TOKEN);
// https://vk.com/dev/utils.getServerTime
JsonElement response = VKMethod.uncheckedExecute(actor, new VKMethod("utils.getServerTime"));
System.err.println("VK server time: " + response);
```

#### Group Long Polling

```java
VKActor actor = new VKActor(GROUP_ACCESS_TOKEN);
// group_id can be obtained via groups.getById (https://vk.com/dev/groups.getById)
LongPolling longPolling = new GroupLongPolling(actor, GROUP_ID) {
    @Override
    protected void onMessageNew(int groupId, JsonObject data) {
        JsonObject message = data.getAsJsonObject("message");
        if (message.get("peer_id").getAsInt() < 2_000_000_000) { // is not a conversation
            System.err.printf("User %s wrote something in group %d.%n", message.get("from_id"), groupId);
        }
    }
    
    @Override
    protected void onLikeAdd(int groupId, JsonObject data) {
        System.err.printf("User %s liked something in group %d.%n", data.get("liker_id"), groupId);
    }
    
    @Override
    protected void onGroupJoin(int groupId, JsonObject data) {
        System.err.printf("User %s joiner to the group %d.%n", data.get("user_id"), groupId);
    }
};
longPolling.start();
longPolling.join();
```

#### User Long Polling

```java
VKActor actor = new VKActor(ACCESS_TOKEN);
LongPolling longPolling = new UserLongPolling(actor) {
    @Override
    protected void onMessageNew(JsonArray data) {
        int flags = data.get(2).getAsInt();
        if ((flags & (2 | 8192)) == 0) { // this is private and not an outgoing message
            int peerId = data.get(3).getAsInt();
            if (peerId >= 0) {
                System.err.printf("User %d wrote something to you.%n", peerId);
            } else {
                System.err.printf("Group %d wrote something to you.%n", -peerId);
            }
        }
    }
    
    @Override
    protected void onOutputMessagesRead(JsonArray data) {
        System.err.printf("Your messages have been read in peer %d.%n", data.get(1).getAsInt());
    }
};
longPolling.start();
longPolling.join();
```

#### Complex checked calling an API methods (getting messages history for the last 5 minutes)

```java
VKActor actor = new VKActor(ACCESS_TOKEN);

try {
    // https://vk.com/dev/execute, https://vk.com/dev/messages.getHistory
    // Let's imagine that MessageItem is some kind of wrapper for a message.
    MessageItem[] items = new VKMethod("execute")
            .param("code", "var a=API.utils.getServerTime()-(60*5);var b=API.messages.getHistory({peer_id:2000000312}).items;var c=[];var d=0;while(d<b.length){if(b[d].date>a)c.push(b[d]);d=d+1;}return c;")
            .executeAs(actor, MessageItem[].class);
    
    System.out.println(Arrays.toString(items));
} catch (ApiException e) {
    // do anything with e.getError()
}
```

Issues and Pull Requests are welcome.