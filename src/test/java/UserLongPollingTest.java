import com.google.gson.JsonArray;
import org.jurasciix.vklib4j.exception.ApiException;
import org.jurasciix.vklib4j.api.VKActor;
import org.jurasciix.vklib4j.api.VKApi;
import org.jurasciix.vklib4j.api.VKMethod;
import org.jurasciix.vklib4j.longPoll.LongPollServer;
import org.jurasciix.vklib4j.longPoll.LongPolling;
import org.jurasciix.vklib4j.longPoll.UserLongPolling;
import org.jurasciix.vklib4j.util.GsonManager;
import org.jurasciix.vklib4j.util.HttpRequestFactory;

public class UserLongPollingTest {

    private static final String ACCESS_TOKEN = "";

    public static void main(String[] args) throws InterruptedException {
        System.out.println("Starting...");
        VKApi api = VKApi.builder()
                .jsonManager(GsonManager.getInstance())
                .requestFactory(HttpRequestFactory.getInstance())
                .version("5.131").build();
        VKActor actor = VKActor.builder()
                .api(api)
                .accessToken(ACCESS_TOKEN)
                .build();
        LongPolling longPolling = new UserLongPolling(actor, UserLongPolling.options()
                .version(10)
                .mode(UserLongPolling.MODE_GET_ATTACHMENTS)) {
            @Override
            public LongPollServer getServer() throws ApiException {
                LongPollServer server = super.getServer();
                System.out.println("Long Polling started");
                return server;
            }

            @Override
            protected void onMessageNew(JsonArray data) {
                System.out.println("New message: " + data);
                if (data.get(5).getAsString().equalsIgnoreCase("!test")) {
                    try {
                        new VKMethod("messages.edit")
                                .param("message_id", data.get(1))
                                .param("peer_id", data.get(3))
                                .param("message", "Hm")
                                .execute(getActor());
                    } catch (ApiException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        longPolling.start();
        longPolling.join();
    }
}
