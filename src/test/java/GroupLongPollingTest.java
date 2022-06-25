import com.google.gson.JsonObject;
import org.jurasciix.vklib4j.exception.ApiException;
import org.jurasciix.vklib4j.api.VKActor;
import org.jurasciix.vklib4j.api.VKApi;
import org.jurasciix.vklib4j.api.VKMethod;
import org.jurasciix.vklib4j.longPoll.GroupLongPolling;
import org.jurasciix.vklib4j.longPoll.LongPollServer;
import org.jurasciix.vklib4j.longPoll.LongPolling;
import org.jurasciix.vklib4j.util.GsonManager;
import org.jurasciix.vklib4j.util.HttpRequestFactory;

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
