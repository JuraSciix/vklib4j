import org.jurasciix.vklib4j.exception.ApiException;
import org.jurasciix.vklib4j.VKActor;
import org.jurasciix.vklib4j.VKApi;
import org.jurasciix.vklib4j.VKMethod;
import org.jurasciix.vklib4j.util.GsonManager;
import org.jurasciix.vklib4j.util.HttpRequestFactory;

public class VKMethodTest {

    private static final String ACCESS_TOKEN = "";

    public static void main(String[] args) {
        VKApi api = VKApi.builder()
                .jsonManager(GsonManager.getInstance())
                .requestFactory(HttpRequestFactory.getInstance())
                .version("5.131").build();
        VKActor actor = VKActor.builder()
                .api(api)
                .accessToken(ACCESS_TOKEN)
                .build();

        String status;

        try {
            status = new VKMethod("status.get")
                    .param("user_id", 1)
                    .execute(actor).getAsJsonObject().get("text").getAsString();
        } catch (ApiException e) {
            e.printStackTrace();
            return;
        }

        System.out.printf("Durov's status: '%s' %n", status);
    }
}
