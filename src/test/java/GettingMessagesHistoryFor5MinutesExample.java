import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.jurasciix.vkapi.ApiException;
import org.jurasciix.vkapi.VKActor;
import org.jurasciix.vkapi.VKMethod;

import java.util.Arrays;

public class GettingMessagesHistoryFor5MinutesExample {

    private static class MessageItem {
        @SerializedName("from_id")
        public long fromId;

        @SerializedName("text")
        public String text;

        @Override
        public String toString() {
            return new ToStringBuilder(this, ToStringStyle.JSON_STYLE)
                    .append("fromId", fromId)
                    .append("text", text)
                    .toString();
        }
    }

    public static void main(String[] args) {
        VKActor actor = new VKActor("Your access token");
        try {
            MessageItem[] items = new VKMethod("execute")
                    .param("code", "var a=API.utils.getServerTime()-(60*5);var b=API.messages.getHistory({peer_id:2000000001}).items;var c=[];var d=0;while(d<b.length){if(b[d].date>a)c.push(b[d]);d=d+1;}return c;")
                    .executeAs(actor, MessageItem[].class);
            System.out.println(Arrays.toString(items));
        } catch (ApiException e) {
            e.printStackTrace();
        }
    }
}