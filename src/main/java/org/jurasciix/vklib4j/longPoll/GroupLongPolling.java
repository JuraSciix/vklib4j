package org.jurasciix.vklib4j.longPoll;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.jurasciix.vklib4j.exception.ApiException;
import org.jurasciix.vklib4j.api.VKActor;
import org.jurasciix.vklib4j.api.VKMethod;

public abstract class GroupLongPolling extends LongPolling {

    public static final class Options {

        long groupId;
        boolean groupIdPresent = false;
        int waitTime = DEFAULT_WAIT;

        Options() {
            super();
        }

        public Options groupId(long groupId) {
            if (groupId <= 0L)
                throw new IllegalArgumentException("group id must be positive");
            this.groupId = groupId;
            this.groupIdPresent = true;
            return this;
        }

        public Options waitTime(int waitTime) {
            this.waitTime = waitTime;
            return this;
        }
    }

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

    static final int DEFAULT_WAIT = LongPolling.DEFAULT_WAIT;

    public static final String JSON_UPDATE_TYPE = "type";

    public static final String JSON_UPDATE_OBJECT = "object";

    public static Options options() {
        return new Options();
    }

    private final long groupId;

    protected GroupLongPolling(VKActor actor) {
        super(actor);
        groupId = actor.getId();
    }

    protected GroupLongPolling(VKActor actor, Options options) {
        super(actor, options.waitTime);
        groupId = options.groupIdPresent ? options.groupId : actor.getId();
    }

    public final long getGroupId() {
        return groupId;
    }

    @Override
    public LongPollServer getServer() throws ApiException {
        VKMethod method = new VKMethod("groups.getLongPollServer");
        method.param("group_id", groupId);
        return method.executeAs(getActor(), LongPollServer.class);
    }

    @Override
    public void onUpdate(JsonElement update) {
        String type = update.getAsJsonObject().get(JSON_UPDATE_TYPE).getAsString();
        JsonObject data = update.getAsJsonObject().getAsJsonObject(JSON_UPDATE_OBJECT);

        switch (type) {
            case EVENT_MESSAGE_NEW:                      onMessageNew(data);                   break;
            case EVENT_MESSAGE_REPLY:                    onMessageReply(data);                 break;
            case EVENT_MESSAGE_EDIT:                     onMessageEdit(data);                  break;
            case EVENT_MESSAGE_ALLOW:                    onMessageAllow(data);                 break;
            case EVENT_MESSAGE_DENY:                     onMessageDeny(data);                  break;
            case EVENT_MESSAGE_TYPING:                   onMessageTyping(data);                break;
            case EVENT_MESSAGE_CALLBACK:                 onMessageCallback(data);              break;
            case EVENT_PHOTO_NEW:                        onPhotoNew(data);                     break;
            case EVENT_PHOTO_COMMENT_NEW:                onPhotoCommentNew(data);              break;
            case EVENT_PHOTO_COMMENT_EDIT:               onPhotoCommentEdit(data);             break;
            case EVENT_PHOTO_COMMENT_RESTORE:            onPhotoCommentRestore(data);          break;
            case EVENT_PHOTO_COMMENT_DELETE:             onPhotoCommentDelete(data);           break;
            case EVENT_AUDIO_NEW:                        onAudioNew(data);                     break;
            case EVENT_VIDEO_NEW:                        onVideoNew(data);                     break;
            case EVENT_VIDEO_COMMENT_NEW:                onVideoCommentNew(data);              break;
            case EVENT_VIDEO_COMMENT_EDIT:               onVideoCommentEdit(data);             break;
            case EVENT_VIDEO_COMMENT_RESTORE:            onVideoCommentRestore(data);          break;
            case EVENT_VIDEO_COMMENT_DELETE:             onVideoCommentDelete(data);           break;
            case EVENT_WALL_POST_NEW:                    onWallPostNew(data);                  break;
            case EVENT_WALL_REPOST:                      onWallRepost(data);                   break;
            case EVENT_WALL_REPLY_NEW:                   onWallReplyNew(data);                 break;
            case EVENT_WALL_REPLY_EDIT:                  onWallReplyEdit(data);                break;
            case EVENT_WALL_REPLY_RESTORE:               onWallReplyRestore(data);             break;
            case EVENT_WALL_REPLY_DELETE:                onWallReplyDelete(data);              break;
            case EVENT_LIKE_ADD:                         onLikeAdd(data);                      break;
            case EVENT_LIKE_REMOVE:                      onLikeRemove(data);                   break;
            case EVENT_BOARD_POST_NEW:                   onBoardPostNew(data);                 break;
            case EVENT_BOARD_POST_EDIT:                  onBoardPostEdit(data);                break;
            case EVENT_BOARD_POST_RESTORE:               onBoardPostRestore(data);             break;
            case EVENT_BOARD_POST_DELETE:                onBoardPostDelete(data);              break;
            case EVENT_MARKET_COMMENT_NEW:               onMarketCommentNew(data);             break;
            case EVENT_MARKET_COMMENT_EDIT:              onMarketCommentEdit(data);            break;
            case EVENT_MARKET_COMMENT_RESTORE:           onMarketCommentRestore(data);         break;
            case EVENT_MARKET_COMMENT_DELETE:            onMarketCommentDelete(data);          break;
            case EVENT_MARKET_ORDER_NEW:                 onMarketOrderNew(data);               break;
            case EVENT_MARKET_ORDER_EDIT:                onMarketOrderEdit(data);              break;
            case EVENT_GROUP_LEAVE:                      onGroupLeave(data);                   break;
            case EVENT_GROUP_JOIN:                       onGroupJoin(data);                    break;
            case EVENT_USER_BLOCK:                       onUserBlock(data);                    break;
            case EVENT_USER_UNBLOCK:                     onUserUnblock(data);                  break;
            case EVENT_POLL_VOTE_NEW:                    onPollVoteNew(data);                  break;
            case EVENT_GROUP_OFFICERS_EDIT:              onGroupOfficersEdit(data);            break;
            case EVENT_CHANGE_SETTINGS:                  onChangeSettings(data);               break;
            case EVENT_CHANGE_PHOTO:                     onChangePhoto(data);                  break;
            case EVENT_VKPAY_TRANSACTION:                onVkpayTransaction(data);             break;
            case EVENT_APP_PAYLOAD:                      onAppPayload(data);                   break;
            case EVENT_DONUT_SUBSCRIPTION_CREATE:        onDonutSubscriptionCreate(data);      break;
            case EVENT_DONUT_SUBSCRIPTION_PROLONGED:     onDonutSubscriptionProlonged(data);   break;
            case EVENT_DONUT_SUBSCRIPTION_EXPIRED:       onDonutSubscriptionExpired(data);     break;
            case EVENT_DONUT_SUBSCRIPTION_CANCELLED:     onDonutSubscriptionCancelled(data);   break;
            case EVENT_DONUT_SUBSCRIPTION_PRICE_CHANGED: onDonutSubscriptionPriceChanged(data); break;
            case EVENT_DONUT_MONEY_WITHDRAW:             onDonutMoneyWithdraw(data);            break;
            case EVENT_DONUT_MONEY_WITHDRAW_ERROR:       onDonutMoneyWithdrawError(data);       break;
            default:                                     onUnknownEvent(data);
        }
    }

