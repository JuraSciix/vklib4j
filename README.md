# A small and flexible library for easier integration with the VK API in Java.

This library simplifies integration with [VK API](https://vk.com/dev/first_guide) (including [User](https://vk.com/dev/using_longpoll) and [Group](https://vk.com/dev/bots_longpoll) Long Polling) through a few interchangeable tools. 

## Prerequisites

* [Java JDK](https://www.oracle.com/java/technologies/downloads/) version 1.8 or later.
* [Maven](https://maven.apache.org/) version 3.6.3 or later.

## Dependencies

#### Built-in
* [Google Gson](https://github.com/google/gson) version 2.8.5
* [Apache Commons-Lang](https://commons.apache.org/proper/commons-lang/) version 3.4

#### Non build-in
* [Apache Http Client](https://hc.apache.org/httpcomponents-client-4.5.x/) version 4.5.13
* [Apache Commons-IO](https://commons.apache.org/proper/commons-io/) version 2.4

### Maven: include non built-in dependencies

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

### Single method calling

Sending message:

```java
VKActor actor = new VKActor(ACCESS_TOKEN);

try {
    new VKMethod("messages.send")
            .param("chat_id", 1)
            .param("random_id", 0)
            .param("message", "Hello, VK API!").execute(actor);
} catch (ApiException e) {
    /* handling e.getError() */
}
```

Execute code without ApiException:

```java
VKActor actor = new VKActor(ACCESS_TOKEN);
VKMethod.performAction(new VKMethod("execute").param("code", CODE), actor);
```

### Group Long Polling

```java
VKActor actor = new VKActor(ACCESS_TOKEN, GROUP_ID);
LongPolling longPolling = new GroupLongPolling(actor) {

    @Override
    protected void onMessageNew(int groupId, JsonObject data) {
        /* Handling a new message */
    }
    
    @Override
    protected void onLikeAdd(int groupId, JsonObject data) {
        /* Handling a new like */
    }
};
longPolling.start();
longPolling.join();
```

### User Long Polling

```java
VKActor actor = new VKActor(ACCESS_TOKEN);
LongPolling longPolling = new UserLongPolling(actor) {

    @Override
    protected void onMessageNew(JsonArray data) {
        /* handling a new message */
    }
    
    @Override
    protected void onChatUpdate(JsonArray data) {
        /* handling a chat profile update */
    }
};
longPolling.start();
longPolling.join();
```

### Converting method responses

Retrieving the VK server time as Java long:

```java
VKActor actor = new VKActor(ACCESS_TOKEN);
try {
    long serverTime = new VKMethod("utils.getServerTime").executeAs(actor, long.class);
} catch (ApiException e) {
    /* handling e.getError() */
}
```

Without ApiException:

```java
VKActor actor = new VKActor(ACCESS_TOKEN);
long serverTime = VKMethod.performActionAs(new VKMethod("utils.getServerTime"), actor, long.class);
```

Issues and Pull Requests are welcome.