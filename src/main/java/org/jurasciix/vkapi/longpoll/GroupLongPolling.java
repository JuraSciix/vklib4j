package org.jurasciix.vkapi.longpoll;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.jurasciix.vkapi.ApiException;
import org.jurasciix.vkapi.VKActor;
import org.jurasciix.vkapi.VKMethod;

import java.util.LinkedHashMap;
import java.util.Map;

public class GroupLongPolling extends LongPolling {

    public static final String EVENT_MESSAGE_NEW = "message_new";
    public static final String EVENT_MESSAGE_REPLY = "message_reply";
    public static final String EVENT_MESSAGE_EDIT = "message_edit";
    public static final String EVENT_MESSAGE_ALLOW = "message_allow";
    public static final String EVENT_MESSAGE_DENY = "message_deny";
    public static final String EVENT_MESSAGE_TYPING = "message_typing_state";
    public static final String EVENT_MESSAGE_CALLBACK = "message_event";
    public static final String EVENT_PHOTO_NEW = "photo_new";
    public static final String EVENT_PHOTO_COMMENT_NEW = "photo_comment_new";
    public static final String EVENT_PHOTO_COMMENT_EDIT = "photo_comment_edit";
    public static final String EVENT_PHOTO_COMMENT_RESTORE = "photo_comment_restore";
    public static final String EVENT_PHOTO_COMMENT_DELETE = "photo_comment_delete";
    public static final String EVENT_AUDIO_NEW = "audio_new";
    public static final String EVENT_VIDEO_NEW = "video_new";
    public static final String EVENT_VIDEO_COMMENT_NEW = "video_comment_new";
    public static final String EVENT_VIDEO_COMMENT_EDIT = "video_comment_edit";
    public static final String EVENT_VIDEO_COMMENT_RESTORE = "video_comment_restore";
    public static final String EVENT_VIDEO_COMMENT_DELETE = "video_comment_delete";
    public static final String EVENT_WALL_POST_NEW = "wall_post_new";
    public static final String EVENT_WALL_REPOST = "wall_repost";
    public static final String EVENT_WALL_REPLY_NEW = "wall_reply_new";
    public static final String EVENT_WALL_REPLY_EDIT = "wall_reply_edit";
    public static final String EVENT_WALL_REPLY_RESTORE = "wall_reply_restore";
    public static final String EVENT_WALL_REPLY_DELETE = "wall_reply_delete";
    public static final String EVENT_LIKE_ADD = "like_add";
    public static final String EVENT_LIKE_REMOVE = "like_remove";
    public static final String EVENT_BOARD_POST_NEW = "board_post_new";
    public static final String EVENT_BOARD_POST_EDIT = "board_post_edit";
    public static final String EVENT_BOARD_POST_RESTORE = "board_post_restore";
    public static final String EVENT_BOARD_POST_DELETE = "board_post_delete";
    public static final String EVENT_MARKET_COMMENT_NEW = "market_comment_new";
    public static final String EVENT_MARKET_COMMENT_EDIT = "market_comment_edit";
    public static final String EVENT_MARKET_COMMENT_RESTORE = "market_comment_restore";
    public static final String EVENT_MARKET_COMMENT_DELETE = "market_comment_delete";
    public static final String EVENT_MARKET_ORDER_NEW = "market_order_new";
    public static final String EVENT_MARKET_ORDER_EDIT = "market_order_edit";
    public static final String EVENT_GROUP_LEAVE = "group_leave";
    public static final String EVENT_GROUP_JOIN = "group_join";
    public static final String EVENT_USER_BLOCK = "user_block";
    public static final String EVENT_USER_UNBLOCK = "user_unblock";
    public static final String EVENT_POLL_VOTE_NEW = "poll_vote_new";
    public static final String EVENT_GROUP_OFFICERS_EDIT = "group_officers_edit";
    public static final String EVENT_CHANGE_SETTINGS = "group_change_settings";
    public static final String EVENT_CHANGE_PHOTO = "group_change_photo";
    public static final String EVENT_VKPAY_TRANSACTION = "vkpay_transaction";
    public static final String EVENT_APP_PAYLOAD = "app_payload";
    public static final String EVENT_DONUT_SUBSCRIPTION_CREATE = "donut_subscription_create";
    public static final String EVENT_DONUT_SUBSCRIPTION_PROLONGED = "donut_subscription_prolonged";
    public static final String EVENT_DONUT_SUBSCRIPTION_EXPIRED = "donut_subscription_expired";
    public static final String EVENT_DONUT_SUBSCRIPTION_CANCELLED = "donut_subscription_cancelled";
    public static final String EVENT_DONUT_SUBSCRIPTION_PRICE_CHANGED = "donut_subscription_price_changed";
    public static final String EVENT_DONUT_MONEY_WITHDRAW = "donut_money_withdraw";
    public static final String EVENT_DONUT_MONEY_WITHDRAW_ERROR = "donut_money_withdraw_error";