    protected void onMessageNew(JsonObject data) {
    }

    protected void onMessageReply(JsonObject data) {
    }

    protected void onMessageEdit(JsonObject data) {
    }

    protected void onMessageAllow(JsonObject data) {
    }

    protected void onMessageDeny(JsonObject data) {
    }

    protected void onMessageTyping(JsonObject data) {
    }

    protected void onMessageCallback(JsonObject data) {
    }

    protected void onPhotoNew(JsonObject data) {
    }

    protected void onPhotoCommentNew(JsonObject data) {
    }

    protected void onPhotoCommentEdit(JsonObject data) {
    }

    protected void onPhotoCommentRestore(JsonObject data) {
    }

    protected void onPhotoCommentDelete(JsonObject data) {
    }

    protected void onAudioNew(JsonObject data) {
    }

    protected void onVideoNew(JsonObject data) {
    }

    protected void onVideoCommentNew(JsonObject data) {
    }

    protected void onVideoCommentEdit(JsonObject data) {
    }

    protected void onVideoCommentRestore(JsonObject data) {
    }

    protected void onVideoCommentDelete(JsonObject data) {
    }

    protected void onWallPostNew(JsonObject data) {
    }

    protected void onWallRepost(JsonObject data) {
    }

    protected void onWallReplyNew(JsonObject data) {
    }

    protected void onWallReplyEdit(JsonObject data) {
    }

    protected void onWallReplyRestore(JsonObject data) {
    }

    protected void onWallReplyDelete(JsonObject data) {
    }

    protected void onLikeAdd(JsonObject data) {
    }

    protected void onLikeRemove(JsonObject data) {
    }

    protected void onBoardPostNew(JsonObject data) {
    }

    protected void onBoardPostEdit(JsonObject data) {
    }

    protected void onBoardPostRestore(JsonObject data) {
    }

    protected void onBoardPostDelete(JsonObject data) {
    }

    protected void onMarketCommentNew(JsonObject data) {
    }

    protected void onMarketCommentEdit(JsonObject data) {
    }

    protected void onMarketCommentRestore(JsonObject data) {
    }

    protected void onMarketCommentDelete(JsonObject data) {
    }

    protected void onMarketOrderNew(JsonObject data) {
    }

    protected void onMarketOrderEdit(JsonObject data) {
    }

    protected void onGroupLeave(JsonObject data) {
    }

    protected void onGroupJoin(JsonObject data) {
    }

    protected void onUserBlock(JsonObject data) {
    }

    protected void onUserUnblock(JsonObject data) {
    }

    protected void onPollVoteNew(JsonObject data) {
    }

    protected void onGroupOfficersEdit(JsonObject data) {
    }

    protected void onChangeSettings(JsonObject data) {
    }

    protected void onChangePhoto(JsonObject data) {
    }

    protected void onVkpayTransaction(JsonObject data) {
    }

    protected void onAppPayload(JsonObject data) {
    }

    protected void onDonutSubscriptionCreate(JsonObject data) {
    }

    protected void onDonutSubscriptionProlonged(JsonObject data) {
    }

    protected void onDonutSubscriptionExpired(JsonObject data) {
    }

    protected void onDonutSubscriptionCancelled(JsonObject data) {
    }

    protected void onDonutSubscriptionPriceChanged(JsonObject data) {
    }

    protected void onDonutMoneyWithdraw(JsonObject data) {
    }

    protected void onDonutMoneyWithdrawError(JsonObject data) {
    }

    protected void onUnknownEvent(JsonObject data) {
    }
}
