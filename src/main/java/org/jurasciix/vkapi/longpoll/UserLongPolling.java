package org.jurasciix.vkapi.longpoll;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import org.jurasciix.vkapi.ApiException;
import org.jurasciix.vkapi.VKActor;
import org.jurasciix.vkapi.VKMethod;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

public class UserLongPolling extends LongPolling {

    public static final class Options {

        private boolean needPts = false;

        private Integer groupId = null;

        private int mode = DEFAULT_MODE;

        private int version = DEFAULT_VERSION;

        private int wait = DEFAULT_WAIT;

        Options() {
            super();
        }

        public boolean needPts() {
            return needPts;
        }

        public Options needPts(boolean needPts) {
            this.needPts = needPts;
            return this;
        }

        public Integer groupId() {
            return groupId;
        }

        public Options groupId(Integer groupId) {
            this.groupId = groupId;
            return this;
        }

        public int mode() {
            return mode;
        }

        public Options mode(int mode) {
            this.mode = mode;
            return this;
        }

        public int version() {
            return version;
        }

        public Options version(int version) {
            this.version = version;
            return this;
        }

        public int wait_() {
            return wait;
        }

        public Options wait_(int wait) {
            this.wait = wait;
            return this;
        }
    }

    public static final int MODE_GET_ATTACHMENTS = 2;
    public static final int MODE_GET_EXTENDED = 8;
    public static final int MODE_GET_PTS = 32;
    public static final int MODE_GET_EXTRA_ONLINE = 64;
    public static final int MODE_GET_RANDOM_ID = 128;

    public static final int EVENT_FLAGS_UPDATE = 1;
    public static final int EVENT_FLAGS_SET = 2;
    public static final int EVENT_FLAGS_RESET = 3;
    public static final int EVENT_MESSAGE_NEW = 4;
    public static final int EVENT_MESSAGE_EDIT = 5;
    public static final int EVENT_INCOMING_MESSAGES_READ = 6;
    public static final int EVENT_OUTGOING_MESSAGES_READ = 7;
    public static final int EVENT_FRIEND_ONLINE = 8;
    public static final int EVENT_FRIEND_OFFLINE = 9;
    public static final int EVENT_PEER_FLAGS_RESET = 10;
    public static final int EVENT_PEER_FLAGS_UPDATE = 11;
    public static final int EVENT_PEER_FLAGS_SET = 12;
    public static final int EVENT_MESSAGES_DELETE = 13;
    public static final int EVENT_MESSAGES_RESTORE = 14;
    public static final int EVENT_MAJOR_ID_UPDATE = 20;
    public static final int EVENT_MINOR_ID_UPDATE = 21;
    public static final int EVENT_CHAT_EDIT = 51;
    public static final int EVENT_CHAT_UPDATE = 52;
    public static final int EVENT_USER_TYPING = 61;
    public static final int EVENT_USER_TYPING_IN_CHAT = 62;
    public static final int EVENT_USERS_TYPING_IN_CHAT = 63;
    public static final int EVENT_USERS_VOICING_IN_CHAT = 64;
    public static final int EVENT_USER_VOICING = 70;
    public static final int EVENT_COUNTER_UPDATE = 80;
    public static final int EVENT_NOTIFICATIONS_EDIT = 114;

    protected static final String HTTP_PARAM_VERSION = "version";
    protected static final String HTTP_PARAM_MODE = "mode";
    protected static final String HTTP_PARAM_WAIT = "wait";

    protected static final String METHOD_GET_SERVER = "messages.getLongPollServer";

    protected static final String METHOD_PARAM_GROUP_ID = "group_id";
    protected static final String METHOD_PARAM_NEED_PTS = "need_pts";

    protected static final int JSON_UPDATE_CODE = 0;

    protected static final boolean DEFAULT_NEED_PTS = false;
    protected static final int DEFAULT_VERSION = 10;
    protected static final int DEFAULT_MODE = 2;
    protected static final int DEFAULT_WAIT = RECOMMENDED_WAIT;

    private static Map<String, String> buildAdditionalRequestParams(Options opts) {
        Map<String, String> additionalRequestParameters = new LinkedHashMap<>();
        if (opts == null) {
            return additionalRequestParameters;
        }
        additionalRequestParameters.put(HTTP_PARAM_MODE, Integer.toString(opts.mode()));
        additionalRequestParameters.put(HTTP_PARAM_VERSION, Integer.toString(opts.version()));
        additionalRequestParameters.put(HTTP_PARAM_WAIT, Integer.toString(opts.wait_()));
        return additionalRequestParameters;
    }

