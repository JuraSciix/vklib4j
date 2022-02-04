# JVKApi is simple library for working with the VK API in Java.

VKApi makes working with [VK API](https://vk.com/dev/first_guide) (including [User](https://vk.com/dev/using_longpoll) and [Group](https://vk.com/dev/bots_longpoll) Long Polling) a little easier.

## Prerequisites

* [Java JDK](https://www.oracle.com/java/technologies/downloads/) version 1.8 or later.
* [Maven](https://maven.apache.org/) version 3.6.3.

## Dependencies

* [Google Gson](https://github.com/google/gson) version 2.8.5 (built-in)
* [Apache Commons-Lang](https://commons.apache.org/proper/commons-lang/) version 3.4 (built-in)
* [Apache Http Client](https://hc.apache.org/httpcomponents-client-4.5.x/) version 4.5.13

### Maven

```xml
<dependencies>
    <dependency>
        <groupId>org.apache.httpcomponents</groupId>
        <artifactId>httpclient</artifactId>
        <version>4.5.13</version>
    </dependency>
</dependencies>
```

## Examples

### Initialization

Creating a VKActor object using an access token only:

```java
VKActor actor = VKActor.fromAccessToken(ACCESS_TOKEN);
```

Creating a VKActor object using an access token and a user, group, or application ID:

```java
VKActor actor = VKActor.fromAccessTokenAndId(ACCESS_TOKEN, TARGET_ID)
```

Creating a VKActor object with own implementations of modules:

```java
VKActor actor = VKActor.builder()
        .requestFactory(ownRequestFactory)
        .jsonManager(ownJsonManager)
        .accessToken(ACCESS_TOKEN)
        .version(API_VERSION)
        .build();
```

### Sending a message

Sending a message "Hello, VK API!" to chat №1.

```java
try {
    new VKMethod("messages.send")
            .param("chat_id", 1)
            .param("random_id", 0)
            .param("message", "Hello, VK API!")
            .execute(actor);
} catch (ApiException e) {
    e.printStackTrace();
}
```

### Community Long Polling

Receiving and processing events from the VKontakte community

```java
LongPolling longPolling = new GroupLongPolling(actor) {
    @Override
    protected void onMessageNew(JsonObject data) {
        System.out.println(data);
    }
};
longPolling.start();
longPolling.join();
```

### User Long Polling

Receiving and processing events from the VKontakte User.

```java
LongPolling longPolling = new UserLongPolling(actor) {
    @Override
    protected void onMessageNew(JsonArray data) {
        System.out.println(data);
    }
};
longPolling.start();
longPolling.join();
```

### Bonus

Performing VKMethod without ApiException and converting the response to Java value.

```java
VKMethod method = new VKMethod("utils.getServerTime");
long vkServerTime = VKMethod.performAs(method, actor, long.class);
```