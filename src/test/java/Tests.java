import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.jurasciix.vkapi.ApiException;
import org.jurasciix.vkapi.VKActor;
import org.jurasciix.vkapi.VKMethod;
import org.jurasciix.vkapi.longpoll.GroupLongPolling;
import org.jurasciix.vkapi.longpoll.LongPolling;
import org.jurasciix.vkapi.longpoll.UserLongPolling;
import org.jurasciix.vkapi.util.LombokToStringStyle;

import java.util.Arrays;

public class Tests {

    private static final String ACCESS_TOKEN = "";
    private static final String GROUP_ACCESS_TOKEN = "";
    private static final int GROUP_ID = 0;

    public static void main(String[] args) throws InterruptedException {
//        System.err.println("testCheckedMethodCalling");
//        testCheckedMethodCalling();
//
//        System.err.println("testUncheckedMethodCalling");
//        testUncheckedMethodCalling();
//
//        System.err.println("testGroupLongPolling");
//        testGroupLongPolling();
//
//        System.err.println("testUserLongPolling");
//        testUserLongPolling();
//
//        System.err.println("testGettingMessagesHistoryForLast5Minutes");
//        testGettingMessagesHistoryForLast5Minutes();
    }

    public static void testCheckedMethodCalling() {
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
    }

    public static void testUncheckedMethodCalling() {
        VKActor actor = new VKActor(ACCESS_TOKEN);
        // https://vk.com/dev/utils.getServerTime
        JsonElement response = VKMethod.uncheckedExecute(actor, new VKMethod("utils.getServerTime"));
        System.err.println("VK server time: " + response);
    }

    public static void testGroupLongPolling() throws InterruptedException {
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
        //System.err.println("Group Long Polling launched");
        //Thread.sleep(60L * 1000);
        //longPolling.interrupt();
    }

    public static void testUserLongPolling() throws InterruptedException {
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
        //System.err.println("User Long Polling launched");
        //Thread.sleep(60L * 1000);
        //longPolling.interrupt();
    }

    private static class MessageItem {

        @SerializedName("from_id")
        private Integer fromId;

        @SerializedName("text")
        private String text;

        @Override
        public String toString() {
            return new ToStringBuilder(this, LombokToStringStyle.STYLE)
                    .append("fromId", fromId)
                    .append("text", text)
                    .toString();
        }
    }

    public static void testGettingMessagesHistoryForLast5Minutes() {
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
    }
}
