# JVKApi is simple library for working with the VK API in Java.

VKApi makes working with [VK API](https://vk.com/dev/first_guide) (including [User](https://vk.com/dev/using_longpoll) and [Group](https://vk.com/dev/bots_longpoll) Long Polling) a little easier.

## Prerequisites

* [Java JDK](https://www.oracle.com/java/technologies/downloads/) version 1.8 or later
* [Maven](https://maven.apache.org/) version 3.6.3

## Dependencies

* [Google Gson](https://github.com/google/gson) version 2.8.6
* [Apache Http Client](https://hc.apache.org/httpcomponents-client-4.5.x/) version 4.5.13

## Examples

### Initialization

Creating the VKActor object using access token only:

```java
VKActor actor = VKActor.withAccessToken(ACCESS_TOKEN);
```

Creating the VKActor object using access token and user/group/application id:

```java
VKActor actor = VKActor.withAccessTokenAndId(ACCESS_TOKEN, ID)
```

Creating the VKActor object in the builder:

```java
VKActor actor = VKActor.builder()
        .requestFactory(customRequestFactory)
        .jsonManager(customJsonManager)
        .accessToken(ACCESS_TOKEN)
        .version(API_VERSION)
        .id(ID)
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
    protected void onGroupJoin(JsonObject data) {
        System.out.println("User join: " + data.getAsJsonObject("object"));
    }
    
    @Override
    protected void onGroupLeave(JsonObject data) {
        System.out.println("User leave: " + data.getAsJsonObject("object"));
    }
    
    @Override
    protected void onMessageNew(JsonObject data) {
        System.out.println("New message: " + data.getAsJsonObject("object"));
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
    protected void onFriendOnline(JsonArray data) {
        System.out.println("Friend became online: " + data);
    }

    @Override
    protected void onFriendOffline(JsonArray data) {
        System.out.println("Friend became offline: " + data);
    }

    @Override
    protected void onMessageNew(JsonArray data) {
        System.out.println("New message: " + data);
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