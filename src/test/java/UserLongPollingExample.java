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
