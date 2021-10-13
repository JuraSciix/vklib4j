import org.jurasciix.vkapi.ApiException;
import org.jurasciix.vkapi.VKActor;
import org.jurasciix.vkapi.VKMethod;

public class SendingMessageExample {

    public static void main(String[] args) {
        VKActor actor = new VKActor("Your access token");

        try {
            new VKMethod("messages.send")
                    .param("chat_id", 1)
                    .param("random_id", 0)
                    .param("message", "Hello, world!")
                    .execute(actor);
        } catch (ApiException e) {
            e.printStackTrace();
        }
    }
}