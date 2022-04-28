package org.jurasciix.vkapi.model;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.net.URL;
import java.util.List;

@Data
public class Error {

    public static final String JSON_ERROR_CODE = "error_code";

    public static final String JSON_ERROR_SUBCODE = "error_subcode";

    public static final String JSON_ERROR_MESSAGE = "error_msg";

    public static final String JSON_ERROR_TEXT = "error_text";

    public static final String JSON_REQUEST_PARAMS = "request_params";

    public static final String JSON_CAPTCHA_URL = "captcha_url";

    public static final String JSON_CAPTCHA_SID = "captcha_sid";

    public static final String JSON_REDIRECT_URI = "redirect_uri";

    public static final String JSON_CONFIRMATION_TEXT = "confirmation_text";

    @SerializedName(JSON_ERROR_CODE)
    private int code;

    @SerializedName(JSON_ERROR_SUBCODE)
    private int subcode; // since 5.124

    @SerializedName(JSON_ERROR_MESSAGE)
    private String message;

    @SerializedName(JSON_ERROR_TEXT)
    private String text; // since 5.122

    @SerializedName(JSON_REQUEST_PARAMS)
    private List<RequestParam> requestParams;

    @SerializedName(JSON_CAPTCHA_URL)
    private URL captchaUrl; // when error_code=14

    @SerializedName(JSON_CAPTCHA_SID)
    private long captchaSid; // when error_code=14

    @SerializedName(JSON_REDIRECT_URI)
    private String redirectUri; // when error_code=17

    @SerializedName(JSON_CONFIRMATION_TEXT)
    private String confirmationText; // when error_code=24

    @Data
    public static class RequestParam {

        public static final String JSON_KEY = "key";

        public static final String JSON_VALUE = "value";

        @SerializedName(JSON_KEY)
        private String key;

        @SerializedName(JSON_VALUE)
        private String value;
    }
}