    protected static final String HTTP_PARAM_WAIT = "wait";

    protected static final String METHOD_GET_SERVER = "groups.getLongPollServer";

    protected static final String METHOD_PARAM_GROUP_ID = "group_id";

    protected static final String JSON_UPDATE_GROUP_ID = "group_id";
    protected static final String JSON_UPDATE_OBJECT = "object";
    protected static final String JSON_UPDATE_TYPE = "type";

    protected static final int DEFAULT_WAIT = RECOMMENDED_WAIT;

    private static Map<String, String> buildAdditionalRequestParams(int wait) {
        Map<String, String> additionalRequestParams = new LinkedHashMap<>();
        additionalRequestParams.put(HTTP_PARAM_WAIT, Integer.toString(wait));
        return additionalRequestParams;
    }

    private static volatile int ID = 0;

    private static synchronized String buildThreadName() {
        String name = (GroupLongPolling.class.getSimpleName() + "-" + ID);
        ID++;
        return name;
    }

    private final int groupId;

    public GroupLongPolling(VKActor actor) {
        this(actor, actor.getTargetId());
    }

    public GroupLongPolling(VKActor actor, int groupId) {
        this(actor, groupId, DEFAULT_WAIT);
    }

    public GroupLongPolling(VKActor actor, int groupId, int wait) {
        super(actor, buildAdditionalRequestParams(wait));
        this.groupId = groupId;
        setName(buildThreadName());
    }

    public int getGroupId() {
        return groupId;
    }

    @Override
    public LongPollServer getServer() throws ApiException {
        VKMethod method = new VKMethod(METHOD_GET_SERVER);
        method.param(METHOD_PARAM_GROUP_ID, groupId);
        return method.executeAs(getActor(), LongPollServer.class);
    }

