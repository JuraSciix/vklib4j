package org.jurasciix.vkapi.longpoll;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import org.jurasciix.vkapi.ApiException;
import org.jurasciix.vkapi.VKActor;
import org.jurasciix.vkapi.VKMethod;

import java.util.LinkedHashMap;
import java.util.Map;

public class UserLongPolling extends LongPolling {

    public static final String HTTP_PARAM_MODE = "mode";
    public static final String HTTP_PARAM_VERSION = "version";
    public static final String HTTP_PARAM_WAIT = "wait";

    public static final String GET_SERVER_METHOD = "messages.getLongPollServer";

    public static final String PARAM_GROUP_ID = "group_id";
    public static final String PARAM_NEED_PTS = "need_pts";

    public static final int JSON_UPDATE_CODE = 0;

    public static final int EVENT_FLAGS_UPDATE = 1;
    public static final int EVENT_FLAGS_SET = 2;
    public static final int EVENT_FLAGS_RESET = 3;
    public static final int EVENT_MESSAGE_NEW = 4;
    public static final int EVENT_MESSAGE_EDIT = 5;
    public static final int EVENT_INPUT_MESSAGES_READ = 6;
    public static final int EVENT_OUTPUT_MESSAGES_READ = 7;
    public static final int EVENT_FRIEND_ONLINE = 8;
    public static final int EVENT_FRIEND_OFFLINE = 9;
    public static final int EVENT_PEER_FLAGS_RESET = 10;
    public static final int EVENT_PEER_FLAGS_UPDATE = 11;
    public static final int EVENT_PEER_FLAGS_SET = 12;
    public static final int EVENT_MESSAGES_DELETE = 13;
    public static final int EVENT_MESSAGES_RESTORE = 14;
    public static final int EVENT_MAJOR_ID_UPDATE = 20;
    public static final int EVENT_MINOR_ID_UPDATE = 21;
    public static final int EVENT_CHAT_PARAM_UPDATE = 51;
    public static final int EVENT_CHAT_INFO_UPDATE = 52;
    public static final int EVENT_USER_TYPING = 61;
    public static final int EVENT_PEER_USER_TYPING = 62;
    public static final int EVENT_PEER_USERS_TYPING = 63;
    public static final int EVENT_PEER_USERS_VOICING = 64;
    public static final int EVENT_USER_VOICING = 70;
    public static final int EVENT_COUNTER_UPDATE = 80;
    public static final int EVENT_NOTIFICATIONS_EDIT = 114;

    private static Map<String, String> buildAdditionalRequestParams(int mode, int version, int wait) {
        Map<String, String> additionalRequestParameters = new LinkedHashMap<>();
        additionalRequestParameters.put(HTTP_PARAM_MODE, Integer.toString(mode));
        additionalRequestParameters.put(HTTP_PARAM_VERSION, Integer.toString(version));
        additionalRequestParameters.put(HTTP_PARAM_WAIT, Integer.toString(wait));
        return additionalRequestParameters;
    }

    private static volatile int ID = 0;

    private static synchronized String buildThreadName() {
        String name = (UserLongPolling.class.getSimpleName() + "-" + ID);
        ID++;
        return name;
    }

    private final Integer groupId;

    private final Boolean needPts;

    public UserLongPolling(VKActor actor) {
        this(actor, null);
    }

    public UserLongPolling(VKActor actor, Integer groupId) {
        this(actor, groupId, null);
    }

    public UserLongPolling(VKActor actor, Integer groupId, Boolean needPts) {
        this(actor, groupId, needPts, 2);
    }

    public UserLongPolling(VKActor actor, Integer groupId, Boolean needPts, int mode) {
        this(actor, groupId, needPts, mode, 10);
    }

    public UserLongPolling(VKActor actor, Integer groupId, Boolean needPts, int mode, int version) {
        this(actor, groupId, needPts, mode, version, 25);
    }

    public UserLongPolling(VKActor actor, Integer groupId, Boolean needPts, int mode, int version, int wait) {
        super(actor, buildAdditionalRequestParams(mode, version, wait));
        this.groupId = groupId;
        this.needPts = needPts;
        setName(buildThreadName());
    }

    public Integer getGroupId() {
        return groupId;
    }

    public boolean isNeedPts() {
        return needPts != null && needPts;
    }

