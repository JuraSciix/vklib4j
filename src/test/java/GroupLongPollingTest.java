import com.google.gson.JsonObject;
import org.jurasciix.vkapi.ApiException;
import org.jurasciix.vkapi.VKActor;
import org.jurasciix.vkapi.VKApi;
import org.jurasciix.vkapi.VKMethod;
import org.jurasciix.vkapi.longpoll.GroupLongPolling;
import org.jurasciix.vkapi.longpoll.LongPollServer;
import org.jurasciix.vkapi.longpoll.LongPolling;
import org.jurasciix.vkapi.util.GsonManager;
import org.jurasciix.vkapi.util.HttpRequestFactory;

public class GroupLongPollingTest {

    private static final String ACCESS_TOKEN = "";

    public static void main(String[] args) throws ApiException, InterruptedException {
        VKApi api = VKApi.builder()
                .jsonManager(GsonManager.getInstance())
                .requestFactory(HttpRequestFactory.getInstance())
                .version("5.131").build();
        VKActor actor = VKActor.builder()
                .api(api)
                .accessToken(ACCESS_TOKEN).build();
        System.out.println("Resolving group id...");
        long groupId = new VKMethod("groups.getById").execute(actor)
                .getAsJsonArray().get(0).getAsJsonObject().get("id").getAsLong();
        actor = actor.toBuilder().id(groupId).build();
        LongPolling longPolling = new GroupLongPolling(actor) {
            @Override
            public LongPollServer getServer() throws ApiException {
                LongPollServer server = super.getServer();
                System.out.println("Long Polling started");
                return server;
            }

            @Override
            protected void onMessageNew(JsonObject data) {
                JsonObject message = data.getAsJsonObject("message");
                System.out.println("New message: " + message);
                if (message.get("text").getAsString().equalsIgnoreCase("!test")) {
                    try {
                        new VKMethod("messages.send")
                                .param("random_id", 0)
                                .param("peer_id", message.get("peer_id"))
                                .param("message", "Ok!")
                                .execute(getActor());
                    } catch (ApiException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        System.out.println("Starting Long Polling...");
        longPolling.start();
        longPolling.join();
    }
}