    @Override
    public void onUpdate(JsonElement sourceUpdate) {
        if (!sourceUpdate.isJsonObject()) {
            throw new IllegalArgumentException(sourceUpdate.getClass() + ": " + sourceUpdate.toString());
        }
        JsonObject update = sourceUpdate.getAsJsonObject();
        String type = update.get(JSON_UPDATE_TYPE).getAsString();
        int groupId = update.get(JSON_UPDATE_GROUP_ID).getAsInt();
        JsonObject data = update.getAsJsonObject(JSON_UPDATE_OBJECT);

        switch (type) {
            case EVENT_MESSAGE_NEW:
                onMessageNew(groupId, data);
                break;
            case EVENT_MESSAGE_REPLY:
                onMessageReply(groupId, data);
                break;
            case EVENT_MESSAGE_EDIT:
                onMessageEdit(groupId, data);
                break;
            case EVENT_MESSAGE_ALLOW:
                onMessageAllow(groupId, data);
                break;
            case EVENT_MESSAGE_DENY:
                onMessageDeny(groupId, data);
                break;
            case EVENT_MESSAGE_TYPING:
                onMessageTyping(groupId, data);
                break;
            case EVENT_MESSAGE_CALLBACK:
                onMessageCallback(groupId, data);
                break;
            case EVENT_PHOTO_NEW:
                onPhotoNew(groupId, data);
                break;
            case EVENT_PHOTO_COMMENT_NEW:
                onPhotoCommentNew(groupId, data);
                break;
            case EVENT_PHOTO_COMMENT_EDIT:
                onPhotoCommentEdit(groupId, data);
                break;
            case EVENT_PHOTO_COMMENT_RESTORE:
                onPhotoCommentRestore(groupId, data);
                break;
            case EVENT_PHOTO_COMMENT_DELETE:
                onPhotoCommentDelete(groupId, data);
                break;
            case EVENT_AUDIO_NEW:
                onAudioNew(groupId, data);
                break;
            case EVENT_VIDEO_NEW:
                onVideoNew(groupId, data);
                break;
            case EVENT_VIDEO_COMMENT_NEW:
                onVideoCommentNew(groupId, data);
                break;
            case EVENT_VIDEO_COMMENT_EDIT:
                onVideoCommentEdit(groupId, data);
                break;
            case EVENT_VIDEO_COMMENT_RESTORE:
                onVideoCommentRestore(groupId, data);
                break;
            case EVENT_VIDEO_COMMENT_DELETE:
                onVideoCommentDelete(groupId, data);
                break;
            case EVENT_WALL_POST_NEW:
                onWallPostNew(groupId, data);
                break;
            case EVENT_WALL_REPOST:
                onWallRepost(groupId, data);
                break;
            case EVENT_WALL_REPLY_NEW:
                onWallReplyNew(groupId, data);
                break;
            case EVENT_WALL_REPLY_EDIT:
                onWallReplyEdit(groupId, data);
                break;
            case EVENT_WALL_REPLY_RESTORE:
                onWallReplyRestore(groupId, data);
                break;
            case EVENT_WALL_REPLY_DELETE:
                onWallReplyDelete(groupId, data);
                break;
            case EVENT_LIKE_ADD:
                onLikeAdd(groupId, data);
                break;
            case EVENT_LIKE_REMOVE:
                onLikeRemove(groupId, data);
                break;
            case EVENT_BOARD_POST_NEW:
                onBoardPostNew(groupId, data);
                break;
            case EVENT_BOARD_POST_EDIT:
                onBoardPostEdit(groupId, data);
                break;
            case EVENT_BOARD_POST_RESTORE:
                onBoardPostRestore(groupId, data);
                break;
            case EVENT_BOARD_POST_DELETE:
                onBoardPostDelete(groupId, data);
                break;
            case EVENT_MARKET_COMMENT_NEW:
                onMarketCommentNew(groupId, data);
                break;
            case EVENT_MARKET_COMMENT_EDIT:
                onMarketCommentEdit(groupId, data);
                break;
            case EVENT_MARKET_COMMENT_RESTORE:
                onMarketCommentRestore(groupId, data);
                break;
            case EVENT_MARKET_COMMENT_DELETE:
                onMarketCommentDelete(groupId, data);
                break;
            case EVENT_MARKET_ORDER_NEW:
                onMarketOrderNew(groupId, data);
                break;
            case EVENT_MARKET_ORDER_EDIT:
                onMarketOrderEdit(groupId, data);
                break;
            case EVENT_GROUP_LEAVE:
                onGroupLeave(groupId, data);
                break;
            case EVENT_GROUP_JOIN:
                onGroupJoin(groupId, data);
                break;
            case EVENT_USER_BLOCK:
                onUserBlock(groupId, data);
                break;
            case EVENT_USER_UNBLOCK:
                onUserUnblock(groupId, data);
                break;
            case EVENT_POLL_VOTE_NEW:
                onPollVoteNew(groupId, data);
                break;
            case EVENT_GROUP_OFFICERS_EDIT:
                onGroupOfficersEdit(groupId, data);
                break;
            case EVENT_CHANGE_SETTINGS:
                onChangeSettings(groupId, data);
                break;
            case EVENT_CHANGE_PHOTO:
                onChangePhoto(groupId, data);
                break;
            case EVENT_VKPAY_TRANSACTION:
                onVkpayTransaction(groupId, data);
                break;
            case EVENT_APP_PAYLOAD:
                onAppPayload(groupId, data);
                break;
            case EVENT_DONUT_SUBSCRIPTION_CREATE:
                onDonutSubscriptionCreate(groupId, data);
                break;
            case EVENT_DONUT_SUBSCRIPTION_PROLONGED:
                onDonutSubscriptionProlonged(groupId, data);
                break;
            case EVENT_DONUT_SUBSCRIPTION_EXPIRED:
                onDonutSubscriptionExpired(groupId, data);
                break;
            case EVENT_DONUT_SUBSCRIPTION_CANCELLED:
                onDonutSubscriptionCancelled(groupId, data);
                break;
            case EVENT_DONUT_SUBSCRIPTION_PRICE_CHANGED:
                onDonutSubscriptionPriceChanged(groupId, data);
                break;
            case EVENT_DONUT_MONEY_WITHDRAW:
                onDonutMoneyWithdraw(groupId, data);
                break;
            case EVENT_DONUT_MONEY_WITHDRAW_ERROR:
                onDonutMoneyWithdrawError(groupId, data);
                break;
            default:
                onUnknownEvent(groupId, data);
        }
    }