    private static volatile int ID = 0;

    private static synchronized String buildThreadName() {
        String name = (UserLongPolling.class.getSimpleName() + "-" + ID);
        ID++;
        return name;
    }

    public static Options options() {
        return new Options();
    }

    private final Integer groupId;

    private final boolean needPts;

    public UserLongPolling(VKActor actor) {
        this(actor, null);
    }

    public UserLongPolling(VKActor actor, Options options) {
        super(actor, buildAdditionalRequestParams(options));
        groupId = (options == null) ? null : options.groupId();
        needPts = options != null && options.needPts();
    }

    public Integer getGroupId() {
        return groupId;
    }

    public boolean isNeedPts() {
        return needPts;
    }

    @Override
    public LongPollServer getServer() throws ApiException {
        VKMethod method = new VKMethod(METHOD_GET_SERVER);
        Optional.ofNullable(groupId).ifPresent(groupId -> method.param(METHOD_PARAM_GROUP_ID, groupId));
        if (needPts) {
            method.param(METHOD_PARAM_NEED_PTS, true);
        }
        return method.executeAs(getActor(), LongPollServer.class);
    }

    @Override
    public void onUpdate(JsonElement sourceUpdate) {
        if (!sourceUpdate.isJsonArray()) {
            throw new IllegalArgumentException(sourceUpdate.getClass() + ": " + sourceUpdate.toString());
        }
        JsonArray update = sourceUpdate.getAsJsonArray();
        int code = update.get(JSON_UPDATE_CODE).getAsInt();

        switch (code) {
            case EVENT_FLAGS_UPDATE:
                onFlagsUpdate(update);
                break;
            case EVENT_FLAGS_SET:
                onFlagsSet(update);
                break;
            case EVENT_FLAGS_RESET:
                onFlagsReset(update);
                break;
            case EVENT_MESSAGE_NEW:
                onMessageNew(update);
                break;
            case EVENT_MESSAGE_EDIT:
                onMessageEdit(update);
                break;
            case EVENT_INCOMING_MESSAGES_READ:
                onIncomingMessagesRead(update);
                break;
            case EVENT_OUTGOING_MESSAGES_READ:
                onOutgoingMessagesRead(update);
                break;
            case EVENT_FRIEND_ONLINE:
                onFriendOnline(update);
                break;
            case EVENT_FRIEND_OFFLINE:
                onFriendOffline(update);
                break;
            case EVENT_PEER_FLAGS_RESET:
                onPeerFlagsReset(update);
                break;
            case EVENT_PEER_FLAGS_UPDATE:
                onPeerFlagsUpdate(update);
                break;
            case EVENT_PEER_FLAGS_SET:
                onPeerFlagsSet(update);
                break;
            case EVENT_MESSAGES_DELETE:
                onMessagesDelete(update);
                break;
            case EVENT_MESSAGES_RESTORE:
                onMessagesRestore(update);
                break;
            case EVENT_MAJOR_ID_UPDATE:
                onMajorIdUpdate(update);
                break;
            case EVENT_MINOR_ID_UPDATE:
                onMinorIdUpdate(update);
                break;
            case EVENT_CHAT_EDIT:
                onChatEdit(update);
                break;
            case EVENT_CHAT_UPDATE:
                onChatUpdate(update);
                break;
            case EVENT_USER_TYPING:
                onUserTyping(update);
                break;
            case EVENT_USER_TYPING_IN_CHAT:
                onUserTypingInChat(update);
                break;
            case EVENT_USERS_TYPING_IN_CHAT:
                onUsersTypingInChat(update);
                break;
            case EVENT_USERS_VOICING_IN_CHAT:
                onUsersVoicingInChat(update);
                break;
            case EVENT_USER_VOICING:
                onUserVoicing(update);
                break;
            case EVENT_COUNTER_UPDATE:
                onCounterUpdate(update);
                break;
            case EVENT_NOTIFICATIONS_EDIT:
                onNotificationsEdit(update);
                break;
            default:
                onUnknownEvent(update);
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

    protected void onIncomingMessagesRead(JsonArray data) {
    }

    protected void onOutgoingMessagesRead(JsonArray data) {
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

    protected void onChatEdit(JsonArray data) {
    }

    protected void onChatUpdate(JsonArray data) {
    }

    protected void onUserTyping(JsonArray data) {
    }

    protected void onUserTypingInChat(JsonArray data) {
    }

    protected void onUsersTypingInChat(JsonArray data) {
    }

    protected void onUsersVoicingInChat(JsonArray data) {
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
