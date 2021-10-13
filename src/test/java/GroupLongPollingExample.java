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
