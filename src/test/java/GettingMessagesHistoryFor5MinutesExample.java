import com.google.gson.annotations.SerializedName;
import org.jurasciix.vkapi.ApiException;
import org.jurasciix.vkapi.VKActor;
import org.jurasciix.vkapi.VKMethod;

public class GettingMessagesHistoryFor5MinutesExample {

    public static void main(String[] args) {
        class MessageItem {
            @SerializedName("from_id") long fromId;
            @SerializedName("text") String text;
        }

        VKActor actor = new VKActor("Your access token");

        try {
            MessageItem[] items = new VKMethod("execute")
                    .param("code", "var a=API.utils.getServerTime()-(60*5);var b=API.messages.getHistory({peer_id:2000000001}).items;var c=[];var d=0;while(d<b.length){if(b[d].date>a)c.push(b[d]);d=d+1;}return c;")
                    .executeAs(actor, MessageItem[].class);
            for (MessageItem item: items){
                System.out.printf("from_id: %d, text: %s%n", item.fromId, item.text);
            }
        } catch (ApiException e) {
            e.printStackTrace();
        }
    }
}