    protected void onMessageNew(int groupId, JsonObject data) {
    }

    protected void onMessageReply(int groupId, JsonObject data) {
    }

    protected void onMessageEdit(int groupId, JsonObject data) {
    }

    protected void onMessageAllow(int groupId, JsonObject data) {
    }

    protected void onMessageDeny(int groupId, JsonObject data) {
    }

    protected void onMessageTyping(int groupId, JsonObject data) {
    }

    protected void onMessageCallback(int groupId, JsonObject data) {
    }

    protected void onPhotoNew(int groupId, JsonObject data) {
    }

    protected void onPhotoCommentNew(int groupId, JsonObject data) {
    }

    protected void onPhotoCommentEdit(int groupId, JsonObject data) {
    }

    protected void onPhotoCommentRestore(int groupId, JsonObject data) {
    }

    protected void onPhotoCommentDelete(int groupId, JsonObject data) {
    }

    protected void onAudioNew(int groupId, JsonObject data) {
    }

    protected void onVideoNew(int groupId, JsonObject data) {
    }

    protected void onVideoCommentNew(int groupId, JsonObject data) {
    }

    protected void onVideoCommentEdit(int groupId, JsonObject data) {
    }

    protected void onVideoCommentRestore(int groupId, JsonObject data) {
    }

    protected void onVideoCommentDelete(int groupId, JsonObject data) {
    }

    protected void onWallPostNew(int groupId, JsonObject data) {
    }

    protected void onWallRepost(int groupId, JsonObject data) {
    }

    protected void onWallReplyNew(int groupId, JsonObject data) {
    }

    protected void onWallReplyEdit(int groupId, JsonObject data) {
    }

    protected void onWallReplyRestore(int groupId, JsonObject data) {
    }

    protected void onWallReplyDelete(int groupId, JsonObject data) {
    }

    protected void onLikeAdd(int groupId, JsonObject data) {
    }

    protected void onLikeRemove(int groupId, JsonObject data) {
    }

    protected void onBoardPostNew(int groupId, JsonObject data) {
    }

    protected void onBoardPostEdit(int groupId, JsonObject data) {
    }

    protected void onBoardPostRestore(int groupId, JsonObject data) {
    }

    protected void onBoardPostDelete(int groupId, JsonObject data) {
    }

    protected void onMarketCommentNew(int groupId, JsonObject data) {
    }

    protected void onMarketCommentEdit(int groupId, JsonObject data) {
    }

    protected void onMarketCommentRestore(int groupId, JsonObject data) {
    }

    protected void onMarketCommentDelete(int groupId, JsonObject data) {
    }

    protected void onMarketOrderNew(int groupId, JsonObject data) {
    }

    protected void onMarketOrderEdit(int groupId, JsonObject data) {
    }

    protected void onGroupLeave(int groupId, JsonObject data) {
    }

    protected void onGroupJoin(int groupId, JsonObject data) {
    }

    protected void onUserBlock(int groupId, JsonObject data) {
    }

    protected void onUserUnblock(int groupId, JsonObject data) {
    }

    protected void onPollVoteNew(int groupId, JsonObject data) {
    }

    protected void onGroupOfficersEdit(int groupId, JsonObject data) {
    }

    protected void onChangeSettings(int groupId, JsonObject data) {
    }

    protected void onChangePhoto(int groupId, JsonObject data) {
    }

    protected void onVkpayTransaction(int groupId, JsonObject data) {
    }

    protected void onAppPayload(int groupId, JsonObject data) {
    }

    protected void onDonutSubscriptionCreate(int groupId, JsonObject data) {
    }

    protected void onDonutSubscriptionProlonged(int groupId, JsonObject data) {
    }

    protected void onDonutSubscriptionExpired(int groupId, JsonObject data) {
    }

    protected void onDonutSubscriptionCancelled(int groupId, JsonObject data) {
    }

    protected void onDonutSubscriptionPriceChanged(int groupId, JsonObject data) {
    }

    protected void onDonutMoneyWithdraw(int groupId, JsonObject data) {
    }

    protected void onDonutMoneyWithdrawError(int groupId, JsonObject data) {
    }

    protected void onUnknownEvent(int groupId, JsonObject data) {
    }
}