    @Override
    public LongPollServer getServer() throws ApiException {
        VKMethod method = new VKMethod(GET_SERVER_METHOD);
        if (groupId != null) method.param(PARAM_GROUP_ID, groupId);
        if (needPts != null) method.param(PARAM_NEED_PTS, needPts);
        return method.executeAs(getActor(), LongPollServer.class);
    }

    @Override
    public void onUpdate(JsonElement sourceUpdate) {
        JsonArray update = sourceUpdate.getAsJsonArray();
        int code = update.get(JSON_UPDATE_CODE).getAsInt();

        switch (code) {
            case EVENT_FLAGS_UPDATE:         onFlagsUpdate(update);        break;
            case EVENT_FLAGS_SET:            onFlagsSet(update);           break;
            case EVENT_FLAGS_RESET:          onFlagsReset(update);         break;
            case EVENT_MESSAGE_NEW:          onMessageNew(update);         break;
            case EVENT_MESSAGE_EDIT:         onMessageEdit(update);        break;
            case EVENT_INPUT_MESSAGES_READ:  onInputMessagesRead(update);  break;
            case EVENT_OUTPUT_MESSAGES_READ: onOutputMessagesRead(update); break;
            case EVENT_FRIEND_ONLINE:        onFriendOnline(update);       break;
            case EVENT_FRIEND_OFFLINE:       onFriendOffline(update);      break;
            case EVENT_PEER_FLAGS_RESET:     onPeerFlagsReset(update);     break;
            case EVENT_PEER_FLAGS_UPDATE:    onPeerFlagsUpdate(update);    break;
            case EVENT_PEER_FLAGS_SET:       onPeerFlagsSet(update);       break;
            case EVENT_MESSAGES_DELETE:      onMessagesDelete(update);     break;
            case EVENT_MESSAGES_RESTORE:     onMessagesRestore(update);    break;
            case EVENT_MAJOR_ID_UPDATE:      onMajorIdUpdate(update);      break;
            case EVENT_MINOR_ID_UPDATE:      onMinorIdUpdate(update);      break;
            case EVENT_CHAT_PARAM_UPDATE:    onChatParamUpdate(update);    break;
            case EVENT_CHAT_INFO_UPDATE:     onChatInfoUpdate(update);     break;
            case EVENT_USER_TYPING:          onUserTyping(update);         break;
            case EVENT_PEER_USER_TYPING:     onPeerUserTyping(update);     break;
            case EVENT_PEER_USERS_TYPING:    onPeerUsersTyping(update);    break;
            case EVENT_PEER_USERS_VOICING:   onPeerUsersVoicing(update);   break;
            case EVENT_USER_VOICING:         onUserVoicing(update);        break;
            case EVENT_COUNTER_UPDATE:       onCounterUpdate(update);      break;
            case EVENT_NOTIFICATIONS_EDIT:   onNotificationsEdit(update);  break;
            default:                         onUnknownEvent(update);
        }
    }

    protected void onFlagsUpdate(JsonArray data) {
    }

    protected void onFlagsSet(JsonArray data) {
    }

    protected void onFlagsReset(JsonArray data) {
    }

    protected void onMessageNew(JsonArray data) {
    }

    protected void onMessageEdit(JsonArray data) {
    }

    protected void onInputMessagesRead(JsonArray data) {
    }

    protected void onOutputMessagesRead(JsonArray data) {
    }

    protected void onFriendOnline(JsonArray data) {
    }

    protected void onFriendOffline(JsonArray data) {
    }

    protected void onPeerFlagsReset(JsonArray data) {
    }

    protected void onPeerFlagsUpdate(JsonArray data) {
    }

    protected void onPeerFlagsSet(JsonArray data) {
    }

    protected void onMessagesDelete(JsonArray data) {
    }

    protected void onMessagesRestore(JsonArray data) {
    }

    protected void onMajorIdUpdate(JsonArray data) {
    }

    protected void onMinorIdUpdate(JsonArray data) {
    }

    protected void onChatParamUpdate(JsonArray data) {
    }

    protected void onChatInfoUpdate(JsonArray data) {
    }

    protected void onUserTyping(JsonArray data) {
    }

    protected void onPeerUserTyping(JsonArray data) {
    }

    protected void onPeerUsersTyping(JsonArray data) {
    }

    protected void onPeerUsersVoicing(JsonArray data) {
    }

    protected void onUserVoicing(JsonArray data) {
    }

    protected void onCounterUpdate(JsonArray data) {
    }

    protected void onNotificationsEdit(JsonArray data) {
    }

    protected void onUnknownEvent(JsonArray data) {
    }
}
